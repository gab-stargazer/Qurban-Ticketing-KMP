package org.lelestacia.qurban_ticketing.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.DialogPrintCouponEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.DialogPrintCouponState
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.util.FutureSelectableDate
import org.lelestacia.qurban_ticketing.util.Hour
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.Minute
import org.lelestacia.qurban_ticketing.util.toFormattedDate
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_cancel
import qurbanticketing.composeapp.generated.resources.btn_print_coupon
import qurbanticketing.composeapp.generated.resources.btn_select
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_label_finish_time
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_label_location
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_label_start_time
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_pickup_date
import qurbanticketing.composeapp.generated.resources.dialog_print_coupon_title
import qurbanticketing.composeapp.generated.resources.label_not_selected


@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun PrintCouponDialog(
    state: DialogPrintCouponState,
    onEvent: (DialogPrintCouponEvent) -> Unit,
    onConfirm: () -> Unit,
) {
    LaunchedEffect(state.datePickerState.selectedDateMillis) {
        onEvent(DialogPrintCouponEvent.OnSelectedPickupDateChanged)
    }

    ElevatedCard(
        shape = RoundedCornerShape(25F),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LocalScreenPadding.current.horizontal)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .padding(
                        horizontal = LocalScreenPadding.current.horizontal
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
                state = state.location,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = LocalScreenPadding.current.vertical)
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                var shouldShownDatePicker by remember { mutableStateOf(false) }

                CustomTextField(
                    value = state.datePickerState.selectedDateMillis?.toFormattedDate()
                        ?: stringResource(Res.string.label_not_selected),
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

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                var shouldShownStartTimePicker by remember { mutableStateOf(false) }

                CustomTextField(
                    value = if (state.startTime.hour > 0) {
                        "${state.startTime.hour}:${
                            if (state.startTime.minute == 0) {
                                "00"
                            } else {
                                state.startTime.minute
                            }
                        }"
                    } else {
                        stringResource(Res.string.label_not_selected)
                    },
                    onValueChange = {},
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                shouldShownStartTimePicker = !shouldShownStartTimePicker
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null
                            )
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(Res.string.dialog_print_coupon_label_start_time),
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

                if (shouldShownStartTimePicker) {
                    val newTimePickerState = rememberTimePickerState(
                        is24Hour = true
                    )

                    TimePickerDialog(
                        onDismissRequest = {
                            shouldShownStartTimePicker = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    shouldShownStartTimePicker = false
                                    onEvent(
                                        DialogPrintCouponEvent.OnStartTimePicked(
                                            hour = Hour(newTimePickerState.hour),
                                            minute = Minute(newTimePickerState.minute)
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
                                    shouldShownStartTimePicker = false
                                }
                            ) {
                                Text(stringResource(Res.string.btn_cancel))
                            }
                        },
                        title = {
                            Text(
                                text = stringResource(resource = Res.string.dialog_print_coupon_label_start_time),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    ) {
                        TimePicker(state = newTimePickerState)
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                var shouldShownFinishTimePicker by remember { mutableStateOf(false) }

                CustomTextField(
                    value = if (state.finishTime.hour > 0) {
                        "${state.finishTime.hour}:${
                            if (state.finishTime.minute == 0) {
                                "00"
                            } else {
                                state.finishTime.minute
                            }
                        }"
                    } else {
                        stringResource(Res.string.label_not_selected)
                    },
                    onValueChange = {},
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                shouldShownFinishTimePicker = !shouldShownFinishTimePicker
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null
                            )
                        }
                    },
                    label = {
                        Text(
                            text = stringResource(Res.string.dialog_print_coupon_label_finish_time),
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

                if (shouldShownFinishTimePicker) {
                    val newTimePickerState = rememberTimePickerState(
                        is24Hour = true
                    )

                    TimePickerDialog(
                        onDismissRequest = {
                            shouldShownFinishTimePicker = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    shouldShownFinishTimePicker = false
                                    onEvent(
                                        DialogPrintCouponEvent.OnFinishTimePicked(
                                            hour = Hour(newTimePickerState.hour),
                                            minute = Minute(newTimePickerState.minute)
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
                                    shouldShownFinishTimePicker = false
                                }
                            ) {
                                Text(stringResource(Res.string.btn_cancel))
                            }
                        },
                        title = {
                            Text(
                                text = stringResource(resource = Res.string.dialog_print_coupon_label_finish_time),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    ) {
                        TimePicker(state = newTimePickerState)
                    }
                }
            }

            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(25F),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LocalScreenPadding.current.vertical)
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


@Preview(locale = "id")
@Composable
private fun PreviewDialogPrintCoupon() {
    QurbanTicketingTheme {
        PrintCouponDialog(
            state = DialogPrintCouponState(),
            onEvent = {},
            onConfirm = {},
        )
    }
}