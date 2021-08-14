package com.rikin.dispochallenge.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.giphy.com/v1/gifs/"
private const val API_KEY = "74IXqjiV8RuLA2yZT2EuloLW9mHhIlna"

const val DUMMY_GIF_URL =
    "https://media1.giphy.com/media/nDSlfqf0gn5g4/giphy.gif?cid=b9a125e2inw13gauixd8pe0zgbg76js7rnm7z4w3mlfov9wv&rid=giphy.gif&ct=g"

class GiphyRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val client = retrofit.create(GiphyService::class.java)

    suspend fun getGifs(query: String): GiphyGifs {
        return withContext(Dispatchers.IO) {
            client.getGifs(searchQuery = query)
        }
    }

    suspend fun getFeaturedGifs(): GiphyGifs {
        return withContext(Dispatchers.IO) {
            client.getTrendingGifs()
        }
    }
}

interface GiphyService {

    @GET("search")
    suspend fun getGifs(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("q") searchQuery: String
    ): GiphyGifs

    @GET("trending")
    suspend fun getTrendingGifs(
        @Query("api_key") apiKey: String = API_KEY
    ): GiphyGifs
}

@JsonClass(generateAdapter = true)
data class GiphyGifs(
    val data: List<GiphyGif>
)

@JsonClass(generateAdapter = true)
data class GiphyGif(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "images") val images: GiphyImages,
    @Json(name = "rating") val rating: String,
    @Json(name = "url") val url: String,
    @Json(name = "username") val username: String
)

@JsonClass(generateAdapter = true)
data class GiphyImages(
    @Json(name = "original") val original: GiphyImage,
    @Json(name = "original_still") val originalStill: GiphyImage
)

@JsonClass(generateAdapter = true)
data class GiphyImage(
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "url") val url: String,
)
