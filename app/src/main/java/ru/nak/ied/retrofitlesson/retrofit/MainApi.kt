package ru.nak.ied.retrofitlesson.retrofit

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.nak.ied.retrofitlesson.retrofit.data.Product
import ru.nak.ied.retrofitlesson.retrofit.data.Products
import ru.nak.ied.retrofitlesson.retrofit.data.User

// 'https://dummyjson.com/products/1'
//interface ProductApi {
//    @GET("products/1")
//    suspend fun getProductById(): Product
//}

interface MainApi {

    // 'https://dummyjson.com/products/1'
    @GET("auth/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    // 'https://dummyjson.com/auth/login'
    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): User

    // 'https://dummyjson.com/products'
    @GET("auth/products")
    suspend fun getAllProducts(): Products

    @Headers("Content-Type: application/json")
    @GET("auth/products/search")
    suspend fun getProductByNameAuth(
        @Header("Authorization") token: String,
        @Query("q") name: String
    ): Products

    @GET("products/search")
    suspend fun getProductByName(@Query("q") name: String): Products
}