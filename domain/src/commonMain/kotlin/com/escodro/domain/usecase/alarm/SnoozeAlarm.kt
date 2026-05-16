package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.provider.DateTimeProvider
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.task.LoadTask
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import mu.KotlinLogging
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Use case to snooze an alarm for a task.
 */
@OptIn(ExperimentalTime::class)
class SnoozeAlarm(
    private val loadTask: LoadTask,
    private val dateTimeProvider: DateTimeProvider,
    private val notificationInteractor: NotificationInteractor,
    private val alarmInteractor: AlarmInteractor,
    private val taskRepository: TaskRepository,
) {

    private val logger = KotlinLogging.logger {}

    /**
     * Snoozes the task.
     *
     * @param taskId the task id
     * @param minutes time to be snoozed in minutes
     */
    suspend operator fun invoke(taskId: Long, minutes: Int = DEFAULT_SNOOZE) {
        require(minutes > 0) { "The delay minutes must be positive" }
        val task = loadTask(taskId = taskId) ?: return

        val snoozedTime = getSnoozedTask(dateTimeProvider.getCurrentInstant(), minutes)
        val updatedTask = task.copy(
            alarmDate = dateTimeProvider.getCurrentInstant()
                .plus(minutes.minutes)
                .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()),
        )
        taskRepository.updateTask(updatedTask)
        alarmInteractor.schedule(updatedTask, snoozedTime)
        notificationInteractor.dismiss(updatedTask)
        logger.debug { "Task snoozed in $minutes minutes" }
    }

    private fun getSnoozedTask(instant: Instant, minutes: Int): Long {
        val updatedCalendar = instant.plus(minutes.minutes)
        return updatedCalendar.toEpochMilliseconds()
    }

    companion object {

        private const val DEFAULT_SNOOZE = 15
    }
}
