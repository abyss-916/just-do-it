package com.escodro.categoryapi.presentation

import com.escodro.categoryapi.model.Category
import kotlinx.collections.immutable.ImmutableList

/**
 * Represents the states of [CategoryListViewModel].
 */
sealed class CategoryState {

    data object Loading : CategoryState()

    data class Loaded(val categoryList: ImmutableList<Category>) : CategoryState()

    data object Empty : CategoryState()

    data class Error(val message: String) : CategoryState()
}
