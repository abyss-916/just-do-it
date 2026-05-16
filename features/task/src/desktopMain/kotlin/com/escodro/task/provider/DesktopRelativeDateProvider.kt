package com.escodro.task.provider

import com.escodro.resources.Res
import com.escodro.resources.relative_date_days
import com.escodro.resources.relative_date_today
import com.escodro.resources.relative_date_tomorrow
import com.escodro.resources.relative_date_yesterday
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.getString
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class DesktopRelativeDateProvider : RelativeDateProvider {

    override fun toRelativeDateString(date: LocalDateTime): String {
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val targetDate = date.date
        val daysDiff = today.daysUntil(targetDate)

        return runBlocking {
            when (daysDiff) {
                0 -> getString(Res.string.relative_date_today)

                1 -> getString(Res.string.relative_date_tomorrow)

                -1 -> getString(Res.string.relative_date_yesterday)

                in 2..6 -> String.format(
                    getString(Res.string.relative_date_days),
                    daysDiff,
                )

                else -> date.toString().take(10)
            }
        }
    }
}
