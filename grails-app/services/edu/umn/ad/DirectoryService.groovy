package edu.umn.ad
import grails.plugin.springcache.annotations.Cacheable

import org.apache.directory.groovyldap.LDAP
import org.apache.directory.groovyldap.SearchScope
import org.codehaus.groovy.grails.commons.*

class DirectoryService {
    static transactional = true
	static def debug = false

	// default constructor for service…
	DirectoryService() {

		def conf = ConfigurationHolder.config

		// pull config var from grails-app/conf/Config.groovy
		ldapUri = conf.umnad.ldapUri
		ldapUserDn = conf.umnad.ldapUserDn
		ldapUserPw = conf.umnad.ldapUserPw
		ldapPeopleBaseDn = conf.umnad.ldapPeopleBaseDn
		ldapGroupsBaseDn = conf.umnad.ldapGroupsBaseDn
		ldapMemberAttribute = conf.umnad.ldapMemberAttribute
		ldapMemberOfAttribute = conf.umnad.ldapMemberOfAttribute
		rolePrefix = conf.umnad.rolePrefix 
		roleSuffix = conf.umnad.roleSuffix 
	}

	// privately defined variables
	private def ldapUri = 'ldaps://ad.umn.edu'
	private def ldapUserDn = ''
	private def ldapUserPw = ''
	private def ldapPeopleBaseDn = ''
	private def ldapGroupsBaseDn = ''
	private def ldapMemberAttribute = 'member'
	private def ldapMemberOfAttribute = 'memberOf'
	private def rolePrefix = ''
	private def roleSuffix = ''

	private def _authorities = [] as Set
	private def _members = [] as Set

	def getAuthorities = { _authorities }
	def getMembers = { _members }

	String loadUsersByGroupname(final String groupname) {

		// lookup user and data

		def password = "ignore"
		def enabled = true
		def accountNonExpired = true
		def credentialsNonExpired = true
		def accountNonLocked = true
		def groupDescription = ""
		def members = ""
		def groupDn = ""
		def groupFound = false

		// internal variables
		def maxDepth = 5
		def currentDepth = 0
		def fillRoles
		def ldap

		if (debug) { println "groupname: ${groupname}" }

		// Open Connection
		ldap = LDAP.newInstance(ldapUri, ldapUserDn, ldapUserPw)

		// Find the Group
		def groupRecord = ldap.search(filter:"cn=${groupname}", base:ldapGroupsBaseDn, scope:SearchScope.SUB )
		groupRecord.each{

			if (debug) { println "found group record: ${it.dn}" }

			groupDescription = it.description
			// groupDn = it.distinguishedName
			groupDn = it.dn
			groupFound = true
		}

		if (! groupFound) {
			throw GroupnameNotFoundException
		}


		// Find the members
		def searchCriteria = "(${ldapMemberOfAttribute}:1.2.840.113556.1.4.1941:=${groupDn})"
		// (memberof:1.2.840.113556.1.4.1941:=(cn=Group1,OU=groupsOU,DC=x))
		def results = ldap.search(filter:searchCriteria, base:ldapPeopleBaseDn, scope:SearchScope.SUB )

		results.each{
			if (debug) { println "found member: ${it.cn}" }

			def member = [:]
			member.username = it.cn
			member.displayName = it.displayname
			member.email = it.mail

			_members.add(member)
			/*

				SAMPLE LDAP ATTRIBUTES
				----------------------

				lastlogontimestamp:129603066805052311, 
				countrycode:0, 
				department:Lib Arts-TC, 
				givenname:Erica, 
				whenchanged:20110916082819.0Z, 
				memberof:[
					CN=EnHS-NCS-DLR,OU=NCS,OU=Groups,OU=ENHS,OU=SPH,OU=Medical,OU=TC,OU=Units,DC=ad,DC=umn,DC=edu, 
					CN=EnHS-HS-Students,OU=HS,OU=Groups,OU=ENHS,OU=SPH,OU=Medical,OU=TC,OU=Units,DC=ad,DC=umn,DC=edu, 
					CN=EnHS-NCS,OU=NCS,OU=Groups,OU=ENHS,OU=SPH,OU=Medical,OU=TC,OU=Units,DC=ad,DC=umn,DC=edu, 
					CN=HHH Employees,OU=Groups,OU=HHH,OU=TC,OU=Units,DC=ad,DC=umn,DC=edu, 
					CN=HHH Everyone,OU=Groups,OU=HHH,OU=TC,OU=Units,DC=ad,DC=umn,DC=edu
					], 
				lastlogoff:0, 
				instancetype:4, 
				codepage:0, 
				usncreated:19674079, 
				dscorepropagationdata:[
					20110428194320.0Z, 
					20110330223147.0Z, 
					16010101000417.0Z
					], 
				usnchanged:248454448, 
				logoncount:71, 
				badpwdcount:0, 
				initials:R, 
				whencreated:20100330181015.0Z, 
				name:tonk0015, 
				objectcategory:CN=Person,CN=Schema,CN=Configuration,DC=ad,DC=umn,DC=edu
				dn:CN=tonk0015,OU=People,DC=ad,DC=umn,DC=edu
				objectclass:[
					top, 
					person, 
					organizationalPerson, 
					user
					], 
				mail:tonk0015@umn.edu, 
				sn:Tonkin, 
				useraccountcontrol:66048, 
				lastlogon:129603222219720943, 
				samaccounttype:805306368, 
				pwdlastset:129606352999049475, 
				badpasswordtime:129604878780795648, 
				distinguishedname:CN=tonk0015,OU=People,DC=ad,DC=umn,DC=edu, 
				cn:tonk0015, 
				primarygroupid:513, 
				samaccountname:tonk0015, 
				accountexpires:9223372036854775807, 
				userprincipalname:tonk0015@ad.umn.edu, 
				displayname:Erica R Tonkin
			*/
		}

		_members = _members.sort{ it?.displayName }
		
		return groupDescription
	}

