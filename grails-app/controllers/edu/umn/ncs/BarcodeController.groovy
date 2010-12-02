package edu.umn.ncs

import org.krysalis.barcode4j.impl.code39.Code39Bean

class BarcodeController {
    def png = {
        // Create and configure the generator
        def generator = new Code39Bean()
        generator.height = 10

        // render a barcode of whatever the params that were passed
        def barcodeValue = params.id

        renderBarcodePng(generator, barcodeValue)
    }
}