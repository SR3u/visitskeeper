package sr3u.showvisitskeeper;

import sr3u.showvisitskeeper.importexport.ImportItem;
import sr3u.showvisitskeeper.importexport.XlsImporter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@SpringBootApplication
public class ShowvisitskeeperApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ShowvisitskeeperApplication.class, args);
	}

}
