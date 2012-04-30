package edu.umn.ncs

import groovy.sql.Sql

class MySqlService {

    static transactional = true

	def dataSource

	def findForeignKeyReferences(String tableName) {
		// Get database name from dataSource Connection
		def databaseName = dataSource.connection.catalog
		def sql = new Sql(dataSource)
		def references = []
		def tables = [] as Set
		def ansiQuotes = 0

		// get a list of all the tables in the database
		sql.eachRow("SHOW TABLES;"){
			tables.add(it[0])
		}
		// Set table quoting accordingly
		def q = "`"

		def foreignKeyString = "CONSTRAINT ${q}[0-9A-Za-z]*${q} FOREIGN KEY \\(${q}.*${q}\\) REFERENCES ${q}${tableName}${q} \\(${q}id${q}\\)".toString()

		// look for foregn keys in all the tables
		tables.each{ table ->
			if (tableName != table) {
				String sqlQuery = 'SHOW CREATE TABLE ' + table + ';'
				sql.eachRow(sqlQuery){
					def createTableSql = it[1]
					createTableSql.eachLine{ line ->
						def findMatch = line.find(foreignKeyString)
						if (findMatch) {
							// Found a foreign key reference
							def foreignKey = line.find("FOREIGN KEY \\(`.*`\\) REFERENCES")
							def columnName = foreignKey.replaceFirst("FOREIGN KEY \\(`", '').replaceFirst("`\\) REFERENCES", '')
							references.add([database:databaseName,table:table, column:columnName ])
						}
					}
				}
			}
		}

		return  references
	}

	def listForignKeyReferences(List references, Long id) {
		def sql = new Sql(dataSource)
		def records = []

		references.each {
			String sqlQuery = "SELECT * FROM ${it.database}.${it.table} WHERE ${it.column} = ?"
			def rows = 0
			sql.eachRow(sqlQuery, id){
				rows++
			}
			if (rows) {
				records.add([database: it.database, table:it.table, records: rows])
			}
		}

		return records
	}

	def deleteForignKeyReferences(List references, Long id) {
		def sql = new Sql(dataSource)

		references.each {
			String sqlQuery = "DELETE FROM ${it.database}.${it.table} WHERE ${it.column} = ?"
			log.debug "${sqlQuery}; ? == ${id}"
			sql.execute(sqlQuery, id)
		}
	}

}
