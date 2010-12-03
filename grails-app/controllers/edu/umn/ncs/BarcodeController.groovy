package edu.umn.ncs

import org.krysalis.barcode4j.impl.code39.Code39Bean

class BarcodeController {
    def png = {
        // Create and configure the generator
		def dpi = 256
		def antiAlias = true
        def barcodeValue = params.id
        def generator = new Code39Bean()
        generator.height = 5
		generator.fontSize = 0

        // render a barcode of whatever the params that were passed
        renderBarcodePng(generator, barcodeValue, [antiAlias:antiAlias, dpi:dpi])
    }
}