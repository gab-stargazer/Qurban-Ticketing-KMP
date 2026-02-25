package org.lelestacia.qurban_ticketing.ui.user_management

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.ui.component.CustomTextField
import org.lelestacia.qurban_ticketing.util.FutureSelectableDate
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.toFormattedDate
import qurbanticketing.composeapp.generated.resources.*

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class
)
@Composable
fun DialogPrintCoupon(
    state: DialogPrintCouponState,
    onEvent: (DialogPrintCouponEvent) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {

        LaunchedEffect(state.datePickerState.selectedDateMillis) {
            onEvent(DialogPrintCouponEvent.OnSelectedPickupDateChanged)
        }

        val focusManager = LocalFocusManager.current
        val keyboardManager = LocalSoftwareKeyboardController.current

        Box(
            modifier = Modifier.padding(horizontal = LocalScreenPadding.current.horizontal)
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(25F),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .padding(
                                horizontal = LocalScreenPadding.current.horizontal,
                                vertical = LocalScreenPadding.current.vertical
                            )
                ) {
                    Text(
                        stringResource(Res.string.dialog_print_coupon_title),
                        style = MaterialTheme.typography.titleMediumEmphasized.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(
                            top = LocalScreenPadding.current.vertical
                        )
                    )

                    CustomTextField(
                        value = state.location,
                        onValueChange = { newLocation ->
                            onEvent(DialogPrintCouponEvent.OnLocationChanged(newLocation))
                        },
                        label = {
                            Text(
                                text = stringResource(Res.string.dialog_print_coupon_label_location),
                                style = MaterialTheme.typography.labelMediumEmphasized.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null
                            )
                        },
                        isError = state.locationError != null,
                        supportingText = {
                            AnimatedVisibility(
                                visible = state.locationError != null,
                                enter = expandVertically() + fadeIn()
                            ) {
                                state.locationError?.let { locationError ->
                                    Text(
                                        stringResource(locationError),
                                        style = MaterialTheme.typography.bodySmallEmphasized.copy(
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    )
                                }
                            }
                        },
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
                            .padding(top = LocalScreenPadding.current.vertical)
                    )

                    var shouldShownDatePicker by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CustomTextField(
                            value = state.datePickerState.selectedDateMillis?.toFormattedDate()
                                ?: stringResource(Res.string.dialog_print_coupon_no_pickup_date),
                            onValueChange = {},
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        shouldShownDatePicker = !shouldShownDatePicker
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = stringResource(Res.string.dialog_print_coupon_pickup_date),
                                    style = MaterialTheme.typography.labelMediumEmphasized.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            },
                            readOnly = true,
                            isError = state.datePickerStateError != null,
                            supportingText = {
                                Column {
                                    AnimatedVisibility(
                                        visible = state.datePickerStateError != null,
                                        enter = expandVertically() + fadeIn()
                                    ) {
                                        state.datePickerStateError?.let { datePickerStateError ->
                                            Text(
                                                stringResource(datePickerStateError),
                                                style = MaterialTheme.typography.bodySmallEmphasized.copy(
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            )
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        if (shouldShownDatePicker) {
                            val newDatePickerState = rememberDatePickerState(
                                initialDisplayMode = DisplayMode.Picker,
                                selectableDates = FutureSelectableDate,
                            )

                            DatePickerDialog(
                                onDismissRequest = {
                                    shouldShownDatePicker = false
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            shouldShownDatePicker = false
                                            onEvent(
                                                DialogPrintCouponEvent.OnDatePicked(
                                                    newDatePickerState.selectedDateMillis ?: 0L
                                                )
                                            )
                                        }
                                    ) {
                                        Text(stringResource(Res.string.btn_select))
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            shouldShownDatePicker = false
                                        }
                                    ) {
                                        Text(stringResource(Res.string.btn_cancel))
                                    }
                                }
                            ) {
                                DatePicker(state = newDatePickerState)
                            }
                        }
                    }

                    Button(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(25F),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = LocalScreenPadding.current.vertical)
                    ) {
                        Text(
                            text = stringResource(Res.string.btn_print_coupon),
                            style = MaterialTheme.typography.labelMediumEmphasized.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}