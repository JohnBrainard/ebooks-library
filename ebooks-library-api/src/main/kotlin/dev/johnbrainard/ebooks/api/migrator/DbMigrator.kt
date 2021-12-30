package dev.johnbrainard.ebooks.api.migrator

import dev.johnbrainard.ebooks.api.logger
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import javax.sql.DataSource

interface DbMigrator {
	fun migrate()
}

class LiquibaseDbMigrator(private val dataSource: DataSource) : DbMigrator {

	private val logger = logger()

	init {
		migrate()
	}

	override fun migrate() {
		logger.info("running database migrations")
		dataSource.connection.use { conn ->

			val jdbcConnection = JdbcConnection(conn)
			val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection)
			val liquibase = Liquibase("db/changelog-master.xml", ClassLoaderResourceAccessor(), database)

			liquibase.update("schema")
		}
		logger.info("database migrations complete")
	}
}
