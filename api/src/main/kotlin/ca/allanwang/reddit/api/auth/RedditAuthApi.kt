package ca.allanwang.reddit.api.auth

import com.squareup.moshi.Json
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface RedditAuthApi {

    @POST("v1/access_token")
    @FormUrlEncoded
    suspend fun accessToken(
        @Header("Authorization")
        authorization: String,
        @Field("grant_type")
        grantType: String
    ): RedditAuthResponse
}

data class RedditAuthResponse(
    val access_token: String,
    val expires_in: Int,
    val scope: String,
    val token_type: String
)