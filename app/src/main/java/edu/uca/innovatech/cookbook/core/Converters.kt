package edu.uca.innovatech.cookbook.core

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.room.TypeConverter
import coil.api.load
import edu.uca.innovatech.cookbook.R
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        println(outputStream.toByteArray().toString())
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        if (BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size) == null) {
            val conf = Bitmap.Config.ARGB_8888
            return Bitmap.createBitmap(1284, 1247, conf)
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}