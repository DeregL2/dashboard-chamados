package br.umc.dashboardchamados.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;

@Configuration
@EnableMongoAuditing
@EnableMongoHttpSession
public class MongoConfig {
}
