package org.lelestacia.qurban_ticketing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import org.lelestacia.qurban_ticketing.theme.QurbanTicketingTheme
import org.lelestacia.qurban_ticketing.util.LocalScreenPadding
import org.lelestacia.qurban_ticketing.util.Navigator
import org.lelestacia.qurban_ticketing.util.padding.CustomPadding

@OptIn(ExperimentalMaterial3ExpressiveApi::class, KoinExperimentalAPI::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navigator = koinInject<Navigator>()
            val entryProvider = koinEntryProvider<Any>()

            CompositionLocalProvider(
                LocalScreenPadding provides
                        CustomPadding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),
            ) {
                QurbanTicketingTheme {
                    NavDisplay(
                        backStack = navigator.backstack,
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator(),
                        ),
                        onBack = { navigator.goBack() },
                        entryProvider = entryProvider,
                        transitionSpec = {
                            fadeIn() togetherWith fadeOut()
                        },
                        predictivePopTransitionSpec = {
                            fadeIn() togetherWith fadeOut()
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}