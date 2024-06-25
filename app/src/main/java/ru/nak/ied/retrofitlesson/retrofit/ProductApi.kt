package ru.nak.ied.retrofitlesson.retrofit

import retrofit2.http.GET

// 'https://dummyjson.com/products/1'
interface ProductApi {
    @GET("products/1")
    suspend fun getProductById(): Product
}