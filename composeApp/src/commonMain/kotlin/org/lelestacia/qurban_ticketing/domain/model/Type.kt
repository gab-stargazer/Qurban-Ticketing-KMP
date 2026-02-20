package org.lelestacia.qurban_ticketing.domain.model

import org.jetbrains.compose.resources.StringResource
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.qurban_type_cow
import qurbanticketing.composeapp.generated.resources.qurban_type_goat
import qurbanticketing.composeapp.generated.resources.qurban_type_sheep

enum class Type(
    val uiText: StringResource
) {
    Cow(Res.string.qurban_type_cow),
    Goat(Res.string.qurban_type_goat),
    Sheep(Res.string.qurban_type_sheep)
}