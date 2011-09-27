class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/admin/manage/$action?"(controller: "adminManage")


		"/"(controller:"mainMenu")
		"500"(view:'/error')
	}
}
