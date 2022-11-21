package edu.uca.innovatech.cookbook.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.uca.innovatech.cookbook.core.Converters
import edu.uca.innovatech.cookbook.data.database.dao.RecetaDao
import edu.uca.innovatech.cookbook.data.database.entities.Paso
import edu.uca.innovatech.cookbook.data.database.entities.Receta

@Database(entities = [Receta::class, Paso::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class RecetasRoomDatabase : RoomDatabase() {

    abstract fun RecetaDao(): RecetaDao

    companion object {
        @Volatile
        private var INSTANCE: RecetasRoomDatabase? = null

        fun getDatabase(context: Context): RecetasRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecetasRoomDatabase::class.java,
                    "recetas_database"
                ).createFromAsset("database/recetas.db")
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}