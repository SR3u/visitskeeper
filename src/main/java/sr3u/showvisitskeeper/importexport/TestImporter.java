package sr3u.showvisitskeeper.importexport;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@Component
public class TestImporter {

    @Value("${import.file.xls}")
    String file;
    @Autowired
    XlsImporter xlsImporter;
    @Autowired
    Saver saver;

    @PostConstruct
    public void init() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(file);
        importAll(inputStream);
    }

    private void importAll(FileInputStream inputStream) {
        List<ImportItem> importItems = xlsImporter.read(inputStream);
        saver.save(importItems);
    }
}
