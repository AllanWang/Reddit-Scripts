package ca.allanwang.reddit.api.pushshift

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject

class PushshiftProvider @Inject internal constructor(

) {
    companion object {
        private const val BASE_URL =
            "https://api.pushshift.io/reddit/search/"
    }

    fun createApi(): PushshiftBaseApi {
        val client = OkHttpClient.Builder()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory())

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi.build()))
            .client(client.build())

        return retrofit.build().create()
    }
}