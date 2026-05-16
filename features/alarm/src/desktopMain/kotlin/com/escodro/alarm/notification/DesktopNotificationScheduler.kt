package com.escodro.alarm.notification

import com.escodro.alarm.model.Task
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

internal class DesktopNotificationScheduler(
    private val taskNotification: TaskNotification,
) : NotificationScheduler {

    private val logger = KotlinLogging.logger {}

    private val scheduler = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "desktop-alarm-scheduler").apply { isDaemon = true }
    }

    private val scheduledTasks = ConcurrentHashMap<Long, ScheduledFuture<*>>()

    override fun scheduleTaskNotification(task: Task, timeInMillis: Long) {
        logger.info { "Scheduling alarm for task ${task.id} at $timeInMillis" }
        cancelTaskNotification(task)

        val delay = timeInMillis - System.currentTimeMillis()
        if (delay <= 0) {
            logger.warn { "Alarm time already passed for task ${task.id}" }
            return
        }

        val future = scheduler.schedule({
            if (!task.isCompleted) {
                taskNotification.show(task)
            }
            scheduledTasks.remove(task.id)
        }, delay, TimeUnit.MILLISECONDS)

        scheduledTasks[task.id] = future
    }

    override fun cancelTaskNotification(task: Task) {
        logger.info { "Canceling alarm for task ${task.id}" }
        scheduledTasks.remove(task.id)?.cancel(false)
    }

    override fun updateTaskNotification(task: Task) {
        logger.info { "Updating alarm for task ${task.id}" }
        cancelTaskNotification(task)
        if (!task.isCompleted && task.alarmDate != null) {
            scheduleTaskNotification(task, task.alarmDate)
        }
    }

    private fun scheduleTaskNotification(task: Task, dateTime: kotlinx.datetime.LocalDateTime) {
        val timeInMillis = dateTime
            .toInstant(kotlinx.datetime.TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
        scheduleTaskNotification(task, timeInMillis)
    }
}
