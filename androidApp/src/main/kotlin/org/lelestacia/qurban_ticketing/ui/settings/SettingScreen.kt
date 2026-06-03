package org.lelestacia.qurban_ticketing.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingEvent
import org.lelestacia.qurban_ticketing.domain.state_event.setting.SettingState
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.ui.dropdown.Language
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import org.lelestacia.qurban_ticketing.util.padding.CustomPadding
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_back_setting

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun SettingScreen(
    state: SettingState,
    onEvent: (SettingEvent) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    var isLanguageDropDownMenuOpened: Boolean by remember {
        mutableStateOf(false)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(),
        modifier = modifier,
    ) { _ ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 32.dp, start = 4.dp)
            ) {
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

                Text(
                    text = stringResource(resource = Res.string.btn_back_setting),
                    style = MaterialTheme.typography.titleMediumEmphasized.copy(
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }

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
                            "Bahasa Pilihan",
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