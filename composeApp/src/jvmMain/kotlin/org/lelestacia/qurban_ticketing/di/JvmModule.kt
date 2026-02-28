package org.lelestacia.qurban_ticketing.di

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import androidx.room.RoomDatabase
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.binds
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.named
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.lelestacia.qurban_ticketing.Constant.IMPORT_DATA_SCHEDULER
import org.lelestacia.qurban_ticketing.Constant.PRINT_COUPON_SCHEDULER
import org.lelestacia.qurban_ticketing.data.db.QurbanDB
import org.lelestacia.qurban_ticketing.domain.ImportDataScheduler
import org.lelestacia.qurban_ticketing.domain.PrintCouponScheduler
import org.lelestacia.qurban_ticketing.domain.background_scheduler.BackgroundScheduler
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.add.MemberAddEditEvent
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.add.MemberAddEditViewModel
import org.lelestacia.qurban_ticketing.domain.viewmodel.member.list.UserManagementViewModel
import org.lelestacia.qurban_ticketing.ui.UserManagementScreen
import org.lelestacia.qurban_ticketing.util.Navigator
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.ADD
import org.lelestacia.qurban_ticketing.util.route.UserAddEdit.ScreenType.EDIT
import org.lelestacia.qurban_ticketing.util.route.UserList
import qurbanticketing.composeapp.generated.resources.*
import java.io.File
import kotlin.uuid.ExperimentalUuidApi

@OptIn(
    KoinExperimentalAPI::class, ExperimentalUuidApi::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class
)
val jvmModule = module {
    single<RoomDatabase.Builder<QurbanDB>> {
        val localAppData = System.getenv("LOCALAPPDATA")
        val appDirectory = File(localAppData, "Qurban Ticketing").apply {
            if (!exists()) mkdirs()
        }

        val dbFile = File(appDirectory, "qurban.db")
        Room.databaseBuilder<QurbanDB>(
            name = dbFile.absolutePath
        )
    }

    single {
        Navigator(UserList)
    }

    factoryOf(::ImportDataScheduler) {
        named(IMPORT_DATA_SCHEDULER)
        binds(listOf(BackgroundScheduler::class))
    }

    factoryOf(::PrintCouponScheduler) {
        named(PRINT_COUPON_SCHEDULER)
        binds(listOf(BackgroundScheduler::class))
    }

    navigation<UserList> {
        val vm = koinViewModel<UserManagementViewModel>()
        val navigator = koinInject<Navigator>()
        val state by vm.state.collectAsStateWithLifecycle()
        UserManagementScreen(
            state = state,
            onEvent = vm::onEvent,
            onNavigateTo = navigator::navigateTo
        )
    }

    navigation<UserAddEdit> { nav ->
        val vm = koinViewModel<MemberAddEditViewModel>(
            parameters = {
                parametersOf(
                    nav.screenType,
                    nav.initialData
                )
            }
        )

        val state by vm.state.collectAsStateWithLifecycle()

        Scaffold(
            contentWindowInsets = WindowInsets(),
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .padding(
                        horizontal = 24.dp,
                        vertical = 12.dp
                    )
            ) {
                Text(
                    text = when (state.screenType) {
                        ADD -> stringResource(Res.string.title_add_data)
                        EDIT -> stringResource(Res.string.title_edit_data)
                    },
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                OutlinedTextField(
                    value = state.name,
                    onValueChange = {
                        vm.onEvent(MemberAddEditEvent.OnNameChanged(it))
                    },
                    shape = RoundedCornerShape(
                        topStart = 10F,
                        topEnd = 15F,
                        bottomStart = 10F,
                        bottomEnd = 15F
                    ),
                    label = {
                        Text(
                            text = stringResource(Res.string.label_name),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(0.5F)
                        )
                    },
                    modifier = Modifier.widthIn(min = 360.dp)
                )

                OutlinedTextField(
                    value = state.address,
                    onValueChange = {
                        vm.onEvent(MemberAddEditEvent.OnAddressChanged(it))
                    },
                    shape = RoundedCornerShape(
                        topStart = 10F,
                        topEnd = 15F,
                        bottomStart = 10F,
                        bottomEnd = 15F
                    ),
                    label = {
                        Text(
                            text = stringResource(Res.string.label_address),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(0.5F)
                        )
                    },
                    modifier = Modifier
                        .widthIn(min = 360.dp)
                        .padding(top = 6.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Button(
                        onClick = {
                            vm.onEvent(MemberAddEditEvent.OnAddEditPressed)
                        },
                        shape =
                            when (state.screenType) {
                                ADD -> {
                                    RoundedCornerShape(15F)
                                }

                                EDIT -> {
                                    RoundedCornerShape(
                                        topStart = 20F,
                                        bottomStart = 20F,
                                        topEnd = 5F,
                                        bottomEnd = 5F
                                    )
                                }
                            },
                        modifier = Modifier
                            .weight(1F)
                    ) {
                        Text(
                            stringResource(
                                when (state.screenType) {
                                    ADD -> Res.string.btn_add_data
                                    EDIT -> Res.string.btn_edit_data
                                }
                            )
                        )
                    }

                    if (state.screenType == EDIT) {
                        Button(
                            onClick = {
                                vm.onEvent(MemberAddEditEvent.OnDeletePressed)
                            },
                            shape = RoundedCornerShape(
                                topEnd = 20F,
                                bottomEnd = 20F,
                                topStart = 5F,
                                bottomStart = 5F
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError,
                            ),
                            modifier = Modifier
                                .weight(1F)
                        ) {
                            Text(
                                "Hapus"
                            )
                        }
                    }
                }
            }
        }
    }
}