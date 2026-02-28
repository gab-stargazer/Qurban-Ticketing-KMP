package org.lelestacia.qurban_ticketing.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.path
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.domain.model.Type
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.DialogPrintCouponEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.UserManagementEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.UserManagementEvent.*
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.UserManagementState
import org.lelestacia.qurban_ticketing.ui.filter.FilterType
import org.lelestacia.qurban_ticketing.ui.user.management.DialogPrintCoupon
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.EDIT
import qurbanticketing.composeapp.generated.resources.*
import kotlin.uuid.ExperimentalUuidApi

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class, ExperimentalUuidApi::class
)
@Composable
fun UserManagementScreen(
    state: UserManagementState,
    onEvent: (UserManagementEvent) -> Unit,
    onNavigateTo: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    val users = state.users.collectAsLazyPagingItems()

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    val launcher = rememberFilePickerLauncher(
        type = FileKitType.File("xlsx")
    ) { file ->
        onEvent(ImportDataEvent.OnImportData(stringUri = file?.path ?: return@rememberFilePickerLauncher))
    }

    if(state.isDialogPrintCouponShowed) {
        DialogPrintCoupon(
            state = state.dialogPrintCouponState,
            onEvent = onEvent,
            onConfirm = {
                onEvent(DialogPrintCouponEvent.OnPrintCouponConfirmedWithPermission)
            },
            onDismiss = {
                onEvent(OnPrintCouponDialogDismissed)
            }
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(),
        floatingActionButton = {
            FloatingActionButtonMenu(
                expanded = state.isFabMenuExpanded,
                button = {
                    ToggleFloatingActionButton(
                        checked = state.isFabMenuExpanded,
                        onCheckedChange = { isFabMenuExpanded ->
                            onEvent(OnFabMenuStateClicked(isFabMenuExpanded))
                        },
                    ) {
                        AnimatedContent(state.isFabMenuExpanded) { isExpanded ->
                            when (isExpanded) {
                                true -> {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                false -> {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }
                },
                content = {
                    FloatingActionButtonMenuItem(
                        onClick = {
                            lifecycle.handleWhenLifecycleResumed(onResumed = launcher::launch)
                        },
                        icon = {},
                        text = {
                            Text(
                                text = stringResource(Res.string.btn_import_data),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    FloatingActionButtonMenuItem(
                        onClick = {
                            lifecycle.handleWhenLifecycleResumed {
                                onEvent(OnFabMenuStateClicked(newFabMenuState = false))
                                onNavigateTo(UserAddEdit())
                            }
                        },
                        icon = {},
                        text = {
                            Text(
                                text = stringResource(Res.string.btn_add_member),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    FloatingActionButtonMenuItem(
                        onClick = {
                            lifecycle.handleWhenLifecycleResumed {
                                onEvent(OnPrintCouponClicked)
                            }
                        },
                        icon = {},
                        text = {
                            Text(
                                text = stringResource(Res.string.btn_print_coupon),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
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
                        value = state.searchQuery,
                        onValueChange = { newSearchQuery ->
                            onEvent(OnSearchQueryChanged(newSearchQuery))
                        },
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
                                visible = state.searchQuery.isNotBlank(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(
                                    onClick = {
                                        onEvent(OnSearchQueryChanged(newSearchQuery = ""))
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
                        expanded = state.isFabMenuExpanded,
                        onExpandedChange = { newState ->
                            onEvent(FilterEvent.OnClick(newState))
                        },
                        modifier = Modifier.weight(1F)
                    ) {
                        TextField(
                            readOnly = true,
                            value = stringResource(state.filterType.uiText),
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
                                    expanded = state.isFilterMenuOpened
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
                            expanded = state.isFilterMenuOpened,
                            onDismissRequest = {
                                onEvent(FilterEvent.OnClick(newState = false))
                            }
                        ) {
                            FilterType.entries.forEach { filterType ->
                                DropdownMenuItem(
                                    onClick = {
                                        onEvent(FilterEvent.OnValueChanged(filterType))
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

            LazyColumn(
                contentPadding = PaddingValues(
                    bottom = 12.dp
                )
            ) {
                items(
                    count = users.itemCount,
                    key = users.itemKey { user -> user.id },
                    contentType = users.itemContentType()
                ) { index ->
                    users[index]?.let { user ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    if (index % 2 == 0) {
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    } else MaterialTheme.colorScheme.surface
                                )
                                .padding(
                                    horizontal = 20.dp,
                                    vertical = 6.dp
                                )
                                .animateItem()
                        ) {
                            Text(
                                text = user.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(2F)
                            )

                            Text(
                                text = stringResource(user.status.uiText),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1F)
                            )

                            Text(
                                text =
                                    if (user.status == Status.Participant) {
                                        stringResource(user.type?.uiText ?: Type.Cow.uiText)
                                    } else
                                        "-",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1F)
                            )

                            IconButton(
                                onClick = {
                                    if (lifecycle.isAtLeast(Lifecycle.State.RESUMED)) {
                                        onEvent(OnFabMenuStateClicked(false))
                                        onNavigateTo(
                                            UserAddEdit(
                                                screenType = EDIT,
                                                initialData = user
                                            )
                                        )
                                    }
                                },
                                modifier = Modifier.weight(0.5F)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}