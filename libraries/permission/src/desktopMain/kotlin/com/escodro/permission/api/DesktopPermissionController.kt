package com.escodro.permission.api

@Suppress("ForbiddenComment")
internal class DesktopPermissionController : PermissionController {

    override val controller: Any = Any()

    override suspend fun requestPermission(permission: Permission) {
        // Desktop has no runtime permissions; this is a no-op
    }

    @Suppress("ExpressionBodySyntax")
    override suspend fun isPermissionGranted(permission: Permission): Boolean {
        // Desktop has no runtime permissions; all permissions are granted
        return true
    }
}
