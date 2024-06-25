package ru.nak.ied.retrofitlesson.retrofit

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.nak.ied.retrofitlesson.retrofit.data.Product
import ru.nak.ied.retrofitlesson.retrofit.data.User

// 'https://dummyjson.com/products/1'
//interface ProductApi {
//    @GET("products/1")
//    suspend fun getProductById(): Product
//}

interface MainApi {

    // 'https://dummyjson.com/products/1'
    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    // 'https://dummyjson.com/auth/login'
    @POST("auth/login")
    suspend fun auth(@Body authRequest: AuthRequest): User
}