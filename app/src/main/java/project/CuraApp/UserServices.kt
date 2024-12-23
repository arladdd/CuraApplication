package project.CuraApp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Body

interface UserServices {
    @POST("users/register")
    suspend fun postRegister(
        @Body registerRequest: RegisterRequest
    ): Response<TokenResponse>

    @POST("users/login")
    suspend fun postLogin(
        @Body loginRequest: LoginRequest
    ): Response<TokenResponse>

}

