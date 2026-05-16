package com.escodro.alarm.notification

import com.escodro.alarm.model.Task
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon
import java.awt.TrayIcon.MessageType
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.concurrent.atomic.AtomicBoolean

internal class DesktopTaskNotification : TaskNotification {

    private val tray: SystemTray? = SystemTray.getSystemTray()
    private val icon: TrayIcon? = runCatching {
        TrayIcon(Toolkit.getDefaultToolkit().getImage(javaClass.getResource("/ic_launcher.png")), "Alkaa Reminder")
    }.getOrNull()

    init {
        icon?.apply {
            isImageAutoSize = true
            addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    tray?.remove(this@apply)
                }
            })
        }
    }

    override fun show(task: Task) {
        if (icon == null || tray == null) return

        tray.add(icon)
        icon.displayMessage(
            "Alkaa - Task Reminder",
            task.title,
            MessageType.INFO,
        )
    }

    override fun showRepeating(task: Task) {
        show(task)
    }

    override fun dismiss(taskId: Long) {
        icon?.let { tray?.remove(it) }
    }
}
