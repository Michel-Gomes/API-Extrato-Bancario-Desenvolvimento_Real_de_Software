package br.com.extratobancario.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Informações da API
                .info(new Info()
                        .title("API Extrato Bancário")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de extratos bancários")
                        .termsOfService("")
                        .contact(new Contact()
                                .name("Michel Gomes")
                                .email("michelsillvagomes@gmail.com")
                                .url("https://meusite.com/contact"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))

                // Servidores
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.extratobancario.com")
                                .description("Servidor de Produção")
                ));
    }
}