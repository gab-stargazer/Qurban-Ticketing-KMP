package org.lelestacia.qurban_ticketing.ui.user_add_edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.domain.model.Status
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.ui.component.CustomTextField
import org.lelestacia.qurban_ticketing.ui.mobile.UserAddEditBanner
import org.lelestacia.qurban_ticketing.ui.user.add_edit.QurbanStatusDropdownMenu
import org.lelestacia.qurban_ticketing.ui.user.add_edit.QurbanTypeDropdownMenu
import org.lelestacia.qurban_ticketing.ui.user.add_edit.UserAddEditEvent
import org.lelestacia.qurban_ticketing.ui.user.add_edit.UserAddEditEvent.*
import org.lelestacia.qurban_ticketing.ui.user.add_edit.UserAddEditState
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.ADD
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.EDIT
import qurbanticketing.composeapp.generated.resources.*

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun UserAddEditScreen(
    state: UserAddEditState,
    onEvent: (UserAddEditEvent) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
    ) {
        UserAddEditBanner(
            screenType = state.screenType,
            onBackPressed = onBackPressed
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(top = screenPadding.vertical)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(Res.string.tv_user_information),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .padding(vertical = screenPadding.vertical)
            )

            CustomTextField(
                value = state.name,
                onValueChange = { newName ->
                    onEvent(OnNameChanged(newName))
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = stringResource(Res.string.label_name),
                        style = MaterialTheme.typography.labelMediumEmphasized.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                isError = state.nameError != null,
                supportingText = {
                    AnimatedVisibility(state.nameError != null) {
                        state.nameError?.let { nameError ->
                            Text(
                                text = stringResource(nameError),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Red
                                )
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenPadding.horizontal)
            )

            val addressPaddingTop by animateDpAsState(
                when (state.nameError != null) {
                    true -> 9.dp
                    false -> 0.dp
                }
            )

            CustomTextField(
                value = state.address,
                onValueChange = { newAddress ->
                    onEvent(OnAddressChanged(newAddress))
                },
                label = {
                    Text(
                        text = stringResource(Res.string.label_address),
                        style = MaterialTheme.typography.labelMediumEmphasized.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = addressPaddingTop)
                    .padding(horizontal = screenPadding.horizontal)
            )

            Text(
                text = stringResource(Res.string.label_qurban_status),
                style = MaterialTheme.typography.titleSmallEmphasized.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .padding(vertical = screenPadding.vertical)
            )

            QurbanStatusDropdownMenu(
                status = state.qurbanStatus,
                onQurbanStatusChanged = onEvent,
                focusManager = focusManager
            )

            AnimatedVisibility(
                state.qurbanStatus == Status.Participant,
                modifier = Modifier.padding(top = 9.dp)
            ) {
                QurbanTypeDropdownMenu(
                    type = state.qurbanType,
                    onQurbanTypeChanged = onEvent,
                    focusManager = focusManager
                )
            }

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
            )

            Button(
                onClick = {
                    onEvent(OnAddEditPressed)
                },
                shape = RoundedCornerShape(25),
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenPadding.horizontal)
                    .padding(
                        top = screenPadding.vertical,
                        bottom = when (state.screenType) {
                            ADD -> screenPadding.vertical
                            EDIT -> 0.dp
                        }
                    )
            ) {
                Text(
                    stringResource(
                        when (state.screenType) {
                            ADD -> Res.string.btn_add_data
                            EDIT -> Res.string.btn_edit_data
                        }
                    ),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            if (state.screenType == EDIT) {
                Button(
                    onClick = {
                        onEvent(OnDeletePressed)
                    },
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(0.85F)
                    ),
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenPadding.horizontal)
                        .padding(
                            top = 3.dp,
                            bottom = screenPadding.vertical
                        )
                ) {
                    Text(
                        stringResource(Res.string.btn_delete_data),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAddEditScreen() {
    QurbanTicketingTheme {
        UserAddEditScreen(
            state = UserAddEditState(
                screenType = ADD,
                name = "Joko",
            ),
            onEvent = {

            },
            onBackPressed = {

            }
        )
    }
}