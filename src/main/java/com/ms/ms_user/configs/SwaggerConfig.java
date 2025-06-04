package com.ms.ms_user.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customSwaggerConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS User")
                        .version("1.0")
                        .description("Aplicação que trata do cadastro e autenticação de usuários (producer)"));
    }

}
