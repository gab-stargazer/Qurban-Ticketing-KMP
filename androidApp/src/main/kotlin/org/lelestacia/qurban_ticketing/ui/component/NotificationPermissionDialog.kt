package org.lelestacia.qurban_ticketing.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.padding.CustomPadding
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_continue_without_permission
import qurbanticketing.composeapp.generated.resources.btn_grant_permission
import qurbanticketing.composeapp.generated.resources.permission_body_notification
import qurbanticketing.composeapp.generated.resources.permission_title_notification

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NotificationPermissionDialog(
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit,
    onDeny: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text(
                    text = stringResource(Res.string.btn_grant_permission),
                    style = MaterialTheme.typography.titleSmallEmphasized.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDeny
            ) {
                Text(
                    text = stringResource(Res.string.btn_continue_without_permission),
                    style = MaterialTheme.typography.titleSmallEmphasized.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                )
            }
        },
        title = {
            Text(
                text = stringResource(Res.string.permission_title_notification),
                style = MaterialTheme.typography.titleSmallEmphasized.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.permission_body_notification),
                style = MaterialTheme.typography.titleSmallEmphasized.copy(
                    textAlign = TextAlign.Justify
                )
            )
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun PreviewNotificationPermissionDialog() {
    CompositionLocalProvider(
        LocalScreenPadding provides
                CustomPadding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
    ) {
        QurbanTicketingTheme {
            NotificationPermissionDialog(
                onDismiss = {},
                onConfirmation = {},
                onDeny = {},
            )
        }
    }
}