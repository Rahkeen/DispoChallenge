package com.rikin.dispochallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rikin.dispochallenge.data.GiphyRepository
import com.rikin.dispochallenge.features.feed.GifFeed
import com.rikin.dispochallenge.ui.theme.DispoChallengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DispoChallengeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val appStoreViewModel: AppStoreViewModel by viewModels {
                        AppStoreViewModelFactory(
                            initialState = AppState(),
                            repository = GiphyRepository()
                        )
                    }

                    val state by appStoreViewModel.states().collectAsState()

                    GifFeed(feed = state.images)
                }
            }
        }
    }
}