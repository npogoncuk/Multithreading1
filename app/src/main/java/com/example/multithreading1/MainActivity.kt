package com.example.multithreading1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.multithreading1.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.util.NoSuchElementException

private const val FILE_NAME = "userData.txt"
const val SAVED_DATA_KEY = "saved_data"

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
                        inputLayoutName.editText?.text.toString().ifBlank { "You saved blank text" } + "\n" +
                                inputLayoutSurname.editText?.text.toString().ifBlank { "You saved blank text" } + "\n" +
                                inputLayoutEmail.editText?.text.toString().ifBlank { "You saved blank text" } + "\n" +
                                inputLayoutPhone.editText?.text.toString().ifBlank { "You saved blank text" }
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

        binding.seeDataButton.setOnClickListener {
            suspend fun readDataFromFile(): String? {
                return withContext(Dispatchers.IO) {
                    var data: List<String>?
                    try {
                        data = filesDir.listFiles()?.first { it.name == FILE_NAME }?.readLines()
                    } catch (e: FileNotFoundException) {data = null}
                    catch (e: NoSuchElementException) {data = null}

                    if (data != null) {
                        data.map { it.ifBlank { "No data" } }
                        return@withContext resources.getString(R.string.saved_data, data[0], data[1], data[2], data[3])
                    } else null
                }
            }
            lifecycleScope.launch {
                val savedData = readDataFromFile()
                if (savedData != null) {
                    val intent = Intent(this@MainActivity, SeeDataActivity::class.java)
                    intent.putExtra(SAVED_DATA_KEY, savedData)
                    startActivity(intent)
                }
                else Toast.makeText(this@MainActivity, "You should save file first", Toast.LENGTH_SHORT).show()
            }

        }
    }
}