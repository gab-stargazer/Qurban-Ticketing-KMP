package org.lelestacia.qurban_ticketing.util

import androidx.lifecycle.Lifecycle

fun Lifecycle.State.handleWhenLifecycleResumed(
    onResumed: () -> Unit
) {
    if (this.isAtLeast(Lifecycle.State.RESUMED)) onResumed()
}