package com.kre4.photodrawer

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.OutputStream
import kotlin.random.Random

class ImageSaver {
    companion object {
        fun saveImage(contentResolver: ContentResolver, bmp: Bitmap?) {
            val fileName = "Drawer" + Random.nextInt(Int.MAX_VALUE)
            try {
                var fos: OutputStream? = null
                var imageUri: Uri? = null
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(
                            MediaStore.MediaColumns.RELATIVE_PATH,
                            Environment.DIRECTORY_PICTURES
                        )
                        put(MediaStore.MediaColumns.IS_PENDING, 1)
                    }
                }

                contentResolver.also { resolver ->
                    imageUri = resolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )
                    fos = imageUri?.let { resolver.openOutputStream(it) }
                }
                fos?.use { bmp?.compress(Bitmap.CompressFormat.JPEG, 70, it) }
                contentValues.clear()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
                }
                contentResolver.update(imageUri!!, contentValues, null, null)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}