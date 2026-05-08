package org.lelestacia.qurban_ticketing.data.utility

import dev.zwander.kotlin.file.FileUtils
import dev.zwander.kotlin.file.IPlatformFile
import io.retable.Retable
import kotlinx.io.IOException
import kotlinx.io.asInputStream
import org.lelestacia.qurban_ticketing.data.entity.UserEntity
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Type
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ExcelUtility {

//    fun exportMemberToExcel(member: List<MemberEntity>) {
//        val documentsDir = Environment
//            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
//        val file = File(documentsDir, "daftar-qurban.xlsx")
//        val columns = object : RetableColumns() {
//            val id = string(ID)
//            val name = string(NAME)
//            val address = string(ADDRESS)
//            val phone = string(PHONE)
//            val status = string(STATUS)
//            val type = string(TYPE)
//        }

//        TODO: Require further investigation regarding APACHE POI capabilities not being able to export into Excel
//
//        Retable(columns)
//            .data(
//                values = member
//            ) {
//
//                mapOf(
//                    id to it.id,
//                    name to it.name,
//                    address to it.address,
//                    rt to it.rt,
//                    rw to it.rw,
//                    phone to it.phone.orEmpty(),
//                    status to if (it.isParticipant) "Peserta" else "Penerima",
//                    type to if (it.isParticipant) {
//                        if (it.isCow == true) {
//                            "Sapi"
//                        } else {
//                            "Kambing"
//                        }
//                    } else ""
//                )
//            }
//            .write(Retable.excel(columns) to file.outputStream())
//    }

    @OptIn(ExperimentalUuidApi::class)
    fun importMemberFromExcel(uri: String): List<UserEntity> {

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
                                    else -> Type.Cow
                                }
                        )
                    }.toList()
                )
            }
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
    }
}