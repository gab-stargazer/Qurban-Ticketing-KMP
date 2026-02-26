package org.lelestacia.qurban_ticketing.data.utility

import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.*
import dev.zwander.kotlin.file.FileUtils
import dev.zwander.kotlin.file.IPlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.asOutputStream
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Type
import org.lelestacia.qurban_ticketing.util.getDocumentDirectory
import qurbanticketing.composeapp.generated.resources.*
import kotlin.time.Clock

class CouponUtility {

    private lateinit var byteArrayLogo: ByteArray
    private lateinit var couponBackgroundImage: ByteArray

    suspend fun saveCouponsV2(
        userData: List<CouponDataV2>,
        qurbanLocation: String = "Lokasi Test",
        qurbanPickupDate: String = "Jumat, 19 Agustus 2023"
    ) {
        val currentYear: Int = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .year

        val appDirectory = FileUtils.fromString(
            input = "${getDocumentDirectory()}/Qurban Ticketing",
            isDirectory = true
        )

        if ((appDirectory?.getExists() ?: false)) {
            appDirectory.mkdirs()
        }

        val file: IPlatformFile? = FileUtils.fromString(
            input = "${getDocumentDirectory()}/${getString(Res.string.coupon_title, currentYear.toString())}.pdf",
            isDirectory = false
        )

        file?.openOutputStream()?.let { fos ->
            withContext(Dispatchers.IO) {
                val writer = PdfWriter(fos.asOutputStream())
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
                            .setHorizontalAlignment(HorizontalAlignment.LEFT)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setPaddingLeft(36F)
                            .setPaddingTop(12F)
                            .setPaddingBottom(12F)
                            .add(
                                Paragraph()
                                    .add(
                                        Text(
                                            getString(Res.string.coupon_title, currentYear)
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

                        cell.setBackgroundImage(
                            BackgroundImage.Builder()
                                .setImage(
                                    PdfImageXObject(
                                        ImageDataFactory.create(
                                            getDrawableResourceBytes(
                                                environment = getSystemResourceEnvironment(),
                                                resource = Res.drawable.background_coupon
                                            )
                                        )
                                    )
                                )
                                .setBackgroundSize(
                                    BackgroundSize().apply {
                                        setBackgroundSizeToCover()
                                    }
                                )
                                .build()
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
        }
    }

//    suspend fun saveCoupons(
//        //TODO: Change into received param only later without default value
//        location: String = "Lokasi Test",
//        time: String = "Jumat, 19 Agustus 2023",
//        isQrEnabled: Boolean = false,
//        qrDataList: List<QRGeneratorData>
//    ) {
//        setupBackgroundCoupon(isQrEnabled)
//        checkOrInitiateByteArrayLogo()
//
//
//        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
//        withContext(Dispatchers.IO) {
//            val documentsDir = Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
//
//
//            val file =
//                File(documentsDir, "kupon-qurban-${currentYear}.pdf")
//            val writer = PdfWriter(file)
//            val pdf = PdfDocument(writer)
//            val document = Document(pdf, PageSize.A4)
//            document.setMargins(0F, 0F, 0F, 0F)
//
//            // Process in smaller batches (8 instead of 16) to reduce memory usage
//            qrDataList.chunked(CHUNK_SIZE).forEach { chunk ->
//
//                val table = Table(floatArrayOf(50f, 50f))
//                    .useAllAvailableWidth()
//
//                table.setMargin(0F)
//                table.setPadding(0F)
//
//                chunk.forEach { qrData ->
//                    writeCoupon(
//                        isQrEnabled = isQrEnabled,
//                        data = CouponData(
//                            couponName = qrData.couponName,
//                            couponQR = qrData.qrCode,
//                            qurbanStatus = qrData.qurbanStatus,
//                            qurbanType = qrData.qurbanType,
//                            qurbanLocation = location,
//                            qurbanDate = time,
//                            qurbanYear = currentYear.toString()
//                        ),
//                        table = table
//                    )
//                }
//
//                document.add(table)
//                if (chunk != qrDataList.chunked(CHUNK_SIZE).last()) {
//                    document.add(AreaBreak(AreaBreakType.NEXT_PAGE))
//                }
//            }
//
//            document.close()
//            pdf.close()
//            writer.close()
//        }
//    }
//
//    fun getQrImage(qrHash: String): Bitmap {
//        checkOrInitiateByteArrayLogo()
//
//        val qrCode = QRCode.ofCircles()
//            .withSize(100)
//            .build(qrHash)
//            .renderToBytes()
//
//        return BitmapFactory.decodeByteArray(
//            qrCode,
//            0,
//            qrCode.size
//        )
//    }
//
//    fun checkOrInitiateByteArrayLogo() {
//        if (!::byteArrayLogo.isInitialized) {
//            val rawLogo = ContextCompat
//                .getDrawable(
//                    context,
//                    R.drawable.logo_qr
//                )
//                ?.toBitmap() ?: error("Icon can't be fetched")
//
//            val baos = ByteArrayOutputStream()
//            rawLogo.compress(Bitmap.CompressFormat.PNG, 80, baos)
//            byteArrayLogo = baos.toByteArray()
//
//            rawLogo.recycle()
//            baos.close()
//        }
//    }
//
//    private fun setupBackgroundCoupon(isQrEnabled: Boolean) {
//        val baos = ByteArrayOutputStream()
//        val drawable =
//            when (isQrEnabled) {
//                true -> ContextCompat.getDrawable(context, R.drawable.tiket_qurban_qr)
//                false -> ContextCompat.getDrawable(context, R.drawable.background_coupon)
//            }
//
//        val bitmap: Bitmap = drawable?.toBitmap() ?: error("Coupon background failed to be fetched")
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
//        couponBackgroundImage = baos.toByteArray()
//
//        baos.close()
//        bitmap.recycle()
//    }
//
//    private fun writeCoupon(
//        isQrEnabled: Boolean,
//        data: CouponData,
//        table: Table
//    ) {
//        with(context) {
//            when (isQrEnabled) {
//                true -> {
////                    NOTE:
////                    For the time being, i can't manage to make the Logo being rendered center on QR
////
////
////                    val logo = ContextCompat.getDrawable(context, R.drawable.icon_qurban)
////                    val bitmap = logo?.toBitmap() ?: error("Coupon Logo failed to be fetched")
////                    val baos = ByteArrayOutputStream()
////                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
////                    val byteArray = baos.toByteArray()
////                    baos.close()
////                    bitmap.recycle()
//
//                    // Generate QR code
//                    val qrCode = QRCode.ofRoundedSquares()
//                        .withSize(20)
//                        .build(data.couponQR.orEmpty())
//                        .render()
//
//                    // Convert to compressed bitmap
//                    val bitmap = BitmapFactory.decodeByteArray(
//                        qrCode.getBytes(),
//                        0,
//                        qrCode.getBytes().size
//                    )
//                    val stream = ByteArrayOutputStream()
//                    bitmap.compress(
//                        Bitmap.CompressFormat.PNG,
//                        80,
//                        stream
//                    ) // Compress to 80% quality
//                    val compressedBytes = stream.toByteArray()
//
//                    // Create image data from compressed bytes
//                    val imageData = ImageDataFactory.create(compressedBytes)
//                    val image = Image(imageData)
//                        .scaleToFit(100F, 100F)
//                        .setMargins(10F, 10F, 5F, 10F)
//
//                    // Clean up
//                    bitmap.recycle()
//                    stream.close()
//
//                    val individualCouponTable = Table(floatArrayOf(30f, 70f))
//
//                    individualCouponTable.addCell(
//                        Cell()
//                            .add(image)
//                            .setBorder(Border.NO_BORDER)
//                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
//                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                    )
//                    individualCouponTable.addCell(
//                        Cell()
//                            .add(
//                                Paragraph()
//                                    .add(
//                                        Text("Kupon Qurban ${data.qurbanYear}\n")
//                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
//                                    )
//                                    .add(
//                                        Text("Nama: ${data.couponName}\n")
//                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
//                                    )
//                                    .add(
//                                        Text("Lokasi: ${data.qurbanLocation}\n")
//                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
//                                    )
//                                    .add(
//                                        Text("Tanggal/Waktu: ${data.qurbanDate}")
//                                            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
//                                    )
//                            )
//                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                            .setHorizontalAlignment(HorizontalAlignment.LEFT)
//                            .setBorder(Border.NO_BORDER)
//                    )
//
//                    individualCouponTable.setBackgroundImage(
//                        BackgroundImage.Builder()
//                            .setImage(PdfImageXObject(ImageDataFactory.create(couponBackgroundImage)))
//                            .setBackgroundSize(
//                                BackgroundSize().apply {
//                                    setBackgroundSizeToCover()
//                                }
//                            )
//                            .build()
//                    )
//
//                    table.addCell(individualCouponTable)
//                }
//
//                false -> {
//                    val cell = Cell()
//                        .setHorizontalAlignment(HorizontalAlignment.LEFT)
//                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
//                        .setPaddingLeft(36F)
//                        .setPaddingTop(12F)
//                        .setPaddingBottom(12F)
//                        .add(
//                            Paragraph()
//                                .add(
//                                    Text(getString(coupon_title, data.qurbanYear))
//                                        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
//                                        .setFontSize(12F)
//                                )
//                                .add(
//                                    Text(
//                                        getString(
//                                            coupon_name_and_information,
//                                            data.couponName,
//                                            when (data.qurbanStatus) {
//                                                QurbanStatus.Recipient -> ""
//                                                QurbanStatus.Participant ->
//                                                    "[${context.getString(data.qurbanType?.uiText ?: QurbanType.Cow.uiText)}]"
//                                            }
//                                        )
//                                    )
//                                        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
//                                        .setFontSize(12F)
//                                )
//                                .add(
//                                    Text(getString(coupon_pickup_location, data.qurbanLocation))
//                                        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
//                                        .setFontSize(12F)
//                                )
//                                .add(
//                                    Text(getString(coupon_pickup_date, data.qurbanDate))
//                                        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
//                                        .setFontSize(12F)
//                                )
//                        )
//
//                    cell.setBackgroundImage(
//                        BackgroundImage.Builder()
//                            .setImage(PdfImageXObject(ImageDataFactory.create(couponBackgroundImage)))
//                            .setBackgroundSize(
//                                BackgroundSize().apply {
//                                    setBackgroundSizeToCover()
//                                }
//                            )
//                            .build()
//                    )
//
//                    table.addCell(cell)
//                }
//            }
//        }
//    }
//
//    private data class CouponData(
//        val couponName: String,
//        val couponQR: String? = null,
//        val qurbanStatus: QurbanStatus,
//        val qurbanType: QurbanType?,
//        val qurbanLocation: String,
//        val qurbanDate: String,
//        val qurbanYear: String,
//    )

    data class QRGeneratorData(
        val qrCode: String,
        val couponStatus: String,
        val couponName: String,
        val qurbanStatus: Status,
        val qurbanType: Type?,
    )

    data class CouponDataV2(
        val name: String,
        val status: Status,
        val type: Type
    )

    companion object {
        private const val CHUNK_SIZE = 16
    }
}
