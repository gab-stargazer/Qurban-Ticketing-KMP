package org.lelestacia.qurban_ticketing.data.utility

import dev.zwander.kotlin.file.FileUtils
import dev.zwander.kotlin.file.IPlatformFile
import io.retable.Retable
import kotlinx.io.IOException
import kotlinx.io.asInputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jetbrains.compose.resources.getString
import org.lelestacia.qurban_ticketing.data.entity.UserEntity
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Type
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ExcelUtility(
    private val platformUtility: PlatformUtility
) {

    suspend fun exportUsersToExcel(users: List<UserEntity>) {
        val os = platformUtility.createExcelGetOS("Data Aplikasi") ?: return

        val statusParticipant = getString(Status.Participant.uiText)
        val statusRecipient = getString(Status.Recipient.uiText)


        val groupedUsers = users.groupBy { it.status }.apply {
            forEach { (_, entities) ->
                entities.sortedBy { it.name }
            }
        }

        XSSFWorkbook().use { workbook ->
            val font = workbook.createFont().apply {
                fontName = "Times New Roman"
                fontHeightInPoints = 12
            }

            val cellStyle = workbook.createCellStyle().apply {
                setFont(font)
            }

            val sheet = workbook.createSheet()
            val headers = listOf(NAME, ADDRESS, STATUS, TYPE)
            val headerRow = sheet.createRow(0)
            headers.forEachIndexed { index, string ->
                headerRow.createCell(index).apply {
                    setCellValue(string)
                    this.cellStyle = cellStyle
                }
            }

            val colWidths = IntArray(headers.size) { headers[it].length }

            groupedUsers.flatMap { it.value }.forEachIndexed { index, entity ->
                val row = sheet.createRow(index + 1)
                row.createCell(0).apply {
                    setCellValue(entity.name)
                    this.cellStyle = cellStyle
                }
                row.createCell(1).apply {
                    setCellValue(entity.address.orEmpty())
                    this.cellStyle = cellStyle
                }
                row.createCell(2).apply {
                    setCellValue(
                        when(entity.status) {
                            Status.Recipient -> statusRecipient
                            Status.Participant -> statusParticipant
                        }
                    )
                    this.cellStyle = cellStyle
                }
                row.createCell(3).apply {
                    setCellValue(
                        when(entity.type) {
                            Type.Cow -> "Sapi"
                            Type.Goat -> "Kambing"
                            Type.Sheep -> "Domba"
                            null -> ""
                        }
                    )
                    this.cellStyle = cellStyle
                }
            }

            colWidths.forEachIndexed { index, width ->
                if (index == 0) {
                    sheet.setColumnWidth(index, (width + 30) * 256)
                } else{
                    sheet.setColumnWidth(index, (width + 4) * 256)
                }
            }


            workbook.write(os)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun importUsersFromExcel(uri: String): List<UserEntity> {

        val entities = mutableListOf<UserEntity>()
        val file: IPlatformFile? = FileUtils.fromString(
            input = uri,
            isDirectory = false
        )

        file?.openInputStream().use { fis ->
            val table = Retable
                .excel()
                .read(fis?.asInputStream() ?: return emptyList())

            table.columns.apply {
                entities.addAll(
                    table.records.map { record ->
                        UserEntity(
                            id = Uuid.generateV7().toString(),
                            name = record[NAME] ?: throw IOException(),
                            address = record[ADDRESS]
                                ?.ifEmpty { null },
                            status =
                                when (record[STATUS]) {
                                    PESERTA -> Status.Participant
                                    else -> Status.Recipient
                                },
                            type =
                                when (record[TYPE]) {
                                    COW -> Type.Cow
                                    GOAT -> Type.Goat
                                    SHEEP -> Type.Sheep
                                    else -> null
                                }
                        )
                    }.toList()
                )
            }

            fis.close()
        }

        return entities
    }

    companion object {
        private const val NAME = "Nama"
        private const val ADDRESS = "Alamat"
        private const val TYPE = "Jenis"
        private const val STATUS = "Status"
        private const val COW = "Sapi"
        private const val GOAT = "Kambing"
        private const val SHEEP = "Domba"
        private const val PESERTA = "Peserta"
        private const val PENERIMA = "Penerima"
    }
}