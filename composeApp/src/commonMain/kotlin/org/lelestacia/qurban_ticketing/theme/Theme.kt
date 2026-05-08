package org.lelestacia.qurban_ticketing.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QurbanTicketingTheme(
    content: @Composable () -> Unit
) {
    MaterialExpressiveTheme(
        colorScheme = lightScheme,
        content = content
    )
}