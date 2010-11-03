package edu.umn.ncs

class ReceiptItemsController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "receipt", params: params)
    }

    def receipt = {}
    def updateResult = {}
}
