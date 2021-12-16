package idea.verlif.juststation.global.openapi;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/11/9 9:44
 */
@Configuration
@EnableOpenApi
public class OpenApiConfig {

    @Bean
    public OpenAPI springShopOpenApi() {
        return new OpenAPI()
                .info(new Info().title("JustStation")
                        .description("一个挺好用的Spring Boot框架")
                        .version("v0.0.1")
                        .contact(new Contact().name("Verlif").email("920767796@qq.com"))
                        .license(new License().name("MIT").url("https://github.com/Verlif/JustStation")))
                .externalDocs(new ExternalDocumentation()
                        .description("JustStation WIKI on Github")
                        .url("https://github.com/Verlif/JustStation/wikis/Home"))
                .externalDocs(new ExternalDocumentation()
                        .description("JustStation WIKI on Gitee")
                        .url("https://gitee.com/Verlif/JustStation/wikis/Home"));
    }
}
