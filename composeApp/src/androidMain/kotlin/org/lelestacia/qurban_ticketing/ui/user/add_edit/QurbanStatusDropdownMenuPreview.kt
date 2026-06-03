package org.lelestacia.qurban_ticketing.ui.user.add_edit

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.padding.CustomPadding

@Preview(showBackground = true, locale = "id")
@Composable
private fun PreviewQurbanStatusDropdownMenu() {
    CompositionLocalProvider(
        LocalScreenPadding provides CustomPadding(
            horizontal = 16.dp,
            vertical = 12.dp
        )
    ) {
        QurbanTicketingTheme {
            Surface {
                QurbanStatusDropdownMenu(
                    status = Status.Recipient,
                    onQurbanStatusChanged = {},
                    focusManager = LocalFocusManager.current,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}