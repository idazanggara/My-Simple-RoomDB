package com.enigma.mysimpleroomdb

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.enigma.mysimpleroomdb.databinding.ActivityEditBinding
import com.enigma.mysimpleroomdb.room.AppDatabase
import com.enigma.mysimpleroomdb.room.entities.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    val db by lazy { AppDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupLstener()

    }

    private fun setupLstener() {
        binding.buttonSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().addNote(
                    //addNote yg kita butuhkan adalah id,title,dan notenya
                    //id disini karena sudah auto generate maka tidak harus diisi
                    Note(
                        0,
                        binding.editNama.text.toString(),
                        binding.editUmur.text.toString(),
                        binding.editAlamat.text.toString()
                    )
                )
            }
            finish()
//            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}