package com.escodro.domain.usecase.task

/**
 * Resets the long-term task if it was completed before today.
 */
fun interface ResetLongTermTask {

    /**
     * Resets the long-term task if it was completed before the given threshold date.
     *
     * @param thresholdDate the date to compare against
     */
    suspend operator fun invoke(thresholdDate: kotlinx.datetime.LocalDateTime)
}
