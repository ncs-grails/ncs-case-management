grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {

        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenRepo "http://artifact.ncs.umn.edu/plugins-release"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

		// Exclude dependencies to resolve conflicts with pdf and renderer plugin
		// compile("org.xhtmlrenderer:core-renderer:R8") {
		// 	excludes 'xml-apis', 'xmlParserAPIs'
		// }

        compile 'groovy-ldap:groovy-ldap:groovy-ldap'
        runtime 'mysql:mysql-connector-java:5.1.18'
    }
    plugins {
		compile ":hibernate:$grailsVersion"
		compile ":tomcat:$grailsVersion"

		compile ":address-lookup-zpfour:0.1.2"
		compile ":audit-logging:0.5.4"
		compile ":barcode4j:0.2"
		compile ":birt-report:2.6.1.0"
		test ":code-coverage:1.2.5"
		test ":codenarc:0.16.1"
		compile ":csv:0.3"
		compile ":joda-time:1.0"
		compile ":jquery:1.7.1"
		compile ":mail:1.0"
		compile ":ncs-appointment:0.3"
		compile ":ncs-calling:0.2"
		compile ":ncs-event:1.2"
		compile ":ncs-norc-link:0.6.2"
		compile ":ncs-people:1.0"
		compile ":ncs-recruitment:1.1"
		compile ":ncs-tracking:3.2.6"
		compile ":ncs-web-template:0.2"
		compile ":pdf:0.6"
		compile ":quartz:0.4.2"
		compile ":springcache:1.3.1"
		
		compile ":webflow:1.3.8"

		compile ":spring-security-core:1.2.7.9"
		// If you're getting errors like the following, try running `grails compile` with the following version of 
		// spring-security-ldap rather than the custom version
		/* :: org.springframework.ldap#org.springframework.ldap;1.3.0.RELEASE: not found
		   :: org.springframework.security#org.springframework.security.ldap;3.0.4.RELEASE: not found
		   */
		// compile ":spring-security-ldap:1.0.5"
		compile ":spring-security-ldap:1.0.6"
		compile ":spring-security-shibboleth-native-sp:1.0.3"
		provided ":spring-security-mock:1.0.1"
	}
}

codenarc.reports = {
	JenkinsXmlReport('xml') {
		outputFile = 'target/test-reports/CodeNarcReport.xml'
		title = 'CodeNarc Report for NCS Case Management'
	}
	JenkinsHtmlReport('html') {
		outputFile = 'CodeNarcReport.html'
		title = 'CodeNarc Report for NCS Case Management'
	}
}
codenarc.propertiesFile = 'grails-app/conf/codenarc.properties'
