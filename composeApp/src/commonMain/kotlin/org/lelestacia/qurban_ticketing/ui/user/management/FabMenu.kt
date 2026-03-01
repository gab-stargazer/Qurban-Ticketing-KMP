package org.lelestacia.qurban_ticketing.ui.user.management

import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_add_member
import qurbanticketing.composeapp.generated.resources.btn_import_data
import qurbanticketing.composeapp.generated.resources.btn_print_coupon

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UserManagementFabMenu(
    isFabExpanded: Boolean,
    onFabStateChange: (Boolean) -> Unit,
    onImportData: () -> Unit,
    onAddData: () -> Unit,
    onPrintCoupon: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    FloatingActionButtonMenu(
        expanded = isFabExpanded,
        button = {
            ToggleFloatingActionButton(
                checked = isFabExpanded,
                onCheckedChange = onFabStateChange,
            ) {
                AnimatedContent(isFabExpanded) { isExpanded ->
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
                    lifecycle.handleWhenLifecycleResumed(onResumed = onImportData)
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
                    lifecycle.handleWhenLifecycleResumed(onResumed = onAddData)
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
                    lifecycle.handleWhenLifecycleResumed(onResumed = onPrintCoupon)
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
        },
        modifier = modifier
    )
}