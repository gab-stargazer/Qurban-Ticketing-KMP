package org.lelestacia.qurban_ticketing.data.utility

import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.AreaBreak
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
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
import org.lelestacia.qurban_ticketing.util.Hour
import org.lelestacia.qurban_ticketing.util.Location
import org.lelestacia.qurban_ticketing.util.Minute
import org.lelestacia.qurban_ticketing.util.PickupDate
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.coupon_file_name
import qurbanticketing.composeapp.generated.resources.coupon_name
import qurbanticketing.composeapp.generated.resources.coupon_name_and_information
import qurbanticketing.composeapp.generated.resources.coupon_participant
import qurbanticketing.composeapp.generated.resources.coupon_pickup_date
import qurbanticketing.composeapp.generated.resources.coupon_pickup_location
import qurbanticketing.composeapp.generated.resources.coupon_pickup_time
import qurbanticketing.composeapp.generated.resources.coupon_recipient
import qurbanticketing.composeapp.generated.resources.coupon_title
import qurbanticketing.composeapp.generated.resources.coupon_title_participant
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField


class CouponUtility(
    private val platformUtility: PlatformUtility
) {

    private lateinit var couponParticipant: ImageData
    private lateinit var couponRecipient: ImageData

    private suspend fun loadBackgroundImage() {
        couponParticipant = ImageDataFactory.create(
            getDrawableResourceBytes(
                getSystemResourceEnvironment(),
                Res.drawable.coupon_participant
            )
        )

        couponRecipient = ImageDataFactory.create(
            getDrawableResourceBytes(
                getSystemResourceEnvironment(),
                Res.drawable.coupon_recipient
            )
        )
    }

    suspend fun saveCoupons(
        userData: List<CouponData>,
        qurbanLocation: Location,
        qurbanPickupDate: PickupDate,
        qurbanStartTime: Pair<Hour, Minute>,
        qurbanFinishTime: Pair<Hour, Minute>,
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
                                when (currentData.status) {
                                    Status.Recipient -> couponRecipient
                                    Status.Participant -> couponParticipant
                                }
                            )
                        )

                        cell
                            .setPaddingLeft(18F)
                            .setPaddingTop(12F)
                            .setPaddingBottom(12F)
                            .add(
                                Paragraph()
                                    .add(
                                        Text(
                                            getString(
                                                when (currentData.status) {
                                                    Status.Recipient -> Res.string.coupon_title
                                                    Status.Participant -> Res.string.coupon_title_participant
                                                },
                                                currentYearFormatted
                                            )
                                        )
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
                                            .setFontSize(11F)
                                    )
                                    .add(
                                        Text(
                                            if (currentData.address.isNotBlank() && currentData.address.lowercase()
                                                    .startsWith("rt") && currentData.address.length < 6
                                            ) {
                                                getString(
                                                    Res.string.coupon_name_and_information,
                                                    currentData.name,
                                                    currentData.address
                                                )
                                            } else {
                                                getString(
                                                    Res.string.coupon_name,
                                                    currentData.name
                                                )
                                            }
                                        )
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                                            .setFontSize(9F)
                                    )
                                    .add(
                                        Text(
                                            getString(
                                                Res.string.coupon_pickup_location,
                                                qurbanLocation.value
                                            )
                                        )
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                                            .setFontSize(9F)
                                    )
                                    .add(
                                        Text(
                                            getString(
                                                Res.string.coupon_pickup_date,
                                                qurbanPickupDate.value
                                            )
                                        )
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                                            .setFontSize(9F)
                                    )
                                    .add(
                                        Text(
                                            getString(
                                                Res.string.coupon_pickup_time,
                                                "${qurbanStartTime.first.value}:${
                                                    if (qurbanStartTime.second.value == 0) {
                                                        "00"
                                                    } else {
                                                        qurbanStartTime.second.value
                                                    }
                                                }",
                                                "${qurbanFinishTime.first.value}:${
                                                    if (qurbanFinishTime.second.value == 0) {
                                                        "00"
                                                    } else {
                                                        qurbanFinishTime.second.value
                                                    }
                                                }",
                                            )
                                        )
                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                                            .setFontSize(9F)
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
        val address: String,
        val status: Status,
        val type: Type?
    )

    companion object {
        private const val CHUNK_SIZE = 16
    }
}
