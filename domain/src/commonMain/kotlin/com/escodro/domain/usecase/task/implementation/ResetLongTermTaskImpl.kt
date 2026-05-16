package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.task.ResetLongTermTask
import kotlinx.datetime.LocalDateTime

internal class ResetLongTermTaskImpl(
    private val taskRepository: TaskRepository,
) : ResetLongTermTask {

    override suspend operator fun invoke(thresholdDate: LocalDateTime) {
        taskRepository.resetLongTermTaskIfCompletedBefore(thresholdDate)
    }
}
