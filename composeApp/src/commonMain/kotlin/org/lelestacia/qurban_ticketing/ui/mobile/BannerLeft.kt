package org.lelestacia.qurban_ticketing.ui.mobile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_back
import qurbanticketing.composeapp.generated.resources.mobile_banner_variant_a
import qurbanticketing.composeapp.generated.resources.title_app_name

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ManagementTicketingBanner(
    title: String,
    isMainMenu: Boolean,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )

                Image(
                    painter = painterResource(Res.drawable.mobile_banner_variant_a),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = title,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.titleSmallEmphasized.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                modifier = Modifier
                    .padding(horizontal = screenPadding.horizontal)
                    .padding(bottom = screenPadding.vertical * 2)
            )
        }

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(
                    start = 4.dp,
                    end = LocalScreenPadding.current.horizontal
                )
        ) {
            when (isMainMenu) {
                true -> {
                    Box(Modifier.weight(1F))
                }

                false -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                lifecycle.handleWhenLifecycleResumed(onBackPressed)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Text(
                            text = stringResource(Res.string.btn_back),
                            style = MaterialTheme.typography.titleMediumEmphasized.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            Text(
                text = stringResource(Res.string.title_app_name),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.titleMediumEmphasized.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                modifier = Modifier.padding(
                    vertical = screenPadding.vertical
                )
            )
        }
    }
}
