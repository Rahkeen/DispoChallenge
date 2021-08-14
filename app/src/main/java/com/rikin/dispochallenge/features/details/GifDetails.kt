package com.rikin.dispochallenge.features.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.rikin.dispochallenge.data.DUMMY_GIF_URL
import com.rikin.dispochallenge.data.GiphyGif
import com.rikin.dispochallenge.data.GiphyImage
import com.rikin.dispochallenge.data.GiphyImages

@Composable
fun GifDetails(selectedGif: GiphyGif) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberImagePainter(
                data = selectedGif.images.original.url,
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Crop
        )
        
        Text(text = selectedGif.title, fontSize = 16.sp, textAlign = TextAlign.Center)
        Text(text = "Uploaded by: ${selectedGif.username}", fontSize = 16.sp)
        Text(text = "Rating: ${selectedGif.rating}", fontSize = 16.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun GifDetailsPreview() {
    val gif = GiphyGif(
        id = "123",
        title = "Dis dummy gif",
        images = GiphyImages(
            original = GiphyImage(100, 100, DUMMY_GIF_URL),
            originalStill = GiphyImage(100, 100, DUMMY_GIF_URL)
        ),
        rating = "g",
        url = DUMMY_GIF_URL,
        username = "rikinthedream"
    )
    
    GifDetails(selectedGif = gif)
}