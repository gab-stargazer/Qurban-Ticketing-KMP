package org.lelestacia.qurban_ticketing.ui.user_management

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.DialogPrintCouponEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.DialogPermissionEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.FilterEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.OnFabMenuStateClicked
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.OnPrintCouponClicked
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.OnPrintCouponDialogDismissed
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.OnPrintingDialogShouldBeDisplayed
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.OnSearchQueryChanged
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.OnUserClicked
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListState
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.ui.component.CustomTextField
import org.lelestacia.qurban_ticketing.ui.component.NotificationPermissionDialog
import org.lelestacia.qurban_ticketing.ui.component.PrintCouponDialog
import org.lelestacia.qurban_ticketing.ui.component.PrintReminder
import org.lelestacia.qurban_ticketing.ui.dropdown.FilterType
import org.lelestacia.qurban_ticketing.ui.mobile.ManagementTicketingBanner
import org.lelestacia.qurban_ticketing.ui.user.management.UserManagementFabMenu
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import org.lelestacia.qurban_ticketing.util.isNotGranted
import org.lelestacia.qurban_ticketing.util.padding.CustomPadding
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.EDIT
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.label_search_name
import qurbanticketing.composeapp.generated.resources.tv_management_banner_title
import qurbanticketing.composeapp.generated.resources.tv_no_participant_data

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalPermissionsApi::class,
)
@Composable
fun UserManagementScreen(
    state: UserListState,
    onEvent: (UserListEvent) -> Unit,
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

    //  Permission
    val notificationPermission =
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = { _ ->
                onEvent(DialogPermissionEvent.OnGrantPermission)
            }
        )

    //  Dialog
    if (state.isNotificationDialogForPrintCouponOpened) {
        NotificationPermissionDialog(
            onDismiss = {
                onEvent(DialogPermissionEvent.OnDismiss)
            },
            onConfirmation = {
                onEvent(DialogPermissionEvent.OnDismiss)
                notificationPermission.launchPermissionRequest()
            },
            onDeny = {
                onEvent(DialogPermissionEvent.OnContinueWithoutPermission)
            }
        )
    }

    if (state.isPrintingReminderOpened) {
        Dialog(
            onDismissRequest = {

            },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            PrintReminder(
                onConfirmation = {
                    onEvent(OnPrintingDialogShouldBeDisplayed)
                }
            )
        }
    }

    if (state.isPrintingDialogOpened) {
        Dialog(
            onDismissRequest = {
                onEvent(OnPrintCouponDialogDismissed)
            },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            PrintCouponDialog(
                state = state.dialogPrintCouponState,
                onEvent = onEvent,
                onConfirm = {
                    onEvent(DialogPrintCouponEvent.OnPrintCouponConfirmed)
                }
            )
        }
    }

    //  Content
    Scaffold(
        floatingActionButton = {
            UserManagementFabMenu(
                isFabExpanded = state.isFabMenuExpanded,
                onFabStateChange =  { newState ->
                    onEvent(OnFabMenuStateClicked(newState))
                },
                onAddData = {
                    lifecycle.handleWhenLifecycleResumed {
                        onEvent(OnFabMenuStateClicked(newFabMenuState = false))
                        onNavigateTo(UserAddEdit())
                    }
                },
                onPrintCoupon = {
                    lifecycle.handleWhenLifecycleResumed {
                        //  Check for notification only
                        if (Build.VERSION.SDK_INT >= 33 && notificationPermission.status.isGranted) {
                            onEvent(OnPrintCouponClicked(false))
                        } else if (Build.VERSION.SDK_INT >= 33 && notificationPermission.status.isNotGranted()) {
                            onEvent(OnPrintCouponClicked(true))
                        } else {
                            onEvent(OnPrintCouponClicked(false))
                        }
                    }
                }
            )
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
                            onEvent(FilterEvent.OnClick(newState = true))
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
                            onEvent(FilterEvent.OnClick(newState = false))
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
                                    onEvent(FilterEvent.OnValueChanged(newFilterType = filterType))
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
                                                                when (isSelected) {
                                                                    true -> onEvent(
                                                                        OnUserClicked(
                                                                            null
                                                                        )
                                                                    )

                                                                    false -> onEvent(
                                                                        OnUserClicked(
                                                                            index
                                                                        )
                                                                    )
                                                                }
                                                                keyboardManager?.hide()
                                                                focusManager.clearFocus()
                                                            }

                                                            UserItemInteraction.OnEdit -> {
                                                                lifecycle.handleWhenLifecycleResumed {
                                                                    onEvent(
                                                                        OnFabMenuStateClicked(
                                                                            newFabMenuState = false
                                                                        )
                                                                    )
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
    CompositionLocalProvider(
        LocalScreenPadding provides CustomPadding(
            horizontal = 16.dp,
            vertical = 12.dp
        )
    ) {
        QurbanTicketingTheme {
            UserManagementScreen(
                state = UserListState(),
                onEvent = {},
                onNavigateTo = {

                },
                onBackPressed = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}