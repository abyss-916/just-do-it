@file:Suppress("ktlint:standard:filename")

package com.escodro.desktopapp

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.escodro.resources.Res
import com.escodro.resources.content_app_name
import com.escodro.shared.AlkaaMultiplatformApp
import com.escodro.shared.di.initKoin
import org.jetbrains.compose.resources.stringResource
import java.awt.Dimension

fun main() {
    application {
        initKoin()

        val windowState = rememberWindowState(
            size = DpSize(width = 1200.dp, height = 800.dp),
        )

        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.content_app_name),
            state = windowState,
            resizable = true,
        ) {
            window.minimumSize = Dimension(800, 600)
            window.setupMenuBar(onQuit = ::exitApplication)
            AlkaaMultiplatformApp()
        }
    }
}
