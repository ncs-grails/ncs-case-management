package edu.umn.ncs

import org.codehaus.groovy.grails.commons.ApplicationHolder
import groovy.sql.Sql

/**
This class is used to enroll people as 
study subjects into various studies
and sub-studies depending on the enrollment criteria.
This service is designed to run as a scheduled task.
*/
class StudyEnrollmentService {

	/** define this service as transactional */
    static transactional = true

	/** We're running some raw SQL here, so we need
	a data source.  This will be automatically injected
	by Grails. */
	def dataSource

	/** This is used to get metadata from the application.
	It's initial use is to get the application name
	for use as provenance data. */	
	def grailsApplication = ApplicationHolder.application

	/**
	This method enrolls people into the full NCS study.
	Enrollment is considered as the point that they complete a
	Lo-I (Low Intensity) questionnaire.
	Right now that data comes from NORC via the 'norc_low_quex1batch' table.
	Because this is data is in a non-grails format (ugly, wide, non-normalized
	table), we're just using a MySQL query to pull it in.
	The SQL itself is pretty generic, so it _should_ run on non-MySQL systems 
	such as MS-SQL, PostgreSQL or Oracle.
	*/
	def enrollNcs() {

		// Define a few variables...
		def now = new Date()
		def study = Study.findByName('NCS')
		def enrollmentType = EnrollmentType.findWhere(name:'low intensity', subEnrollment:false)
		def randomized = true
		def appName = grailsApplication.metadata['app.name']

		// Here lies the query
		def insertQuery = """
			INSERT INTO subject
				(version
				, app_created
				, date_created
				, enrollment_id
				, person_id
				, randomized
				, selection_date
				, study_id
				, subject_id
				, user_created)
			SELECT 0 as version
				, :appName as app_created
				, :now AS date_created
				, :enrollmentId AS enrollment_id
				, pl.person_id
				, :randomized AS randomized
				, nlcb.datacollection_finishtime AS selection_date
				, :studyId AS study_id
				, nlcb.su_id AS subject_id
				, 'nobody' AS user_created
			FROM norc_low_consent_batch nlcb INNER JOIN
				person_link pl ON nlcb.su_id = pl.norc_su_id LEFT OUTER JOIN
				subject sj ON pl.person_id = sj.person_id AND sj.study_id = :studyId
			WHERE (nlcb.datacollection_status01 = 1)
				AND (sj.id IS NULL)"""

		if (study && enrollmentType && appName && dataSource) {
			// build the parameters map
			def parameters = [
				appName: appName,
				now: now,
				enrollmentId: enrollmentType.id,
				randomized: randomized,
				studyId: study.id
				]

			// create a groovy Sql instance.
			def sql = new Sql(dataSource)

			if (sql) {
				// Execute the query
				sql.execute(insertQuery, parameters)
			} else {
				throw GroovySqlIsNullException
			}

		} else if ( ! dataSource ) {
			throw NullDataSourceException
		} else if ( ! study ) {
			throw MissingStudyException
		} else if ( ! enrollmentType ) {
			throw MissingEnrollmentTypeException
		} else if ( ! appName ) {
			throw MissingAppNameException
		}
	}

	/** Enroll NCS High Intensity.
	This method enroll study participants that are in the high intensity
	arm of the study.  It does so by changing their EnrollmentType.
	Right now we get this data from the norc_ppv1batch table that
	is uploaded via their web service. */
	def enrollNcsHighIntensity() {

		// Define a few variables...
		def now = new Date()
		def study = Study.findByName('NCS')
		def enrollmentType = EnrollmentType.findWhere(name:'low intensity', subEnrollment:false)
		def randomized = true
		def appName = grailsApplication.metadata['app.name']

		// Here lies the query
		def insertQuery = """
			INSERT INTO subject
				(version
				, app_created
				, date_created
				, enrollment_id
				, person_id
				, randomized
				, selection_date
				, study_id
				, subject_id
				, user_created)
			SELECT 0 as version
				, :appName as app_created
				, :now AS date_created
				, :enrollmentId AS enrollment_id
				, pl.person_id
				, :randomized AS randomized
				, nlcb.datacollection_finishtime AS selection_date
				, :studyId AS study_id
				, nlcb.su_id AS subject_id
				, 'nobody' AS user_created
			FROM norc_low_consent_batch nlcb INNER JOIN
				person_link pl ON nlcb.su_id = pl.norc_su_id LEFT OUTER JOIN
				subject sj ON pl.person_id = sj.person_id AND sj.study_id = :studyId
			WHERE (nlcb.datacollection_status01 = 1)
				AND (sj.id IS NULL)"""

		if (study && enrollmentType && appName && dataSource) {
			// build the parameters map
			def parameters = [
				appName: appName,
				now: now,
				enrollmentId: enrollmentType.id,
				randomized: randomized,
				studyId: study.id
				]

			// create a groovy Sql instance.
			def sql = new Sql(dataSource)

			if (sql) {
				// Execute the query
				sql.execute(insertQuery, parameters)
			} else {
				throw GroovySqlIsNullException
			}

		} else if ( ! dataSource ) {
			throw NullDataSourceException
		} else if ( ! study ) {
			throw MissingStudyException
		} else if ( ! enrollmentType ) {
			throw MissingEnrollmentTypeException
		} else if ( ! appName ) {
			throw MissingAppNameException
		}
	}
}
