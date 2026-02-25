package org.lelestacia.qurban_ticketing.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import org.lelestacia.qurban_ticketing.util.padding.CustomPadding


val LocalScreenPadding: ProvidableCompositionLocal<CustomPadding> =
    compositionLocalOf { error("Local Padding haven't been initialized") }

val LocalSnackbarHost: ProvidableCompositionLocal<SnackbarHostState> =
    compositionLocalOf { error("Snackbar Host haven't been initialized") }