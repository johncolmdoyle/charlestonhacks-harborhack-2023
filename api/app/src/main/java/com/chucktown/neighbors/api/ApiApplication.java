package com.chucktown.neighbors.api;

import static java.util.Arrays.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.log4j.Log4j2;

@Log4j2
@EnableAutoConfiguration
@SpringBootApplication
@ConfigurationPropertiesScan
public class ApiApplication {

    enum DotEnv {
        PORT,
        CLIENT_ORIGIN_URL,
        OKTA_OAUTH2_ISSUER,
        OKTA_OAUTH2_AUDIENCE
    }

    public static void main(final String[] args) {
        dotEnvSafeCheck();

        SpringApplication.run(ApiApplication.class, args);
    }

    private static void dotEnvSafeCheck() {
        final var dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        stream(DotEnv.values())
                .map(DotEnv::name)
                .filter(varName -> dotenv.get(varName, "").isEmpty())
                .findFirst()
                .ifPresent(varName -> {
                    log.error("[Fatal] Missing or empty environment variable: {}", varName);

                    System.exit(1);
                });
    }
}
