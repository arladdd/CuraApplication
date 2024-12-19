package project.CuraApp


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProblemServices {

    // Get a list of problems with pagination
    @GET("problems/all")
    suspend fun getProblems(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ProblemResponse>

    // Get problem details by ID
    @GET("problems/{id}")
    suspend fun getProblemDetails(@Path("id") id: String): Response<DetailItem>
}
