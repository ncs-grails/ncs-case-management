import edu.umn.ncs.*

class BootStrap {

    def init = { servletContext ->

		// Global BootStrap

		/*** People Seciton ***/
		/* Items: AddressType, ContactRole, Country, EmailType,
		 * EnrollmentType, Ethnicity, Gender, PhoneType, RelationshipType,
		 * Study */

		//AddressType
		def homeAddress = AddressType.findByName('home')
		if (! homeAddress) {
			homeAddress = new AddressType(name:'home').save()
		}
		def seasonalAddress = AddressType.findByName('seasonal')
		if (! homeAddress) {
			seasonalAddress = new AddressType(name:'seasonal').save()
		}
		//ContactRoleType
		def emergencyContact = ContactRoleType.findByName('emergency')
		if (! emergencyContact) {
			emergencyContact = new ContactRoleType(name:'emergency').save()
		}
		//Country
		def us = Country.findByAbbreviation('us')
		if (! us) {
			us = new Country(name:'United States', abbreviation:'us').save()
		}
		//EmailType
		def personalEmail = EmailType.findByName('personal')
		if (! personalEmail) {
			personalEmail = new EmailType(name:'personal').save()
		}
		//EnrollmentType
		def highIntensity = EnrollmentType.findByName('high intensity')
		if (! highIntensity) {
			highIntensity = new EnrollmentType(name:'high intensity').save()
		}
		//Ethnicity
		def latino = Ethnicity.findByName('latino')
		if (! latino) {
			latino = new Ethnicity(name:'latino').save()
		}
		//Gender
		def male = Gender.findByName('male')
		if (! male) {
			male = new Gender(name:'male').save()
		}
		def female = Gender.findByName('female')
		if (! female) {
			female = new Gender(name:'female').save()
		}
		//PhoneType
		def mobilePhone = PhoneType.findByName('mobile')
		if (! mobilePhone) {
			mobilePhone = new PhoneType(name:'mobile').save()
		}
		//RelationshipType
		def mother = RelationshipType.findByName('mother')
		if (! mother) {
			mother = new RelationshipType(name:'mother').save()
		}
		//Study
		def ncs = Study.findByName("National Children's Study")
		if (! ncs) {
			ncs = new Study(name:"NCS",
				fullName:"National Children's Study",
				active:true, sponsor:'NCI/NICHD',
				coordinator:"NICHD",
				collaborator:"TBA",
				purpose:"improving the health of america's children",
				exclusionary: false).save()
		}


    }
    def destroy = {
    }
}
