package edu.uca.innovatech.cookbook.core

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.OutputStream
import android.content.ContentResolver
import android.os.Environment
import java.io.File
import java.lang.Exception
import java.util.*

class ToAppFiles {

    fun guardarAGaleria(bitmap: Bitmap, nombreReceta: String, contentResolver: ContentResolver?): Uri? {
        val fos: OutputStream
        try {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
                val resolver = contentResolver
                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_$nombreReceta.jpg")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+ File.separator+"CookBookRecipes")
                val imageUri = resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = resolver?.openOutputStream(Objects.requireNonNull(imageUri)!!)!!
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                Objects.requireNonNull<OutputStream?>(fos)
                println("Se guardo la mierda creo yo")

                return imageUri
            }

        } catch (e: Exception){
            println("No se guardo la mierda")
        }
        return null
    }

}