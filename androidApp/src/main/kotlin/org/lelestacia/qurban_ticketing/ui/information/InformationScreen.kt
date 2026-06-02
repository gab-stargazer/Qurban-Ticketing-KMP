package org.lelestacia.qurban_ticketing.ui.information

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.handleWhenLifecycleResumed
import qurbanticketing.composeapp.generated.resources.Res
import qurbanticketing.composeapp.generated.resources.btn_back
import qurbanticketing.composeapp.generated.resources.logo_equrban
import qurbanticketing.composeapp.generated.resources.title_app_name

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InformationScreen(
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()
    val context = LocalContext.current

    var shouldShowBottomSheet by remember { mutableStateOf(false) }

    BackHandler(shouldShowBottomSheet) {
        shouldShowBottomSheet = false
    }

    Scaffold (
        contentWindowInsets = WindowInsets(),
        modifier = modifier,
    ) { _ ->

        if (shouldShowBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    shouldShowBottomSheet = false
                }
            ) {
                Text(
                    text = "App Qurban adalah aplikasi yang ditujukan untuk mempermudah Panitia Qurban dalam proses pendataan peserta Qurban. App ini juga dilengkapi dengan fitur Pembuatan Tiket yang bisa diproses batch, mudah dan cepat, serta terdapat fitur Manajemen User yang mempermudah dalam proses pengelolaan data Peserta Qurban seperti mengedit, menambahkan data baru atau mengecek status peserta Qurban dengan mudah dan simpel.",
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
                        lifecycle.handleWhenLifecycleResumed(onResumed = goBack)
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

            Image(
                painter = painterResource(Res.drawable.logo_equrban),
                contentDescription = null,
                modifier = Modifier.size(256.dp)
            )

            Text(
                text = stringResource(Res.string.title_app_name),
                style = MaterialTheme.typography.titleLargeEmphasized.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text("v1.0")

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.65F)
                    .padding(top = LocalScreenPadding.current.vertical)
            ) {
                Button(
                    onClick = {
                        lifecycle.handleWhenLifecycleResumed {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://nighturnal.carrd.co/".toUri()
                            )
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Desainer")
                }

                Button(
                    onClick = {
                        lifecycle.handleWhenLifecycleResumed {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://www.facebook.com/lelestacia/".toUri()
                            )
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Programmer")
                }

                Button(
                    onClick = {
                        lifecycle.handleWhenLifecycleResumed {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                "https://www.facebook.com/ariel.syafiqri".toUri()
                            )
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Penulis")
                }

                Button(
                    onClick = {
                        lifecycle.handleWhenLifecycleResumed {
                            shouldShowBottomSheet = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tentang Aplikasi")
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Github")
                }
            }
        }
    }
}