package com.escodro.search.presentation

import androidx.lifecycle.ViewModel
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.search.SearchTasksByName
import com.escodro.search.mapper.TaskSearchMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class SearchViewModel(
    private val findTaskUseCase: SearchTasksByName,
    private val mapper: TaskSearchMapper,
) : ViewModel() {

    fun findTasksByName(name: String = ""): Flow<SearchViewState> =
        findTaskUseCase(name)
            .map { taskList ->
                if (taskList.isNotEmpty()) {
                    val searchList = mapper.toTaskSearch(taskList)
                    SearchViewState.Loaded(searchList)
                } else {
                    SearchViewState.Empty
                }
            }
            .catch { emit(SearchViewState.Error(it.message ?: "Unknown error")) }
}
