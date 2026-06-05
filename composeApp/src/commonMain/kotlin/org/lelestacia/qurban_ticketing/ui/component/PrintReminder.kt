package org.lelestacia.qurban_ticketing.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_understand
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_printing_reminder
import qurbanticketing.composeapp.generated.resources.title_information

@Composable
fun PrintReminder(
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(25F),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(resource = Res.string.title_information),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(
                    top = LocalScreenPadding.current.vertical
                )
            )

            Text(
                text = stringResource(resource = Res.string.dialog_print_coupon_printing_reminder),
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Justify),
                modifier = Modifier
                    .padding(horizontal = LocalScreenPadding.current.horizontal)
                    .padding(vertical = LocalScreenPadding.current.vertical)
            )

            TextButton(
                onClick = {
                    if (lifecycle.isAtLeast(Lifecycle.State.RESUMED)) {
                        onConfirmation()
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary,
                    contentColor = MaterialTheme.colorScheme.onSurface.copy(0.85F)
                ),
                shape = RoundedCornerShape(bottomStart = 25F, bottomEnd = 25F),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(resource = Res.string.btn_understand),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "id")
@Composable
private fun PreviewPrintReminder() {
    QurbanTicketingTheme {
        Surface {
            PrintReminder(
                onConfirmation = {},
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}