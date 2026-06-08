package org.lelestacia.qurban_ticketing.ui.information

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import org.lelestacia.qurban_ticketing.util.padding.CustomPadding
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_back
import qurbanticketing.composeapp.generated.resources.info_description
import qurbanticketing.composeapp.generated.resources.info_designer
import qurbanticketing.composeapp.generated.resources.info_programmer
import qurbanticketing.composeapp.generated.resources.info_writer
import qurbanticketing.composeapp.generated.resources.logo_equrban
import qurbanticketing.composeapp.generated.resources.title_app_name

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InformationScreen(
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

    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()
    val context = LocalContext.current

    var shouldShowBottomSheet by remember { mutableStateOf(false) }

    BackHandler(shouldShowBottomSheet) {
        shouldShowBottomSheet = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {
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
                            text = stringResource(resource = Res.string.btn_back),
                            style = MaterialTheme.typography.titleMediumEmphasized.copy(
                                fontWeight = FontWeight.SemiBold,
                            )
                        )
                    }
                }

            )
        },
        contentWindowInsets = WindowInsets(),
        modifier = modifier,
    ) { padding ->

        if (shouldShowBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    shouldShowBottomSheet = false
                }
            ) {
                Text(
                    text = stringResource(Res.string.info_description),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Justify
                    ),
                    modifier = Modifier.padding(
                        horizontal = LocalScreenPadding.current.horizontal,
                        vertical = LocalScreenPadding.current.vertical * 2
                    )
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = LocalScreenPadding.current.horizontal)
                .padding(top = LocalScreenPadding.current.vertical)
        ) {
            Image(
                painter = painterResource(Res.drawable.logo_equrban),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )

            Text(
                text = stringResource(Res.string.title_app_name),
                style = MaterialTheme.typography.titleSmallEmphasized.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text("v1.0")

            ElevatedCard(
                modifier = Modifier.padding(top = LocalScreenPadding.current.vertical * 2)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = LocalScreenPadding.current.horizontal,
                            vertical = LocalScreenPadding.current.vertical
                        )
                ) {
                    AsyncImage(
                        model = "https://nighturnal.carrd.co/assets/images/image01.png?v=b2782633",
                        null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(75.dp)
                            .height(75.dp)
                    )

                    Column(
                        modifier = Modifier.padding(start = LocalScreenPadding.current.horizontal)
                    ) {
                        Text(
                            text = stringResource(Res.string.info_designer),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text("Nighturnal", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.weight(1F))

                    IconButton(
                        onClick = {
                            lifecycle.handleWhenLifecycleResumed {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    "https://nighturnal.carrd.co/".toUri()
                                )
                                context.startActivity(intent)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null
                        )
                    }
                }


            }

            ElevatedCard(
                modifier = Modifier.padding(top = LocalScreenPadding.current.vertical)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = LocalScreenPadding.current.horizontal,
                            vertical = LocalScreenPadding.current.vertical
                        )
                ) {
                    AsyncImage(
                        model = "https://scontent.fplm7-1.fna.fbcdn.net/v/t39.30808-6/676901983_122106858602875201_4778387875524553706_n.jpg?stp=dst-jpg_tt6&cstp=mx717x702&ctp=s717x702&_nc_cat=109&ccb=1-7&_nc_sid=6ee11a&_nc_eui2=AeHXafsJdgHxfAKOi_x226ky0-8CCLwH0DzT7wIIvAfQPES5-LjlT5lfJt8ALhyjO1nGSVutbEDy0FMJUSFnOHNB&_nc_ohc=f9VW7gPwldgQ7kNvwHse1R2&_nc_oc=AdpjVrTKBWvV_ah1YoU75gUSeZNsx1Clfd2IcJyOnztiaJCUWozRAO7VWa0_XiXv8lw&_nc_zt=23&_nc_ht=scontent.fplm7-1.fna&_nc_gid=-SNZMlLahxbcU3P1mYIVgw&_nc_ss=7b2a8&oh=00_Af889dyCafc3woQh92NTnVpUy8cbpvK5cmLD1SFLMA0nbA&oe=6A2B43A7",
                        null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(75.dp)
                            .height(75.dp)
                            .clip(CircleShape)
                    )

                    Column(
                        modifier = Modifier.padding(start = LocalScreenPadding.current.horizontal)
                    ) {
                        Text(
                            text = stringResource(Res.string.info_programmer),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text("Gabbu", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.weight(1F))

                    IconButton(
                        onClick = {
                            lifecycle.handleWhenLifecycleResumed {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    "https://www.facebook.com/lelestacia/".toUri()
                                )
                                context.startActivity(intent)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null
                        )
                    }
                }
            }

            ElevatedCard(
                modifier = Modifier.padding(top = LocalScreenPadding.current.vertical)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = LocalScreenPadding.current.horizontal,
                            vertical = LocalScreenPadding.current.vertical
                        )
                ) {
                    AsyncImage(
                        model = "https://avatars.githubusercontent.com/u/88129448?v=4",
                        null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .width(75.dp)
                            .height(75.dp)
                            .clip(CircleShape)
                    )

                    Column(
                        modifier = Modifier.padding(start = LocalScreenPadding.current.horizontal)
                    ) {
                        Text(
                            text = stringResource(Res.string.info_writer),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text("Shinka", style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.weight(1F))

                    IconButton(
                        onClick = {
                            lifecycle.handleWhenLifecycleResumed {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    "https://www.facebook.com/ariel.syafiqri".toUri()
                                )
                                context.startActivity(intent)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = null
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(LocalScreenPadding.current.horizontal / 2),
                modifier = Modifier
                    .padding(top = LocalScreenPadding.current.vertical)
            ) {
                Button(
                    onClick = {
                        lifecycle.handleWhenLifecycleResumed {
                            shouldShowBottomSheet = true
                        }
                    },
                    shape = RoundedCornerShape(
                        bottomStart = 25F,
                        topStart = 25F,
                        bottomEnd = 10F,
                        topEnd = 10F
                    ),
                    modifier = Modifier.weight(1F)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null
                    )
                }

                Button(
                    onClick = {
                        lifecycle.handleWhenLifecycleResumed {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://github.com/gab-stargazer/Qurban-Ticketing-KMP".toUri()
                            )
                            context.startActivity(intent)
                        }
                    },
                    shape = RoundedCornerShape(
                        bottomEnd = 25F,
                        topEnd = 25F,
                        bottomStart = 10F,
                        topStart = 10F
                    ),
                    modifier = Modifier.weight(1F)
                ) {
                    Text("Repositori")
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
            InformationScreen(
                onBackPressed = {}
            )
        }
    }
}