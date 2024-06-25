package ru.nak.ied.retrofitlesson

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nak.ied.retrofitlesson.databinding.ActivityMainBinding
import ru.nak.ied.retrofitlesson.databinding.ActivitySecondBinding
import ru.nak.ied.retrofitlesson.retrofit.AuthRequest
import ru.nak.ied.retrofitlesson.retrofit.MainApi

class SecondActivity : AppCompatActivity() {
    lateinit var binding: ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // для логинга logcat
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        // добавляем клиента
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainApi = retrofit.create(MainApi::class.java)

        binding.button.setOnClickListener {
            // корутина запустит на второстепенном потоке
            CoroutineScope(Dispatchers.IO).launch {
                //val product = productApi.getProductById()
                //val product = mainApi.getProductById(3)
                // runOnUiThread - запустит на основном потоке
                val user = mainApi.auth(
                    AuthRequest(
                        binding.username.text.toString(),
                        binding.pussword.text.toString()
                    )
                )
                // по сути запуск обновления экрана
                runOnUiThread {
                    binding.apply {
                        //Picasso.get().load(user.image).into(binding.iv)
                        Picasso.get().load(user.image).into(iv)
                        firstName.text = user.firstName
                        lastName.text = user.lastName
                    }
                    // binding.text = product.title
                }
            }
        }
    }
}