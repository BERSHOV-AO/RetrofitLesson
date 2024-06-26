package ru.nak.ied.retrofitlesson

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import ru.nak.ied.retrofitlesson.databinding.ContentBaseBinding

class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ContentBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}