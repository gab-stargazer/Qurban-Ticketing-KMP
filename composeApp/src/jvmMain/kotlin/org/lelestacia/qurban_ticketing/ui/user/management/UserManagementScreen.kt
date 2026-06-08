package org.lelestacia.qurban_ticketing.ui.user.management

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.DialogPrintCouponEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.FilterEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.OnFabMenuStateClicked
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.OnPrintingDialogShouldBeDisplayed
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.OnPrintingReminderShouldBeDisplayed
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListEvent.OnSearchQueryChanged
import org.lelestacia.qurban_ticketing.domain.viewmodel.user.list.UserListState
import org.lelestacia.qurban_ticketing.ui.component.PrintCouponDialog
import org.lelestacia.qurban_ticketing.ui.component.PrintReminder
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.EDIT
import kotlin.uuid.ExperimentalUuidApi

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class, ExperimentalUuidApi::class
)
@Composable
fun UserManagementScreen(
    state: UserListState,
    onEvent: (UserListEvent) -> Unit,
    onNavigateTo: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    val users = state.users.collectAsLazyPagingItems()

    if (state.isPrintingDialogOpened) {
        Dialog(
            onDismissRequest = {
                onEvent(UserListEvent.OnPrintCouponDialogDismissed)
            }
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

    if (state.isPrintingReminderOpened) {
        Dialog(
            onDismissRequest = {}
        ) {
            PrintReminder(
                onConfirmation = {
                    onEvent(OnPrintingDialogShouldBeDisplayed)
                }
            )
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(),
        floatingActionButton = {
            UserManagementFabMenu(
                isFabExpanded = state.isFabMenuExpanded,
                onFabStateChange = { newFabMenuState ->
                    onEvent(OnFabMenuStateClicked(newFabMenuState))
                },
                onAddData = {
                    onEvent(OnFabMenuStateClicked(newFabMenuState = false))
                    onNavigateTo(UserAddEdit())
                },
                onPrintCoupon = {
                    onEvent(OnPrintingReminderShouldBeDisplayed)
                }
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            UserManagementHeader(
                searchQuery = state.searchQuery,
                onSearchQueryChanged = { newSearchQuery ->
                    onEvent(OnSearchQueryChanged(newSearchQuery))
                },
                isFilterMenuOpened = state.isFilterMenuOpened,
                onFilterMenuClicked = { newState ->
                    onEvent(FilterEvent.OnClick(newState))
                },
                currentFilter = state.filterType,
                onFilterChanged = { newFilterType ->
                    onEvent(FilterEvent.OnValueChanged(newFilterType))
                }
            )

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
                        User(
                            userIndexed = Pair(user, index),
                            onEdit = {
                                onEvent(OnFabMenuStateClicked(false))
                                onNavigateTo(
                                    UserAddEdit(
                                        screenType = EDIT,
                                        initialData = user
                                    )
                                )
                            },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}