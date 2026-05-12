package com.escodro.task.presentation.fake

import com.escodro.domain.usecase.task.UnsetAsLongTerm

internal class UnsetAsLongTermFake : UnsetAsLongTerm {

    var invoked: Boolean = false

    override suspend fun invoke() {
        invoked = true
    }
}
