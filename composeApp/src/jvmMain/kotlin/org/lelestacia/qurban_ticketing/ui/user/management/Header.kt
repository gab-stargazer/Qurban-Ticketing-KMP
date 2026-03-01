package org.lelestacia.qurban_ticketing.ui.user.management

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.ui.filter.FilterType
import qurbanticketing.composeapp.generated.resources.*

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun UserManagementHeader(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    isFilterMenuOpened: Boolean,
    onFilterMenuClicked: (Boolean) -> Unit,
    currentFilter: FilterType,
    onFilterChanged: (FilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(
                horizontal = 12.dp,
                vertical = 18.dp
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                label = {
                    Text(
                        stringResource(Res.string.label_search_name),
                        style = MaterialTheme.typography.labelLargeEmphasized.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    backgroundColor = MaterialTheme.colorScheme.surface
                ),
                trailingIcon = {
                    AnimatedVisibility(
                        visible = searchQuery.isNotBlank(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(
                            onClick = {
                                onSearchQueryChanged("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(
                    topEnd = 5F,
                    bottomEnd = 5F,
                    topStart = 15F,
                    bottomStart = 15F
                ),
                modifier = Modifier.weight(3F)
            )

            ExposedDropdownMenuBox(
                expanded = isFilterMenuOpened,
                onExpandedChange = onFilterMenuClicked,
                modifier = Modifier.weight(1F)
            ) {
                TextField(
                    readOnly = true,
                    value = stringResource(currentFilter.uiText),
                    onValueChange = {},
                    label = {
                        Text(
                            stringResource(Res.string.label_filter),
                            style = MaterialTheme.typography.labelLargeEmphasized.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isFilterMenuOpened
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        backgroundColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(
                        topStart = 5F,
                        bottomStart = 5F,
                        topEnd = 15F,
                        bottomEnd = 15F
                    ),
                    modifier = Modifier
                        .weight(1F)
                        .exposedDropdownSize(matchTextFieldWidth = true)
                )

                ExposedDropdownMenu(
                    expanded = isFilterMenuOpened,
                    onDismissRequest = {
                        onFilterMenuClicked(false)
                    }
                ) {
                    FilterType.entries.forEach { filterType ->
                        DropdownMenuItem(
                            onClick = {
                                onFilterChanged(filterType)
                                onFilterMenuClicked(false)
                            }
                        ) {
                            Text(
                                text = stringResource(filterType.uiText),
                                style = MaterialTheme.typography.labelLargeEmphasized.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = stringResource(Res.string.label_name),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.weight(2F)
            )

            Text(
                text = stringResource(Res.string.label_qurban_status),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1F)
            )

            Text(
                text = stringResource(Res.string.label_qurban_type),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1F)
            )

            Spacer(modifier = Modifier.weight(0.5F))
        }
    }
}