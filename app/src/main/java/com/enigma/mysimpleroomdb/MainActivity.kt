package com.enigma.mysimpleroomdb


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.enigma.mysimpleroomdb.adapter.NoteAdapter
import com.enigma.mysimpleroomdb.databinding.ActivityMainBinding
import com.enigma.mysimpleroomdb.room.AppDatabase
import com.enigma.mysimpleroomdb.room.entities.Note
import com.enigma.mysimpleroomdb.utils.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val db by lazy { AppDatabase(this) }
    val activityMain = "MAINACTIVITY"
    private lateinit var binding : ActivityMainBinding
    lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupListener()
        setUpRecycleView()
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNotes()
            Log.d(activityMain,"DbResponse : $notes")
            withContext(Dispatchers.Main){
                noteAdapter.setData(notes)
            }
        }
    }

    //Berpindah ke EditActivity
    private fun setupListener(){
        binding.buttonCreate.setOnClickListener {
            //startActivity(Intent(this,EditActivity::class.java))
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }

    private fun setUpRecycleView(){
        noteAdapter = NoteAdapter(arrayListOf(), object : NoteAdapter.OnAdapterListener{
            override fun onClick(note: Note) {
                //Toast.makeText(applicationContext, note.nama, Toast.LENGTH_SHORT).show()
                intentEdit(note.id, Constant.TYPE_READ)
            }

            override fun onUpdate(note: Note) {
                intentEdit(note.id,Constant.TYPE_UPDATE)
            }
        })

        binding.listNote.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = noteAdapter
        }
    }

    fun intentEdit(noteId : Int, intentType : Int){
        startActivity(Intent(
            applicationContext,EditActivity::class.java)
            .putExtra("intent_id", noteId)
            .putExtra("intent_type", intentType)

        )
    }
}