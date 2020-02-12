package com.example.myapplication

import android.R.attr
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.write_actionbar.*


class WriteActivity : AppCompatActivity() {
    val PICTURE_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        val toolbar : Toolbar = findViewById(R.id.writeActionBar)
        setSupportActionBar(toolbar)
        val actionBarTemp = supportActionBar
        actionBarTemp?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        addImageBt.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            galleryIntent.type = "image/*"
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICTURE_REQUEST_CODE)
        }

        saveMemo.setOnClickListener {
            toast("123")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICTURE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                val uri = data?.data
                val clipData = data?.clipData
                toast("${clipData?.itemCount}")
            }
        }


    }

    private fun Context.toast(message:String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }
}

