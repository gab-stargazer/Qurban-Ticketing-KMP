package org.lelestacia.qurban_ticketing.ui.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingEvent
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingEvent.OnDismissPermissionDialog
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingEvent.OnExportDataClicked
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingEvent.OnImportData
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingEvent.OnImportDataClicked
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingEvent.OnParticipantImageChanged
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingEvent.OnRecipientImageChanged
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingState
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.ui.component.NotificationPermissionDialog
import org.lelestacia.qurban_ticketing.ui.dropdown.Language
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.handleImagePick
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import org.lelestacia.qurban_ticketing.util.isNotGranted
import org.lelestacia.qurban_ticketing.util.padding.CustomPadding
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_back_setting
import qurbanticketing.composeapp.generated.resources.btn_export_data
import qurbanticketing.composeapp.generated.resources.btn_import_data
import qurbanticketing.composeapp.generated.resources.export_data_body
import qurbanticketing.composeapp.generated.resources.export_data_label
import qurbanticketing.composeapp.generated.resources.import_data_body
import qurbanticketing.composeapp.generated.resources.import_data_label
import qurbanticketing.composeapp.generated.resources.label_preferred_language

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun SettingScreen(
    state: SettingState,
    onEvent: (SettingEvent) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {

    val view = LocalView.current
    DisposableEffect(Unit) {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view)
            .isAppearanceLightStatusBars = true

        onDispose {
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = false
        }
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()
    val excelPicker = rememberFilePickerLauncher(type = FileKitType.File("xlsx")) { file ->
        onEvent(OnImportData(file?.path ?: return@rememberFilePickerLauncher))
    }

    val recipientImagePicker = rememberFilePickerLauncher(type = FileKitType.Image) { file ->
        scope.launch {
            context.handleImagePick(
                file,
                onPicked = { uri, byteArray ->
                    onEvent(OnRecipientImageChanged(uri, byteArray))
                }
            )
        }
    }

    val participantImagePicker = rememberFilePickerLauncher(type = FileKitType.Image) { file ->
        scope.launch {
            file?.let { file ->
                context.handleImagePick(
                    file,
                    onPicked = { uri, byteArray ->
                        onEvent(OnParticipantImageChanged(uri, byteArray))
                    }
                )
            }
        }
    }

    var isLanguageDropDownMenuOpened: Boolean by remember {
        mutableStateOf(false)
    }

    val notificationPermission =
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = { _ ->
                onEvent(OnDismissPermissionDialog)
            }
        )

    //  Dialog
    if (state.isPermissionShownForExport || state.isPermissionShownForImport) {
        NotificationPermissionDialog(
            onDismiss = {
                onEvent(OnDismissPermissionDialog)
            },
            onConfirmation = {
                notificationPermission.launchPermissionRequest()
            },
            onDeny = {
                if (state.isPermissionShownForExport) {
                    onEvent(OnExportDataClicked(hasPermission = true))
                } else {
                    onEvent(OnDismissPermissionDialog)
                    excelPicker.launch()
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(resource = Res.string.btn_back_setting),
                        style = MaterialTheme.typography.titleMediumEmphasized.copy(
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            lifecycle.handleWhenLifecycleResumed(onResumed = onBackPressed)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(),
        modifier = modifier,
    ) { padding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
        ) {
            ExposedDropdownMenuBox(
                expanded = isLanguageDropDownMenuOpened,
                onExpandedChange = { newState ->
                    isLanguageDropDownMenuOpened = newState
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = LocalScreenPadding.current.vertical)
                    .padding(horizontal = LocalScreenPadding.current.horizontal)
            ) {
                TextField(
                    value = stringResource(state.preferredLanguage.title),
                    onValueChange = {},
                    label = {
                        Text(
                            stringResource(Res.string.label_preferred_language),
                            style = MaterialTheme.typography.labelMediumEmphasized
                        )
                    },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(isLanguageDropDownMenuOpened)
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(25F),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = isLanguageDropDownMenuOpened,
                    onDismissRequest = { isLanguageDropDownMenuOpened = false },
                    modifier = Modifier.exposedDropdownSize()
                ) {
                    Language.entries.forEach { language ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(language.title),
                                    style = MaterialTheme.typography.bodyMediumEmphasized.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            },
                            onClick = {
                                onEvent(SettingEvent.OnLanguageChanged(language))
                                isLanguageDropDownMenuOpened = false
                            }
                        )
                    }
                }
            }

            ExportImport(
                title = stringResource(Res.string.export_data_label),
                body = stringResource(Res.string.export_data_body),
                buttonLabel = stringResource(Res.string.btn_export_data),
                onButtonClicked = {
                    lifecycle.handleWhenLifecycleResumed {
                        //  Check for notification only
                        if (Build.VERSION.SDK_INT >= 33 && notificationPermission.status.isGranted) {
                            onEvent(OnExportDataClicked(hasPermission = true))
                        } else if (Build.VERSION.SDK_INT >= 33 && notificationPermission.status.isNotGranted()) {
                            onEvent(OnExportDataClicked(hasPermission = false))
                        } else {
                            onEvent(OnExportDataClicked(hasPermission = true))
                        }
                    }
                },
                modifier = Modifier.padding(top = LocalScreenPadding.current.vertical)
            )

            ExportImport(
                title = stringResource(Res.string.import_data_label),
                body = stringResource(Res.string.import_data_body),
                buttonLabel = stringResource(Res.string.btn_import_data),
                onButtonClicked = {
                    lifecycle.handleWhenLifecycleResumed {
                        //  Check for notification only
                        if (Build.VERSION.SDK_INT >= 33 && notificationPermission.status.isGranted) {
                            excelPicker.launch()
                        } else if (Build.VERSION.SDK_INT >= 33 && notificationPermission.status.isNotGranted()) {
                            onEvent(OnImportDataClicked(hasPermission = false))
                        } else {
                            excelPicker.launch()
                        }
                    }
                },
                modifier = Modifier.padding(top = LocalScreenPadding.current.vertical)
            )

            CustomCouponSection(
                recipientCustomCoupon = state.recipientCustomCoupon,
                participantCustomCoupon = state.participantCustomCoupon,
                onRecipientClicked = {
                    recipientImagePicker.launch()
                },
                onDeleteRecipient = {
                    onEvent(OnRecipientImageChanged("", byteArrayOf()))
                },
                onParticipantClicked = {
                    participantImagePicker.launch()
                },
                onDeleteParticipant = {
                    onEvent(OnParticipantImageChanged("", byteArrayOf()))
                },
                modifier = Modifier.padding(top = LocalScreenPadding.current.vertical)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, locale = "id")
@Composable
private fun PreviewInformationScreen() {
    CompositionLocalProvider(
        LocalScreenPadding provides CustomPadding(
            horizontal = 16.dp,
            vertical = 12.dp
        )
    ) {
        QurbanTicketingTheme {
            SettingScreen(
                state = SettingState(
                    preferredLanguage = Language.ID
                ),
                onEvent = {},
                onBackPressed = {}
            )
        }
    }
}