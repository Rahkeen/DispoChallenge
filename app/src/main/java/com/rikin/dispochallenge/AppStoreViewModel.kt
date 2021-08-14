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
    val isError: Boolean = false
)

class AppStoreViewModel(
    initialState: AppState,
    private val repository: GiphyRepository
): ViewModel() {
    private val appStates = MutableStateFlow(initialState)

    fun states(): StateFlow<AppState> {
        return appStates.asStateFlow()
    }

    fun search(query: String) {
        viewModelScope.launch {
           try {
               val gifs = repository.getGifs(query)
               appStates.emit(gifs.toAppState())
           } catch(error: Exception) {
               appStates.emit(AppState(isError = true))
           }
        }
    }

    private fun GiphyGifs.toAppState(): AppState {
        return AppState(images = data)
    }
}

class AppStoreViewModelFactory(
    private val initialState: AppState,
    private val repository: GiphyRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AppStoreViewModel(initialState, repository) as T
    }
}