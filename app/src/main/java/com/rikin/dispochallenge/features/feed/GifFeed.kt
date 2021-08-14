package com.rikin.dispochallenge.features.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.rikin.dispochallenge.AppAction
import com.rikin.dispochallenge.AppLocation
import com.rikin.dispochallenge.AppState
import com.rikin.dispochallenge.data.GiphyGif
import com.rikin.dispochallenge.ui.theme.DispoChallengeTheme

@Composable
fun GifFeed(
    state: AppState,
    actions: (AppAction) -> Unit = {},
    navigate: (AppLocation) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val (query, setQuery) = remember { mutableStateOf(state.searchQuery)}
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = query,
            onValueChange = {
                setQuery(it)
                actions(AppAction.Search(it))
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = state.images, key = { it.id }) { gif ->
                GifRow(
                    state = gif,
                    selectAction = { selectedGif ->
                        actions(AppAction.SelectGif(selectedGif))
                        navigate(AppLocation.Details)
                    }
                )
            }
        }
    }
}

@Composable
fun GifRow(state: GiphyGif, selectAction: (GiphyGif) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp)
            .clickable {
                selectAction(state)
            },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(
                data = state.images.original.url,
                builder = {
                    crossfade(true)
                }),
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = state.title)
    }
}

@Preview
@Composable
fun GifFeedPreview() {
    DispoChallengeTheme {
        GifFeed(state = AppState())
    }
}
