package org.lelestacia.qurban_ticketing.ui.user_management

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.theme.lightScheme
import org.lelestacia.qurban_ticketing.ui.component.CustomTextField
import org.lelestacia.qurban_ticketing.ui.component.NotificationPermissionDialog
import org.lelestacia.qurban_ticketing.ui.filter.FilterType
import org.lelestacia.qurban_ticketing.ui.mobile.ManagementTicketingBanner
import org.lelestacia.qurban_ticketing.ui.user_management.UserManagementEvent.*
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import org.lelestacia.qurban_ticketing.util.isNotGranted
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.EDIT
import qurbanticketing.composeapp.generated.resources.*

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalPermissionsApi::class,
)
@Composable
fun UserManagementScreen(
    state: UserManagementState,
    onEvent: (UserManagementEvent) -> Unit,
    onNavigateTo: (Any) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val screenPadding = LocalScreenPadding.current
    val keyboardManager = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()
    val users = state.users.collectAsLazyPagingItems()
    val excelSelectionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) {
        it?.let { uri ->
            onEvent(OnImportData(uri))
        }
    }

    LaunchedEffect(state.shouldLaunchExcelLauncher) {
        if (state.shouldLaunchExcelLauncher) excelSelectionLauncher.launch(arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
    }

    //  Permission
    val notificationPermission =
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = { _ ->
                onEvent(OnDialogPermissionGranted)
            }
        )

    val writeStoragePermission =
        rememberPermissionState(
            permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    //  Dialog
    if (state.isNotificationDialogForImportDataOpened || state.isNotificationDialogForPrintCouponOpened) {
        NotificationPermissionDialog(
            onDismiss = {
                onEvent(OnDialogPermissionDismissed)
            },
            onConfirmation = {
                onEvent(OnDialogPermissionDismissed)
                notificationPermission.launchPermissionRequest()
            },
            onDeny = {
                onEvent(OnDialogPermissionDismissed)
                excelSelectionLauncher.launch(arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            }
        )
    }

    if (state.isDialogPrintCouponShowed) {
        DialogPrintCoupon(
            state = state.dialogPrintCouponState,
            onEvent = onEvent,
            onConfirm = {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (notificationPermission.status.isGranted) {
                        onEvent(DialogPrintCouponEvent.OnPrintCouponConfirmedWithPermission)
                    } else {
                        //  It just asks if the user want to grant notification permission or not
                        onEvent(DialogPrintCouponEvent.OnPrintCouponConfirmedWithoutPermission)
                    }
                } else {
                    onEvent(DialogPrintCouponEvent.OnPrintCouponConfirmedWithPermission)
                }
            },
            onDismiss = {
                onEvent(OnPrintCouponDialogDismissed)
            }
        )
    }

    //  Content
    Scaffold(
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
                }
            ) {
                FloatingActionButtonMenuItem(
                    onClick = {
                        lifecycle.handleWhenLifecycleResumed {
                            //  Check for notification only
                            if (Build.VERSION.SDK_INT >= 33 && notificationPermission.status.isGranted) {
                                excelSelectionLauncher.launch(arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                            } else if (Build.VERSION.SDK_INT >= 33 && notificationPermission.status.isNotGranted()) {
                                onEvent(OnImportDataClicked)
                            } else {
                                excelSelectionLauncher.launch(arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                            }
                        }
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
                            if (Build.VERSION.SDK_INT <= 29 && writeStoragePermission.status.isNotGranted()) {
                                writeStoragePermission.launchPermissionRequest()
                                return@handleWhenLifecycleResumed
                            }

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
        },
        contentWindowInsets = WindowInsets(),
        modifier = modifier
    ) { innerPadding ->

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(innerPadding)
        ) {

            ManagementTicketingBanner(
                title = stringResource(Res.string.tv_management_banner_title),
                isMainMenu = false,
                onBackPressed = {
                    lifecycle.handleWhenLifecycleResumed(onBackPressed)
                }
            )

            CustomTextField(
                value = state.searchQuery,
                onValueChange = { newQuery ->
                    onEvent(OnSearchQueryChanged(newQuery))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onEvent(OnFilterMenuClicked(true))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = null
                        )
                    }

                    DropdownMenu(
                        expanded = state.isFilterMenuOpened,
                        onDismissRequest = {
                            onEvent(OnFilterMenuClicked(false))
                        }
                    ) {
                        FilterType.entries.forEach { filterType ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(filterType.uiText),
                                        style = MaterialTheme.typography.bodySmallEmphasized.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                },
                                onClick = {
                                    onEvent(OnFilterTypeChanged(filterType))
                                    onEvent(OnFilterMenuClicked(false))
                                }
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = stringResource(Res.string.label_search_name),
                        style = MaterialTheme.typography.labelMediumEmphasized.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardManager?.hide()
                        focusManager.clearFocus(true)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = screenPadding.horizontal,
                        vertical = 9.dp
                    )
            )

            AnimatedContent(users.loadState.refresh is LoadState.Loading) { isLoading ->
                when (isLoading) {
                    true -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LoadingIndicator()
                        }
                    }

                    false -> {
                        AnimatedContent(users.loadState.refresh is LoadState.NotLoading && users.itemSnapshotList.isEmpty()) { isDataEmpty ->
                            when (isDataEmpty) {
                                true -> Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = stringResource(Res.string.tv_no_participant_data),
                                        style = MaterialTheme.typography.titleSmallEmphasized.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }

                                false -> {
                                    LazyColumn(
                                        contentPadding = PaddingValues(
                                            start = screenPadding.horizontal,
                                            end = screenPadding.horizontal,
                                            bottom = screenPadding.vertical
                                        ),
                                        verticalArrangement = Arrangement.spacedBy(9.dp)
                                    ) {
                                        items(count = users.itemCount) { index ->
                                            users[index]?.let { user ->
                                                val isSelected = index == state.selectedUserIndex
                                                UserItem(
                                                    userData = UserData(
                                                        user = user,
                                                        isSelected = isSelected
                                                    ),
                                                    onInteraction = { interaction ->
                                                        when (interaction) {
                                                            UserItemInteraction.OnClick -> {
                                                                onEvent(OnUserClicked(index))
                                                                keyboardManager?.hide()
                                                                focusManager.clearFocus()
                                                            }

                                                            UserItemInteraction.OnEdit -> {
                                                                lifecycle.handleWhenLifecycleResumed {
                                                                    onEvent(OnFabMenuStateClicked(newFabMenuState = false))
                                                                    onNavigateTo(
                                                                        UserAddEdit(
                                                                            screenType = EDIT,
                                                                            initialData = user
                                                                        )
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .animateItem()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
private fun PreviewUserManagementScreen() {
    MaterialExpressiveTheme(
        colorScheme = lightScheme
    ) {
        UserManagementScreen(
            state = UserManagementState(),
            onEvent = {},
            onNavigateTo = {

            },
            onBackPressed = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}