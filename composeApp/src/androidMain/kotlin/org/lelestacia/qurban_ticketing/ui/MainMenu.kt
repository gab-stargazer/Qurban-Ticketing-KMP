package org.lelestacia.qurban_ticketing.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.ui.mobile.ManagementTicketingBanner
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import org.lelestacia.qurban_ticketing.util.route.Information
import org.lelestacia.qurban_ticketing.util.route.UserList
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_show_app_information
import qurbanticketing.composeapp.generated.resources.btn_show_user_management
import qurbanticketing.composeapp.generated.resources.tv_main_menu

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainMenu(
    navigateTo: (Any) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ManagementTicketingBanner(
            title = stringResource(Res.string.tv_main_menu),
            isMainMenu = true,
            onBackPressed = {}
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilledIconButton(
                        onClick = {
                            lifecycle.handleWhenLifecycleResumed(
                                onResumed = {
                                    navigateTo(UserList)
                                }
                            )
                        },
                        shape = RoundedCornerShape(25F),
                        modifier = Modifier
                            .aspectRatio(1F)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Text(
                        text = stringResource(Res.string.btn_show_user_management),
                        style = MaterialTheme.typography.labelMediumEmphasized.copy(
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilledIconButton(
                        onClick = {
                            lifecycle.handleWhenLifecycleResumed(
                                onResumed = {
                                    navigateTo(Information)
                                }
                            )
                        },
                        shape = RoundedCornerShape(25F),
                        modifier = Modifier
                            .aspectRatio(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Text(
                        text = stringResource(Res.string.btn_show_app_information),
                        style = MaterialTheme.typography.labelMediumEmphasized.copy(
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}