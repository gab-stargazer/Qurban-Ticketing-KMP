package org.lelestacia.qurban_ticketing.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.padding.CustomPadding

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QurbanTicketingTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalScreenPadding provides
                CustomPadding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
    ) {
        MaterialExpressiveTheme(
            colorScheme = lightScheme,
            content = content
        )
    }
}