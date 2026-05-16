package com.escodro.domain.usecase.fake

import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.model.Task

internal class NotificationInteractorFake : NotificationInteractor {

    private val notificationMap: MutableMap<Long, Boolean> = mutableMapOf()

    var lastDismissedTask: Task? = null
        private set

    override fun show(task: Task) {
        notificationMap[task.id] = true
    }

    override fun dismiss(task: Task) {
        lastDismissedTask = task
        notificationMap[task.id] = false
    }

    fun isNotificationShown(notificationId: Long): Boolean =
        notificationMap[notificationId] == true

    fun clear() {
        notificationMap.clear()
        lastDismissedTask = null
    }
}
