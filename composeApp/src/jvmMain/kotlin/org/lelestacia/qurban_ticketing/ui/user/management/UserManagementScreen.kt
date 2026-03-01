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
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.path
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.DialogPrintCouponEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.UserManagementEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.UserManagementEvent.*
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.UserManagementState
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
    state: UserManagementState,
    onEvent: (UserManagementEvent) -> Unit,
    onNavigateTo: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    val users = state.users.collectAsLazyPagingItems()

    val launcher = rememberFilePickerLauncher(
        type = FileKitType.File("xlsx")
    ) { file ->
        onEvent(ImportDataEvent.OnImportData(stringUri = file?.path ?: return@rememberFilePickerLauncher))
    }

    if (state.isDialogPrintCouponShowed) {
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
            UserManagementFabMenu(
                isFabExpanded = state.isFabMenuExpanded,
                onFabStateChange = { newFabMenuState ->
                    onEvent(OnFabMenuStateClicked(newFabMenuState))
                },
                onImportData = {
                    onEvent(OnFabMenuStateClicked(newFabMenuState = false))
                    launcher.launch()
                },
                onAddData = {
                    onEvent(OnFabMenuStateClicked(newFabMenuState = false))
                    onNavigateTo(UserAddEdit())
                },
                onPrintCoupon = {
                    onEvent(OnPrintCouponClicked)
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