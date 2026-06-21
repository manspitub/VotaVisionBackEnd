package com.jacaranda.manuel.VotaVision.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseSchemaConfig {

	private static final Logger log = LoggerFactory.getLogger(DatabaseSchemaConfig.class);

	@Bean
	ApplicationRunner surveyReportSchemaMaintenance(JdbcTemplate jdbcTemplate) {
		return args -> {
			try {
				jdbcTemplate.execute("ALTER TABLE ReporteEncuesta MODIFY COLUMN estado VARCHAR(32) NOT NULL");
			} catch (Exception e) {
				log.debug("No se pudo ajustar ReporteEncuesta.estado. Puede que la tabla aún no exista.", e);
			}
		};
	}
}
