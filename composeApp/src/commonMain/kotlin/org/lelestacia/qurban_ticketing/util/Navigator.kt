package org.lelestacia.qurban_ticketing.util

import androidx.compose.runtime.mutableStateListOf

class Navigator(
    private val initialRoute: Any
) {

    val backstack = mutableStateListOf<Any>(initialRoute)

    fun onNavigateTo(
        destination: Any,
        singleNavigate: Boolean = false,
    ) {
        backstack.add(destination)
    }

    fun onBackPressed() {
        if (backstack.size == 1) return

        backstack.removeLastOrNull()
    }
}