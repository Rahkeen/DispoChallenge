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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rikin.dispochallenge.data.GiphyRepository
import com.rikin.dispochallenge.features.details.GifDetails
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
                    val navController = rememberNavController()
                    val appStoreViewModel: AppStoreViewModel by viewModels {
                        AppStoreViewModelFactory(
                            initialState = AppState(),
                            repository = GiphyRepository()
                        )
                    }

                    val appState by appStoreViewModel.states().collectAsState()

                    NavHost(navController = navController, startDestination = AppLocation.Feed.name) {
                        composable(AppLocation.Feed.name) {
                            GifFeed(
                                state = appState,
                                actions = appStoreViewModel::send,
                                navigate = { navController.navigate(it.name) }
                            )
                        }
                        composable(AppLocation.Details.name) {
                            GifDetails(selectedGif = appState.selectedGif!!)
                        }
                    }
                }
            }
        }
    }
}