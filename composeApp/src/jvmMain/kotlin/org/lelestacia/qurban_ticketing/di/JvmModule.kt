package org.lelestacia.qurban_ticketing.di

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import androidx.room.Room
import androidx.room.RoomDatabase
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.lelestacia.qurban_ticketing.data.db.QurbanDB
import org.lelestacia.qurban_ticketing.domain.viewmodel.MemberListViewModel
import org.lelestacia.qurban_ticketing.util.Navigator
import org.lelestacia.qurban_ticketing.util.route.MemberAdd
import org.lelestacia.qurban_ticketing.util.route.MemberList
import java.io.File
import kotlin.uuid.ExperimentalUuidApi

@OptIn(KoinExperimentalAPI::class, ExperimentalUuidApi::class)
val jvmModule = module {
    single<RoomDatabase.Builder<QurbanDB>> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "qurban.db")
        Room.databaseBuilder<QurbanDB>(
            name = dbFile.absolutePath
        )
    }

    single {
        Navigator(MemberList)
    }

    navigation<MemberList> {
        val vm = koinViewModel<MemberListViewModel>()
        val navigator = koinInject<Navigator>()
        val members = vm.getMember().collectAsLazyPagingItems()

        val lifecycleOwner = LocalLifecycleOwner.current
        val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

        Scaffold(
            contentWindowInsets = WindowInsets(),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        if (lifecycle.isAtLeast(Lifecycle.State.RESUMED)) {
                            navigator.navigateTo(MemberAdd)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Member"
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        )
                ) {
                    Text(
                        text = "Nama",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(2F)
                    )

                    Text(
                        text = "Nomor Telpon",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1F)
                    )

                    Text(
                        text = "Status",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1F)
                    )

                    Text(
                        text = "Jenis",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1F)
                    )
                }

                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = 0.dp,
                        vertical = 8.dp
                    )
                ) {
                    items(
                        count = members.itemCount,
                        key = members.itemKey { it.id },
                        contentType = members.itemContentType()
                    ) { index ->
                        members[index]?.let { member ->
                            Row(
                                modifier = Modifier
                                    .background(
                                        if (index % 2 == 0) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .padding(
                                        horizontal = 12.dp,
                                        vertical = 4.dp
                                    ),
                            ) {
                                Text(
                                    text = member.name,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.weight(2F)
                                )

                                Text(
                                    text = member.phoneNumber.toString(),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.weight(1F)
                                )

                                Text(
                                    text = stringResource(member.status.uiText),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.weight(1F)
                                )

                                Text(
                                    text =
                                        if (member.type != null) {
                                            stringResource(member.type.uiText)
                                        } else
                                            "",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.weight(1F)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    navigation<MemberAdd> {
        Scaffold(
            contentWindowInsets = WindowInsets()
        ) {
            Column {

            }
        }
    }
}