package com.example.multithreading1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.multithreading1.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val FILE_NAME = "userData.txt"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveDataButton.setOnClickListener {
            suspend fun writeFileOnInternalStorage() {
                withContext(Dispatchers.IO) {
                    val text = with(binding) {
                        inputLayoutName.editText?.text.toString() + "\n" +
                                inputLayoutSurname.editText?.text.toString() + "\n" +
                                inputLayoutEmail.editText?.text.toString() + "\n" +
                                inputLayoutPhone.editText?.text.toString()
                    }
                    with(openFileOutput(FILE_NAME, MODE_PRIVATE)) {
                        write(text.toByteArray())
                        flush()
                        close()
                    }
                }
            }
            lifecycleScope.launch {
                writeFileOnInternalStorage()
            }
        }


    }
}