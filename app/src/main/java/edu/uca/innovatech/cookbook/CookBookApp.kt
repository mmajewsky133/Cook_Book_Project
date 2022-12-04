package edu.uca.innovatech.cookbook

import android.app.Application
import edu.uca.innovatech.cookbook.data.database.RecetasRoomDatabase

class CookBookApp : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        lateinit var application: Application
            private set
        //Se usa lazy para que solo sea init cuando se llame
        val database: RecetasRoomDatabase by lazy { RecetasRoomDatabase.getDatabase(application) }
    }
}