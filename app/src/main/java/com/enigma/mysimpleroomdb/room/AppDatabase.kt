package com.enigma.mysimpleroomdb.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.enigma.mysimpleroomdb.room.dao.NoteDao
import com.enigma.mysimpleroomdb.room.entities.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun noteDao() : NoteDao

    // biar dapat di panggil secara global
    companion object {
        // ada 2, Volatile dan Non-Volatile
        // ini jatuhnya kita bermain dengan memory
        // karena nanti kita akan bermain dengan banyak memory dan dengan Volatile ini kita bisa ngakses ke satu instance yg sama
        @Volatile private var instance : AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            //namaDatabsaenya
            "mst_notes.db"
        ).build()

    }
}