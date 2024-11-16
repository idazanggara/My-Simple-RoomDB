package com.enigma.mysimpleroomdb.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enigma.mysimpleroomdb.R
import com.enigma.mysimpleroomdb.databinding.ListItemBinding
import com.enigma.mysimpleroomdb.room.entities.Note

class NoteAdapter(
    private val notes : ArrayList<Note>,
    private val listener: OnAdapterListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        // findViewById
//        return  NoteViewHolder(
//            LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
//        )
        // binding
        return  NoteViewHolder(
            ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        //holder.textTitle.text = note.nama
        holder.apply {
            textTitle.text = note.nama
            textTitle.setOnClickListener {
                listener.onClick(note)
            }
            buttonEdit.setOnClickListener {
                listener.onUpdate(note)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Note>){
        notes.clear()
        notes.addAll(list)
        notifyDataSetChanged()
    }

    // findViewById
    //    class NoteViewHolder(view : View) : RecyclerView.ViewHolder(view){
//        val textTitle = view.findViewById<TextView>(R.id.text_title)
//    }
    // Binding
    class NoteViewHolder(val binding : ListItemBinding) : RecyclerView.ViewHolder(binding.root){
        val textTitle = binding.textTitle
        val buttonEdit = binding.iconEdit
        val buttonDelete = binding.iconDelete
    }

    interface OnAdapterListener{
        fun onClick(note: Note)
        fun onUpdate(note: Note)
    }
}