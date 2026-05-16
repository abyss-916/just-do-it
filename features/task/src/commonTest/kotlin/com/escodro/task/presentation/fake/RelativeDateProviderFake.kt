package com.escodro.task.presentation.fake

import com.escodro.task.provider.RelativeDateProvider
import kotlinx.datetime.LocalDateTime

internal class RelativeDateProviderFake : RelativeDateProvider {

    override fun toRelativeDateString(date: LocalDateTime): String = date.toString().take(10)
}
