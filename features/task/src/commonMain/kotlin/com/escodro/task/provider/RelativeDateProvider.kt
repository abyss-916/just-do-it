package com.escodro.task.provider

import kotlinx.datetime.LocalDateTime

/**
 * Provides a relative date string (date only, no time) using the platform implementation.
 */
internal interface RelativeDateProvider {

    /**
     * Converts the [date] to a relative date string.
     *
     * @param date the date to be converted
     *
     * @return the relative date string
     */
    fun toRelativeDateString(date: LocalDateTime): String
}
