package com.sample.totp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 2FA TOTP Application
 *
 * @author Aaric
 * @version 0.1.0-SNAPSHOT
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Online Api Document",
                description = "2FA TOTP Learning",
                version = "0.1.0",
                contact = @Contact(
                        name = "Aaric", url = "https://github.com/aaric"
                )
        )
)
@SpringBootApplication(exclude = {
        org.springdoc.core.configuration.SpringDocSecurityConfiguration.class
})
public class TotpApp {

    public static void main(String[] args) {
        SpringApplication.run(TotpApp.class, args);
    }
}
