package sr3u.showvisitskeeper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sr3u.showvisitskeeper.dto.smart.annotations.Mapper;
import sr3u.showvisitskeeper.dto.smart.annotations.MapperImpl;

@Configuration
public class MiscBeans {
    @Bean
    Mapper mapper() {
        return new MapperImpl();
    }
}
