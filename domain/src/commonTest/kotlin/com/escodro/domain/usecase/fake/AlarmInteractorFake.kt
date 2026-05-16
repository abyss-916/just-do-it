package com.escodro.domain.usecase.fake

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.Task

internal class AlarmInteractorFake : AlarmInteractor {

    private val alarmMap: MutableMap<Long, Long> = mutableMapOf()

    var updatedTask: Task? = null

    var lastCancelledTask: Task? = null
        private set

    override fun schedule(task: Task, timeInMillis: Long) {
        alarmMap[task.id] = timeInMillis
    }

    override fun cancel(task: Task) {
        lastCancelledTask = task
        alarmMap.remove(task.id)
    }

    override fun update(task: Task) {
        updatedTask = task
    }

    fun isAlarmScheduled(alarmId: Long): Boolean =
        alarmMap.contains(alarmId)

    fun getAlarmTime(alarmId: Long): Long? =
        alarmMap[alarmId]

    fun clear() {
        alarmMap.clear()
        updatedTask = null
        lastCancelledTask = null
    }
}
