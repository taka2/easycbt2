package easycbt2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import easycbt2.thymeleaf_helper.dialect.StringHelperDialect;

@Configuration
public class ThymeleafConfig {
    @Bean
    public StringHelperDialect StringHelperDialect() {
        return new StringHelperDialect();
    }
}
