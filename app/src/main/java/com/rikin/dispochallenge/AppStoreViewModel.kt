@file:Suppress("UNCHECKED_CAST")

package com.rikin.dispochallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rikin.dispochallenge.data.GiphyGif
import com.rikin.dispochallenge.data.GiphyGifs
import com.rikin.dispochallenge.data.GiphyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppState(
    val images: List<GiphyGif> = emptyList(),
    val searchQuery: String = "",
    val selectedGif: GiphyGif? = null,
    val isError: Boolean = false
)

sealed class AppAction {
    class Search(val query: String): AppAction()
    class SelectGif(val selectedGif: GiphyGif): AppAction()
}

class AppStoreViewModel(
    initialState: AppState,
    private val repository: GiphyRepository
) : ViewModel() {
    private val appStates = MutableStateFlow(initialState)

    init {
        search(appStates.value.searchQuery)
    }

    fun states(): StateFlow<AppState> {
        return appStates.asStateFlow()
    }

    fun send(action: AppAction) {
        when(action) {
            is AppAction.Search -> {
                search(query = action.query)
            }
            is AppAction.SelectGif -> {
                selectGif(gif = action.selectedGif)
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            try {
                val gifs = if (query.isNotEmpty()) {
                    repository.getGifs(query)
                } else {
                    repository.getFeaturedGifs()
                }

                appStates.emit(gifs.toAppState(query = query))
            } catch (error: Exception) {
                appStates.emit(AppState(isError = true, searchQuery = query))
            }
        }
    }

    fun selectGif(gif: GiphyGif) {
        appStates.value = appStates.value.copy(selectedGif = gif)
    }

    private fun GiphyGifs.toAppState(query: String): AppState {
        return AppState(images = data, searchQuery = query)
    }
}

class AppStoreViewModelFactory(
    private val initialState: AppState,
    private val repository: GiphyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppStoreViewModel(initialState, repository) as T
    }
}