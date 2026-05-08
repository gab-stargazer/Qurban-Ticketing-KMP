package org.lelestacia.qurban_ticketing.util

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionStatus.isNotGranted(): Boolean {
    return !this.isGranted
}