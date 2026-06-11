package br.com.escola.biblioteca.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${biblioteca.openapi.dev-url}")
    private String devUrl;

    @Value("${biblioteca.openapi.prod-url}")
    private String prodUrl;

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server().url(devUrl).description("Servidor de Desenvolvimento");
        Server prodServer = new Server().url(prodUrl).description("Servidor de Produção");

        Contact contact = new Contact()
                .name("Matheus Silveira Santos")
                .url("https://github.com/matheussilveirasantos/API-Livraria");

        License license = new License()
                .name("Apache License 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0");

        Info info = new Info()
                .title("API de Catálogo de Livros e Autores")
                .version("2.0.0")
                .description("API REST para gerenciamento de biblioteca com autenticação JWT, " +
                             "cadastro de Autores, Livros, Gêneros e Editoras.")
                .termsOfService("https://github.com/matheussilveirasantos/API-Livraria")
                .contact(contact)
                .license(license);

        // Esquema de segurança JWT para o Swagger
        
        SecurityScheme securityScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Insira o token JWT obtido no endpoint POST /auth/login. Exemplo: Bearer eyJhbGci...");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
                .tags(List.of(
                        new Tag().name("Autenticação").description("Login e geração de token JWT"),
                        new Tag().name("Autores").description("CRUD de autores"),
                        new Tag().name("Gêneros").description("CRUD de gêneros literários"),
                        new Tag().name("Editoras").description("CRUD de editoras"),
                        new Tag().name("Livros").description("CRUD de livros")
                ));
    }
}
