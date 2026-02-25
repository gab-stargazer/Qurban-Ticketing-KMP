package org.lelestacia.qurban_ticketing.util.route

import kotlinx.serialization.Serializable
import org.lelestacia.qurban_ticketing.domain.model.User

@Serializable
data object UserList

@Serializable
data object Dashboard

@Serializable
data class UserAddEdit(
    val screenType: ScreenType = ScreenType.ADD,
    val initialData: User? = null,
) {

    @Serializable
    enum class ScreenType {
        ADD, EDIT
    }
}

@Serializable
data object MemberAdd