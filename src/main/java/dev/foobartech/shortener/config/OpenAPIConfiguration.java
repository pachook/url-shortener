package dev.foobartech.shortener.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Bartosz Wołczyk");
        myContact.setEmail("bw@foobartech.dev");

        Info information = new Info()
                .title("URL shortener Service API")
                .version("0.01")
                .description("This API exposes endpoints to deal url shortener application")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
