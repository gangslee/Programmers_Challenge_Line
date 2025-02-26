package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.aboutDB.PostData
import com.example.myapplication.adapters.AddImgAdapter
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.write_actionbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class WriteActivity : AppCompatActivity() {

    private val dialogText = arrayOf("이미지 선택 & 촬영하기", "외부 이미지 선택하기")
    var imgDataList: ArrayList<String> = arrayListOf()
    private var imgAdapter: AddImgAdapter = AddImgAdapter(this, imgDataList)
    private var isNew : Boolean = true
    private var txtList : ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        isNew = intent.extras?.getBoolean("isNew") as Boolean
        if(!isNew){
            txtList = intent.extras?.getStringArrayList("txtList") as ArrayList<String>
            writeTitleText.setText(txtList[0], TextView.BufferType.EDITABLE)
            writeDetailText.setText(txtList[1], TextView.BufferType.EDITABLE)
            if(txtList[2]!="[]") {
                imgDataList.addAll(txtList[2].substring(1, txtList[2].length - 1).split(", "))
                imgAdapter.notifyDataSetChanged()
            }
        }
        //check intent is new writing or modify writing

        val toolbar: Toolbar = findViewById(R.id.write_actionBar)
        setSupportActionBar(toolbar)
        val actionBarTemp = supportActionBar
        actionBarTemp?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        //add custom actionbar

        val lm = LinearLayoutManager(this@WriteActivity)
        lm.orientation = LinearLayoutManager.HORIZONTAL
        imageList.layoutManager = lm
        imageList.setHasFixedSize(true)

        imgAdapter.btClick = object : AddImgAdapter.BtClick {
            override fun onClick(position: Int) {
                imgDataList.removeAt(position)
                imgAdapter.notifyDataSetChanged()
            }
        }
        imageList.adapter = imgAdapter
        //setting image list recyclerview & adapter

        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    toast("권한이 승인되었습니다.")
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    println(deniedPermissions)
                    toast("권한이 거부되었습니다.")
                }

            })
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.INTERNET
            )
            .check()
        //check got permission

        addImageBt.setOnClickListener { showDialogList() }

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        save_memo.setOnClickListener {
            if (TextUtils.isEmpty(writeTitleText.text) && TextUtils.isEmpty(writeDetailText.text))
                toast("내용이 입력되지 않았습니다.")
            else {
                val intentToMain = Intent(applicationContext, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                val currentTime: Date = Calendar.getInstance().time
                val formatTime: String =
                    SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분", Locale.getDefault()).format(
                        currentTime
                    )
                lifecycleScope.launch(Dispatchers.IO) {
                    if(isNew){
                        viewModel.insert(
                            PostData(
                                writeTitleText.text.toString(),
                                writeDetailText.text.toString(),
                                imgDataList.toString(),
                                formatTime
                            )
                        )
                    }
                    else{
                        viewModel.update(
                            writeTitleText.text.toString(),
                            writeDetailText.text.toString(),
                            imgDataList.toString(),
                            formatTime,
                            txtList[4].toInt()
                        )
                    }
                    startActivity(intentToMain)
                }
            }
        }
        //setting onClick event modify, update mode & insert mode
    }

    private fun showDialogList() {
        val builder = AlertDialog.Builder(this@WriteActivity)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.select_image_dialog, null)
        builder.setView(view)

        val dialogListView = view.findViewById<ListView>(R.id.image_dialog_list)
        val dialog = builder.create()

        val arrayAdapter =
            ArrayAdapter<String>(
                this@WriteActivity,
                R.layout.image_dialog_listview,
                R.id.alertDialogItemTextView,
                dialogText
            )
        dialogListView.adapter = arrayAdapter
        dialogListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position: Int, _ ->
                when (position) {
                    0 -> {
                        TedImagePicker.with(this)
                            .startMultiImage { uriList ->
                                for (i in uriList.indices) {
                                    imgDataList.add(uriList[i].toString())
                                }
                                imgAdapter.notifyDataSetChanged()
                            }
                    }
                    else -> showDialogInput()
                }
                dialog.dismiss()
            }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
    //create how-to image add dialog

    private fun showDialogInput() {
        val builder = AlertDialog.Builder(this@WriteActivity)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.input_url_dialog, null)

        builder.setView(view)

        val dialog = builder.create()

        val inputBts = listOf<Button>(
            view.findViewById(R.id.input_dialog_ok),
            view.findViewById(R.id.input_dialog_cancel)
        )
        inputBts[0].setOnClickListener {
            val inputUrl = view.findViewById<EditText>(R.id.input_imgae_url)
            val imgUrl: String = inputUrl.text.toString()
            val checkExt = imgUrl.substring(imgUrl.lastIndexOf(".")+1, imgUrl.length).toLowerCase(Locale.ENGLISH)
            if (URLUtil.isValidUrl(imgUrl) && (checkExt == "jpg" || checkExt == "png" || checkExt == "gif") ) {
                imgDataList.add(imgUrl)
                imgAdapter.notifyDataSetChanged()
            } else {
                toast("잘못된 이미지 주소를 입력하셨습니다!")
            }
            dialog.dismiss()
        }
        inputBts[1].setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
    //create input image url dialog

    override fun onBackPressed() {
        super.onBackPressed()
        toast("작성중인 메모가 저장되지 않았습니다.")
    }

    private fun Context.toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

}

