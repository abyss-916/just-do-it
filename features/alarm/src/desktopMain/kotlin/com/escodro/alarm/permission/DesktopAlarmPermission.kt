package com.escodro.alarm.permission

import com.escodro.alarmapi.AlarmPermission

@Suppress("ForbiddenComment")
internal class DesktopAlarmPermission : AlarmPermission {

    @Suppress("ExpressionBodySyntax")
    override fun hasExactAlarmPermission(): Boolean {
        // Desktop has no exact alarm restrictions
        return true
    }

    override fun openExactAlarmPermissionScreen() {
        // Desktop has no exact alarm restrictions; no-op
    }

    override fun openAppSettings() {
        // Desktop has no app settings screen; no-op
    }
}
