package ru.nak.ied.retrofitlesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nak.ied.retrofitlesson.databinding.FragmentLoginBinding
import ru.nak.ied.retrofitlesson.retrofit.AuthRequest
import ru.nak.ied.retrofitlesson.retrofit.MainApi


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mainApi: MainApi
    private val viewModel: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()
        binding.apply {
            bNext.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_productsFragment)
            }
            bSignIn.setOnClickListener {
                auth(
                    AuthRequest(
                        login.text.toString(),
                        password.text.toString()
                    )
                )
            }
        }
    }

    private fun initRetrofit() {
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
        mainApi = retrofit.create(MainApi::class.java)
    }

    private fun auth(authRequest: AuthRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mainApi.auth(authRequest)
            // в корутине обязательно запускать через requireActivity
            val message = response.errorBody()?.string()?.let {
                JSONObject(it).getString("message")
            }
            requireActivity().runOnUiThread {
                binding.error.text = message
            }
        }
    }
}