	String loadUserByUsername(final String username) {

		// lookup user and data

		def password = "ignore"
		def enabled = true
		def accountNonExpired = true
		def credentialsNonExpired = true
		def accountNonLocked = true
		def userRealName = ""
		def info = ""
		def email = ""

		// internal variables
		def maxDepth = 5
		def currentDepth = 0
		def fillRoles
		def ldap

		// Open Connection
		ldap = LDAP.newInstance(ldapUri, ldapUserDn, ldapUserPw)

		// Find the User
		def userRecord = ldap.search(filter:"cn=${username}", base:ldapPeopleBaseDn, scope:SearchScope.SUB )
		userRecord.each{
			userRealName = it.displayname
			email = it.mail
			info = it.info
		}

		if (! email) {
			throw UsernameNotFoundException
		}


		// Find the groups
		def searchCriteria = "(${ldapMemberAttribute}:1.2.840.113556.1.4.1941:=CN=${username},OU=People,DC=ad,DC=umn,DC=edu)"
		def results = ldap.search(filter:searchCriteria, base:ldapGroupsBaseDn, scope:SearchScope.SUB )

		results.each{
			def authority = it.name.toUpperCase()
			if (authority.indexOf(rolePrefix.toUpperCase()) == 0) {
				authority = authority.replaceFirst(rolePrefix.toUpperCase(), "")
			}
			if (authority.indexOf(roleSuffix.toUpperCase()) > -1) {
				if (authority[-(roleSuffix.toUpperCase().length())..-1] == roleSuffix.toUpperCase()) {
					def l = authority.length()
					def sb = new StringBuffer(authority)
					// replace the end of the string
					authority = sb.replace(l - roleSuffix.toUpperCase().length(), l, "")
				}
			}
			authority = authority.replace('-', '_')
			
			//println " ~ ROLE_${authority}"
			
			_authorities.add("ROLE_${authority}")
		}

		return userRealName
	}
}