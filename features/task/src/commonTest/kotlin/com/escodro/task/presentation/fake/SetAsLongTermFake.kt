package com.escodro.task.presentation.fake

import com.escodro.domain.usecase.task.SetAsLongTerm

internal class SetAsLongTermFake : SetAsLongTerm {

    var lastTaskId: Long? = null

    override suspend fun invoke(taskId: Long) {
        lastTaskId = taskId
    }
}
