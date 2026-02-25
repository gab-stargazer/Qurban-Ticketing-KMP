package org.lelestacia.qurban_ticketing.ui.user_add_edit

import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.theme.lightScheme
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.ADD
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.EDIT
import qurbanticketing.composeapp.generated.resources.*

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UserAddEditHeader(
    screenType: ScreenType,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenPadding = LocalScreenPadding.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = modifier
        ) {
            Image(
                painter = painterResource(Res.drawable.mobile_banner_variant_b),
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                when (screenType) {
                    ADD -> stringResource(Res.string.title_add_data)
                    EDIT -> stringResource(Res.string.title_edit_data)
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                modifier = Modifier.padding(
                    horizontal = screenPadding.horizontal,
                    vertical = 24.dp
                )
            )
        }

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(end = screenPadding.horizontal)
                .padding(top = screenPadding.vertical)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        lifecycle.handleWhenLifecycleResumed(onResumed = onBackPressed)
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
                    style = MaterialTheme.typography.titleSmallEmphasized.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }

            Text(
                stringResource(Res.string.title_app_name),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.titleSmallEmphasized.copy(
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun PreviewAddEditHeader() {
    MaterialExpressiveTheme(
        colorScheme = lightScheme
    ) {
        UserAddEditHeader(
            screenType = ADD,
            onBackPressed = {

            }
        )
    }
}