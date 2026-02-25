package org.lelestacia.qurban_ticketing.ui.user.add_edit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.ui.component.CustomTextField
import org.lelestacia.qurban_ticketing.ui.user.add_edit.UserAddEditEvent.OnStatusChanged
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.label_qurban_status
import qurbanticketing.composeapp.generated.resources.qurban_status_participant
import qurbanticketing.composeapp.generated.resources.qurban_status_recipient


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun QurbanStatusDropdownMenu(
    status: Status,
    onQurbanStatusChanged: (UserAddEditEvent) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    val screenPadding = LocalScreenPadding.current
    var isQurbanStatusExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = isQurbanStatusExpanded,
        onExpandedChange = { newState ->
            // STOPSHIP: Investigate whether this is an IDE issue or not
            isQurbanStatusExpanded = newState
        },
        modifier = modifier.padding(horizontal = screenPadding.horizontal)
    ) {
        CustomTextField(
            value =
                stringResource(
                    when (status) {
                        Status.Recipient -> Res.string.qurban_status_recipient
                        Status.Participant -> Res.string.qurban_status_participant
                    }
                ),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = stringResource(Res.string.label_qurban_status),
                    style = MaterialTheme.typography.labelMediumEmphasized.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(isQurbanStatusExpanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = isQurbanStatusExpanded,
            onDismissRequest = {
                isQurbanStatusExpanded = false
            },
        ) {
            Status.entries.forEach { status ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(
                                when (status) {
                                    Status.Recipient -> Res.string.qurban_status_recipient
                                    Status.Participant -> Res.string.qurban_status_participant
                                }
                            ),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    onClick = {
                        onQurbanStatusChanged(OnStatusChanged(newStatus = status))
                        isQurbanStatusExpanded = false
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}