package org.lelestacia.qurban_ticketing.data.utility

import com.itextpdf.io.image.ImageData
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.renderer.CellRenderer
import com.itextpdf.layout.renderer.DrawContext
import com.itextpdf.layout.renderer.IRenderer

class CouponCellRenderer(
    modelElement: Cell,
    private val imageData: ImageData
) : CellRenderer(modelElement) {

    override fun draw(drawContext: DrawContext) {

        val rect = occupiedAreaBBox
        val canvas = drawContext.canvas

        canvas.addImageFittedIntoRectangle(
            imageData,
            rect,
            false
        )

        super.draw(drawContext)
    }

    override fun getNextRenderer(): IRenderer {
        return CouponCellRenderer(
            modelElement as Cell,
            imageData
        )
    }
}