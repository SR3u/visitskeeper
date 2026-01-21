package sr3u.showvisitskeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ShowvisitskeeperApplication {



    public static void main(String[] args) throws Exception {
		SpringApplication.run(ShowvisitskeeperApplication.class, args);
	}

}
