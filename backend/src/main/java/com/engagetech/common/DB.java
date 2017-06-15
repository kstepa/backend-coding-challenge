package com.engagetech.common;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Setups and provides connection to the database, also handles DB migration.
 */
public class DB {
	public final DataSource dataSource;
	public final Configuration jooqCfg;
	public final DSLContext dsl;
	
	public DB(SvcConfig cfg) {
		HikariConfig hikariConfig = new HikariConfig();
		// this is just default. If we use transaction it gets overwritten
		hikariConfig.setAutoCommit(true);
		hikariConfig.setJdbcUrl(cfg.dbUrl());
		hikariConfig.setUsername(cfg.dbUser());
		hikariConfig.setPassword(cfg.dbPassword());
		dataSource = new HikariDataSource(hikariConfig);
		
		jooqCfg = new DefaultConfiguration();
		jooqCfg.set(new DataSourceConnectionProvider(dataSource));
		jooqCfg.set(SQLDialect.MYSQL);
		
		dsl = DSL.using(jooqCfg);
	}
	
	public int migrate() {
		Flyway fw = new Flyway();
		fw.setDataSource(dataSource);
		return fw.migrate();
	}
	
	public int reset() {
		Flyway fw = new Flyway();
		fw.setDataSource(dataSource);
		fw.clean();
		return fw.migrate();
	}
}
