package ca.allanwang.reddit.api.impl

import ca.allanwang.reddit.api.JrawProvider
import ca.allanwang.reddit.api.RedditApi
import ca.allanwang.reddit.api.RedditConfig
import ca.allanwang.reddit.api.auth.RedditAuthApi
import ca.allanwang.reddit.api.auth.impl.RedditAuthApiModule
import com.google.common.flogger.FluentLogger
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.runBlocking
import net.dean.jraw.RedditClient
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditApiProvider @Inject internal constructor(
    private val config: RedditConfig,
    private val authApi: RedditAuthApi,
) {

    companion object {
        private const val BASE_URL = "https://oauth.reddit.com/"

        private val logger = FluentLogger.forEnclosingClass()
    }

    private fun token(): String? {
        val expiration = config.expiration ?: return null
        val token = config.accessToken ?: return null
        return token.takeIf { expiration > System.currentTimeMillis() }
    }

    suspend fun newToken(): String {
        logger.atInfo().log("Create new token")
        val credential = Credentials.basic(config.clientId, config.clientSecret)
        val accessResponse = authApi.accessToken(
            authorization = credential,
            grantType = "client_credentials"
        )
        config.accessToken = accessResponse.access_token
        config.expiration =
            accessResponse.expires_in * 1000 + System.currentTimeMillis()
        config.save()
        return accessResponse.access_token
    }

    fun createApi(): RedditApi {
        val token = token() ?: runBlocking { newToken() }

        val authInterceptor: Interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
            request.addHeader("Authorization", "Bearer $token")
            chain.proceed(request.build())
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory())

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi.build()))
            .client(client.build())

        return retrofit.build().create()
    }

}