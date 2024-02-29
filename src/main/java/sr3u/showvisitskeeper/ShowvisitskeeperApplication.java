package sr3u.showvisitskeeper;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import sr3u.showvisitskeeper.importexport.ImportItem;
import sr3u.showvisitskeeper.importexport.XlsImporter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@SpringBootApplication
public class ShowvisitskeeperApplication {

	private final TemplateEngine templateEngine;

    public ShowvisitskeeperApplication(final ServletContext servletContext) {
		super();

		ServletContextTemplateResolver templateResolver =
				new ServletContextTemplateResolver(servletContext);

		// HTML is the default mode, but we set it anyway for better understanding of code
		templateResolver.setTemplateMode(TemplateMode.HTML);
		// This will convert "home" to "/WEB-INF/templates/home.html"
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		// Template cache TTL=1h. If not set, entries would be cached until expelled by LRU
		templateResolver.setCacheTTLMs(3600000L);

		// Cache is set to true by default. Set to false if you want templates to
		// be automatically updated when modified.
		templateResolver.setCacheable(false);

		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);

    }

    public static void main(String[] args) throws Exception {
		SpringApplication.run(ShowvisitskeeperApplication.class, args);
	}

}
