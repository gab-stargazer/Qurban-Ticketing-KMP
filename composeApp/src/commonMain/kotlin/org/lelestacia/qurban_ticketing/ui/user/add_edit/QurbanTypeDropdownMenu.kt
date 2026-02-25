package org.lelestacia.qurban_ticketing.ui.user.add_edit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.domain.model.Type
import org.lelestacia.qurban_ticketing.ui.component.CustomTextField
import org.lelestacia.qurban_ticketing.ui.user.add_edit.UserAddEditEvent.OnTypeChanged
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import qurbanticketing.composeapp.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QurbanTypeDropdownMenu(
    type: Type,
    onQurbanTypeChanged: (UserAddEditEvent) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    val screenPadding = LocalScreenPadding.current
    var isQurbanTypeExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = isQurbanTypeExpanded,
        onExpandedChange = { newState ->
            // STOPSHIP: Check this issue
            isQurbanTypeExpanded = newState
        },
        modifier = modifier
            .padding(horizontal = screenPadding.horizontal)
    ) {
        CustomTextField(
            value =
                stringResource(
                    when (type) {
                        Type.Cow -> Res.string.qurban_type_cow
                        Type.Goat -> Res.string.qurban_type_goat
                        Type.Sheep -> Res.string.qurban_type_sheep
                    }
                ),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = stringResource(Res.string.label_qurban_type),
                    style = MaterialTheme.typography.labelMediumEmphasized.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(isQurbanTypeExpanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = isQurbanTypeExpanded,
            onDismissRequest = {
                isQurbanTypeExpanded = false
            }
        ) {
            Type.entries.forEach { newType ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(
                                when (newType) {
                                    Type.Cow -> Res.string.qurban_type_cow
                                    Type.Goat -> Res.string.qurban_type_goat
                                    Type.Sheep -> Res.string.qurban_type_sheep
                                }
                            ),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    onClick = {
                        onQurbanTypeChanged(OnTypeChanged(newType))
                        isQurbanTypeExpanded = false
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}