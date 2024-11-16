package com.enigma.mysimpleroomdb

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.enigma.mysimpleroomdb.databinding.ActivityEditBinding
import com.enigma.mysimpleroomdb.room.AppDatabase
import com.enigma.mysimpleroomdb.room.entities.Note
import com.enigma.mysimpleroomdb.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    val db by lazy { AppDatabase(this) }
    private var noteId = 0
    var intentType = 0


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
        intentType = intent.getIntExtra("intent_type", 0)
        setupLstener()
        setupView()

        noteId = intent.getIntExtra("intent_id", 0)
        Toast.makeText(this, noteId.toString(), Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    fun setupView() {
        when (intentType) {
//            Constant.TYPE_CREATE ->{
//
//            }
            Constant.TYPE_READ -> {
                binding.buttonSave.visibility = View.GONE
                binding.editNama.isEnabled = false
                binding.editUmur.isEnabled = false
                binding.editAlamat.isEnabled = false
                getNote()
            }

            Constant.TYPE_UPDATE -> {
                binding.buttonSave.text = "UPDATE"
                getNote()
            }
        }
    }

    private fun setupLstener() {
        binding.buttonSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                when (intentType) {
                    Constant.TYPE_CREATE -> {
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

                    Constant.TYPE_UPDATE -> {
                        db.noteDao().updateNote(
                            Note(
                                noteId,
                                binding.editNama.text.toString(),
                                binding.editUmur.text.toString(),
                                binding.editAlamat.text.toString()
                            )
                        )
                    }
                }
            }
            finish()
//            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun getNote() {
        noteId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNote(noteId)
            binding.editNama.setText(notes.nama)
            binding.editUmur.setText(notes.umur)
            binding.editAlamat.setText(notes.alamat)
        }
    }
}