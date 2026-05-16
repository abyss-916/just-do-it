package com.escodro.desktopapp

import java.awt.Window
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

fun Window.setupMenuBar(onQuit: () -> Unit) {
    val menuBar = JMenuBar()

    val fileMenu = JMenu("File")
    val quitItem = JMenuItem("Quit").apply {
        accelerator = javax.swing.KeyStroke.getKeyStroke(
            java.awt.event.KeyEvent.VK_Q,
            java.awt.Toolkit.getDefaultToolkit().menuShortcutKeyMaskEx,
        )
        addActionListener { onQuit() }
    }
    fileMenu.add(quitItem)
    menuBar.add(fileMenu)

    val viewMenu = JMenu("View")
    menuBar.add(viewMenu)

    val helpMenu = JMenu("Help")
    helpMenu.add(
        JMenuItem("About Alkaa").apply {
            addActionListener { }
        },
    )
    menuBar.add(helpMenu)

    (this as? JFrame)?.jMenuBar = menuBar
}
