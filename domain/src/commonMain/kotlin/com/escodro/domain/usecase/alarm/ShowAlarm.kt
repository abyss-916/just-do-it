package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.repository.TaskRepository
import mu.KotlinLogging

/**
 * Use case to show an alarm.
 */
class ShowAlarm(
    private val taskRepository: TaskRepository,
    private val notificationInteractor: NotificationInteractor,
    private val scheduleNextAlarm: ScheduleNextAlarm,
) {

    private val logger = KotlinLogging.logger {}

    /**
     * Shows the alarm.
     *
     * @param taskId the alarm id to be shown
     */
    suspend operator fun invoke(taskId: Long) {
        val task = taskRepository.findTaskById(taskId) ?: return

        if (task.isCompleted) {
            logger.debug { "Task '${task.title}' is already completed. Will not notify" }
            return
        } else {
            logger.debug { "Notifying task '${task.title}'" }
            notificationInteractor.show(task)
        }

        if (task.isRepeating && task.alarmInterval != null) {
            scheduleNextAlarm(task)
        } else if (task.isRepeating) {
            logger.warn { "Repeating task '${task.title}' has no alarmInterval. Cancelling alarm." }
            taskRepository.updateTask(task.copy(isRepeating = false))
        }
    }
}
