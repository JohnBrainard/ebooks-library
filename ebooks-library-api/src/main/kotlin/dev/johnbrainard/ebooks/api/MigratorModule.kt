package dev.johnbrainard.ebooks.api

import dev.johnbrainard.ebooks.api.migrator.DbMigrator
import dev.johnbrainard.ebooks.api.migrator.LiquibaseDbMigrator
import org.koin.dsl.module

val migratorModule = module {
	single<DbMigrator>(createdAtStart = true) { LiquibaseDbMigrator(get()) }
}
