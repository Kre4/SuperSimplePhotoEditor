package com.kre4.photodrawer

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var drawableImageView: DrawableImageView

    private var visibleBmp: Bitmap? = null

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val photoUri: Uri = result.data!!.data!!
                val bmpOptions = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                val inputStream = this.contentResolver.openInputStream(photoUri)
                val bmp = BitmapFactory
                    .decodeStream(
                        inputStream
                    )
                visibleBmp = bmp?.let {
                    Bitmap.createBitmap(
                        bmp.width,
                        bmp.height,
                        bmp.config
                    )
                }

                visibleBmp?.let {
                    drawableImageView.setImage(it, bmp)
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawableImageView = findViewById(R.id.drawableImageView)

        val pictureChooserButton: Button = findViewById(R.id.chooseBtn)
        pictureChooserButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val choosePictureIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                activityLauncher.launch(choosePictureIntent)
            }
        })

        val pictureSaverButton: Button = findViewById(R.id.saveBtn)
        pictureSaverButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                ImageSaver.saveImage(application.contentResolver, visibleBmp)
                Toast.makeText(this@MainActivity, "Image saved", Toast.LENGTH_LONG).show()
            }

        })


    }


}