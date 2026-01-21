package sr3u.showvisitskeeper.importexport;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

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


    @PostConstruct
    public void init() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(file);
        dropIfNeeded();
        importAll(inputStream);
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
