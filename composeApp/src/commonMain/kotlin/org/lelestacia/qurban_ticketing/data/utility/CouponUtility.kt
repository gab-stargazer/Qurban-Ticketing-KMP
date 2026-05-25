package org.lelestacia.qurban_ticketing.data.utility

import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.AreaBreakType
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.VerticalAlignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Type
import qurbanticketing.composeapp.generated.resources.*
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField


class CouponUtility(
    private val platformUtility: PlatformUtility
) {

    private lateinit var backgroundImage: ImageData

    private suspend fun loadBackgroundImage() {
        backgroundImage = ImageDataFactory.create(
            getDrawableResourceBytes(
                getSystemResourceEnvironment(),
                Res.drawable.background_coupon
            )
        )
    }

    suspend fun saveCoupons(
        userData: List<CouponData>,
        qurbanLocation: String,
        qurbanPickupDate: String
    ) {
        val currentYear: Int = HijrahDate.now().get(ChronoField.YEAR)
        val currentYearFormatted = "$currentYear Hijriah"

        val documentName: String = getString(
            resource = Res.string.coupon_file_name,
            currentYearFormatted
        )

        loadBackgroundImage()

        val os = platformUtility.createQurbanTicketAndGetOS(documentName)
        os?.let { os ->
            withContext(Dispatchers.IO) {
                val writer = PdfWriter(os)
                val pdf = PdfDocument(writer)
                val document = Document(pdf, PageSize.A4)
                document.setMargins(0F, 0F, 0F, 0F)

                userData.chunked(CHUNK_SIZE).forEach { chunk ->

                    val table = Table(floatArrayOf(50f, 50f))
                        .useAllAvailableWidth()

                    table.setMargin(0F)
                    table.setPadding(0F)
                    chunk.forEach { currentData ->
                        val cell = Cell()
                            .setMargin(0F)
                            .setHorizontalAlignment(HorizontalAlignment.LEFT)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)

                        cell.setNextRenderer(
                            CouponCellRenderer(
                                cell,
                                backgroundImage
                            )
                        )

                        cell
                            .setPaddingLeft(12F)
                            .setPaddingTop(12F)
                            .setPaddingBottom(12F)
                            .add(
                                Paragraph()
                                    .add(
                                        Text(
                                            getString(Res.string.coupon_title, currentYearFormatted)
                                        )
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
                                            .setFontSize(12F)
                                    )
                                    .add(
                                        Text(
                                            getString(
                                                Res.string.coupon_name_and_information,
                                                currentData.name,
                                                when (currentData.status) {
                                                    Status.Recipient -> ""
                                                    Status.Participant ->
                                                        "[${getString(currentData.type.uiText)}]"
                                                }
                                            )
                                        )
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                                            .setFontSize(12F)
                                    )
                                    .add(
                                        Text(getString(Res.string.coupon_pickup_location, qurbanLocation))
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                                            .setFontSize(12F)
                                    )
                                    .add(
                                        Text(getString(Res.string.coupon_pickup_date, qurbanPickupDate))
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                                            .setFontSize(12F)
                                    )
                            )

                        table.addCell(cell)
                    }

                    document.add(table)
                    if (chunk != userData.chunked(CHUNK_SIZE).last()) {
                        document.add(AreaBreak(AreaBreakType.NEXT_PAGE))
                    }
                }

                document.close()
                pdf.close()
                writer.close()
            }

            os.close()
        }
    }

    data class CouponData(
        val name: String,
        val status: Status,
        val type: Type
    )

    companion object {
        private const val CHUNK_SIZE = 16
    }
}
