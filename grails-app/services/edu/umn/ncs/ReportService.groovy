package edu.umn.ncs
import javax.sql.DataSource
import groovy.sql.Sql

class ReportService {

    static transactional = true
    DataSource dataSource

    def batchAnalysis = { batchId ->

        println "batchId --> ${batchId}"
        def results = null
            if (!dataSource) {
                println "Error: connecting to dataSource in DocumentGenerationService"
            } else {

                def sql = new Sql(dataSource)
                    if (sql) {

                        def selectionQuery = """SELECT ds.segment_id,
                                                        rg.name,
                                                        rs.population_ratio as `segment ratio`,
                                                        rsg.population_ratio as `group ratio` ,
                                                        count(ti.dwelling_unit_id) as Sent
                                                    FROM tracked_item ti INNER JOIN
                                                        batch b ON b.id = ti.batch_id INNER JOIN
                                                        batch_instrument bi ON ti.batch_id = bi.batch_id INNER JOIN
                                                        dwelling_segment ds ON ds.dwelling_unit_id = ti.dwelling_unit_id INNER JOIN
                                                        recruitment_segment rs ON ds.segment_id = rs.id INNER JOIN
                                                        recruitment_segment_group rsg ON rsg.segment_id = rs.id AND rsg.recruitment_group_id = ds.recruitment_group_id INNER JOIN
                                                        recruitment_group rg ON rg.id = rsg.recruitment_group_id
                                                    WHERE (bi.instrument_id = 1)
                                                        AND (b.id = ?)
                                                    GROUP BY segment_id,
                                                        rs.population_ratio,
                                                        rsg.recruitment_group_id,
                                                        rsg.population_ratio
                                                    ORDER BY segment_id, rsg.recruitment_group_id"""

        /*                def selectionParams = [:]

                        if (selectionParams) {
                            results = sql.rows(selectionQuery)
                        } else {
                            results = sql.rows(selectionQuery)
                        }
                        */

                        results = sql.rows(selectionQuery, [batchId])
                        if (results) {
                            println "results are not NULL!!!!"
                        } else {

                            println "selectionQuery returned nothing"
                        }
                    }
            }
            return results
    }
}
