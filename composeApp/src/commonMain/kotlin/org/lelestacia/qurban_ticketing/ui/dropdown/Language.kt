package org.lelestacia.qurban_ticketing.ui.dropdown

import org.jetbrains.compose.resources.StringResource
import org.lelestacia.qurban_ticketing.util.LanguageCode
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.lang_en
import qurbanticketing.composeapp.generated.resources.lang_id

enum class Language(
    val code: LanguageCode,
    val title: StringResource
) {
    ID(LanguageCode("id"), Res.string.lang_id),
    EN(LanguageCode("en"), Res.string.lang_en)
}