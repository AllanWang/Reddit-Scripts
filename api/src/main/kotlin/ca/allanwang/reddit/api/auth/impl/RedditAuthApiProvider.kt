package ca.allanwang.reddit.api.auth.impl

import ca.allanwang.reddit.api.RedditApi
import ca.allanwang.reddit.api.auth.RedditAuthApi
import ca.allanwang.reddit.api.impl.RedditApiProvider
import ca.allanwang.reddit.api.impl.RedditConfigModule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject

class RedditAuthApiProvider @Inject internal constructor(

){
    companion object {
        private const val BASE_URL = "https://www.reddit.com/api/"
    }

    fun createApi(): RedditAuthApi {
        val client = OkHttpClient.Builder()

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory())

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi.build()))
            .client(client.build())

        return retrofit.build().create()
    }

}

@Module
object RedditAuthApiModule {
    @Provides
    @JvmStatic
    fun api(provider: RedditAuthApiProvider): RedditAuthApi = provider.createApi()
}