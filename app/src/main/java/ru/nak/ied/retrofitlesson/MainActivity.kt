package ru.nak.ied.retrofitlesson

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nak.ied.retrofitlesson.adapter.ProductAdapter
import ru.nak.ied.retrofitlesson.databinding.ActivityMainBinding
import ru.nak.ied.retrofitlesson.retrofit.AuthRequest
import ru.nak.ied.retrofitlesson.retrofit.MainApi
import ru.nak.ied.retrofitlesson.retrofit.data.User

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Гость"

        adapter = ProductAdapter()
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter


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

        //********************************************************************
        var user: User? = null
        CoroutineScope(Dispatchers.IO).launch {
            user = mainApi.auth(
                AuthRequest(
                    "emilys",
                    "emilyspass"
                )
            )
            runOnUiThread {
                supportActionBar?.title = user?.firstName
            }
        }

        //********************************************************************

        // setOnQueryTextListener - это наш слушатель
        binding.sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // когда пользователь написал текст и нажал на кнопку поиск,
            // сообщение передается в данный метод
            override fun onQueryTextSubmit(text: String?): Boolean {
                // корутина запустит на второстепенном потоке
                CoroutineScope(Dispatchers.IO).launch {
                    //val productsObject = mainApi.getProductByName(text)

                    // если text не равен null? то запкститься запрос
                    val productsObject = text?.let { mainApi.getProductByNameAuth(user?.token ?: "", it) }

                    // по сути запуск обновления экрана
                    runOnUiThread {
                        binding.apply {
                            adapter.submitList(productsObject?.products)
                        }
                    }
                }
                return true
            }

            // запускаеться каждый раз когда есть изменение в SV, живой поиск
            override fun onQueryTextChange(text: String?): Boolean {
//                // корутина запустит на второстепенном потоке
//                CoroutineScope(Dispatchers.IO).launch {
//                    //val productsObject = mainApi.getProductByName(text)
//
//                    // если text не равен null? то запкститься запрос
//                    val productsObject = text?.let { mainApi.getProductByName(it) }
//
//                    // по сути запуск обновления экрана
//                    runOnUiThread {
//                        binding.apply {
//                            adapter.submitList(productsObject?.products)
//                        }
//                    }
//                }
                return true
            }
        })
    }
}
