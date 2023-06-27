package es.com.uam.eps.tfg.learnp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.com.uam.eps.tfg.learnp.model.Word
import kotlin.jvm.Volatile;

@Database(entities = [Word ::class], version = 1, exportSchema = false)
abstract class LearnPDatabase : RoomDatabase() {

    /* Objeto para acceder a la BD sin tener que instanciarla fuera */
    companion object {
        @Volatile
        private var INSTANCE: LearnPDatabase? = null

        fun getInstance(context: Context): LearnPDatabase {
            var instance = INSTANCE

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    LearnPDatabase::class.java,
                    "specs"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
            }
            return instance
        }
    }
}