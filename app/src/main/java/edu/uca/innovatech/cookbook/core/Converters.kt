package edu.uca.innovatech.cookbook.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class Converters {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
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