package edu.uca.innovatech.cookbook.data.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import edu.uca.innovatech.cookbook.data.database.dao.CocinandoDao
import edu.uca.innovatech.cookbook.data.database.util.Converters
import edu.uca.innovatech.cookbook.data.database.dao.RecetaDao
import edu.uca.innovatech.cookbook.data.database.entities.CookingReceta
import edu.uca.innovatech.cookbook.data.database.entities.Ingrediente
import edu.uca.innovatech.cookbook.data.database.entities.Paso
import edu.uca.innovatech.cookbook.data.database.entities.Receta

@Database(entities = [Receta::class, Paso::class, Ingrediente::class, CookingReceta::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class CookBookRoomDatabase : RoomDatabase() {

    abstract fun RecetaDao(): RecetaDao
    abstract fun CocinandoDao(): CocinandoDao

    companion object {
        @Volatile
        private var INSTANCE: CookBookRoomDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `cooking` (`idCooking` INTEGER NOT NULL, " +
                        " `id_receta` INTEGER NOT NULL, `current_step` INTEGER NOT NULL, " +
                        "PRIMARY KEY(`idCooking`))")
            }
        }

        fun getDatabase(context: Context): CookBookRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CookBookRoomDatabase::class.java,
                    "recetas_database"
                ).addMigrations(MIGRATION_1_2)
                    .createFromAsset("database/recetas_database.db")
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}