package sr3u.showvisitskeeper.importexport;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sr3u.showvisitskeeper.entities.ImportEntity;
import sr3u.showvisitskeeper.repo.repositories.ImportRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TestImporter {

    @Value("${import.file.xls}")
    String file;
    @Value("${app.db.drop.all.on.restart}")
    boolean drop = false;

    @Autowired
    XlsImporter xlsImporter;
    @Autowired
    Saver saver;
    @Autowired
    List<JpaRepository<?, ?>> repositories;

    @Autowired
    ImportRepository importRepository;


    @PostConstruct
    public void init() throws IOException {
        String md5 = null;
        try (InputStream is = Files.newInputStream(Paths.get(file))) {
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
            Optional<ImportEntity> lastImport = importRepository.findByFile(file);
            String finalMd = md5;
            if (lastImport.map(li -> finalMd.equalsIgnoreCase(li.getMd5())).orElse(false) && !drop) {
                log.info("[init] File has not changed, skipping import");
                return;
            }
        }
        FileInputStream inputStream = new FileInputStream(file);
        dropIfNeeded();
        importAll(inputStream);
        if(md5 != null) {
            importRepository.deleteByFile(file);
            importRepository.saveAndFlush(ImportEntity.builder()
                    .file(file)
                    .md5(md5)
                    .build());
        }
    }

    @Transactional
    public void dropIfNeeded() {
        if (drop) {
            repositories.forEach(CrudRepository::deleteAll);
        }
    }

    private void importAll(FileInputStream inputStream) {
        List<ImportItem> importItems = xlsImporter.read(inputStream);
        saver.save(importItems);
        log.info("[importAll] done!");
    }
}
