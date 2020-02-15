package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapters.AddImgAdapter
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.write_actionbar.*


class WriteActivity : AppCompatActivity() {

    private val dialogText = arrayOf("이미지 선택 & 촬영하기", "외부 이미지 선택하기")
    var imgDataList : ArrayList<String>? = null
    private var imgAdapter : AddImgAdapter? = null


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

        val lm = LinearLayoutManager(this@WriteActivity)
        lm.orientation = LinearLayoutManager.HORIZONTAL
        imageList.layoutManager = lm
        imageList.setHasFixedSize(true)

        imgDataList = arrayListOf()
        imgAdapter = AddImgAdapter(this, imgDataList as ArrayList<String>)
        imageList.adapter = imgAdapter

        TedPermission.with(this)
                .setPermissionListener(object : PermissionListener{
                    override fun onPermissionGranted() { toast("권한이 승인되었습니다.") }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        println(deniedPermissions)
                        toast("권한이 거부되었습니다.")
                    }

                })
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.INTERNET)
                .check()

        addImageBt.setOnClickListener { showDialogList() }
        saveMemo.setOnClickListener { toast("123") }
    }


    private fun showDialogList(){
        val builder = AlertDialog.Builder(this@WriteActivity)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.select_image_dialog, null)
        builder.setView(view)

        val dialogListView = view.findViewById<ListView>(R.id.image_dialog_list)
        val dialog = builder.create()

        val arrayAdapter =
            ArrayAdapter<String>(this@WriteActivity,R.layout.image_dialog_listview,R.id.alertDialogItemTextView, dialogText)
        dialogListView.adapter = arrayAdapter
        dialogListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position: Int, _ ->
            when(position){
                0 -> {
                    TedImagePicker.with(this)
                            .startMultiImage { uriList ->
                                for(i in uriList.indices){
                                    imgDataList?.add(uriList[i].toString())
                                }
                                imgAdapter!!.notifyDataSetChanged()
                            }
                }
                else -> showDialogInput()
            }
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showDialogInput(){
        val builder = AlertDialog.Builder(this@WriteActivity)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.input_url_dialog, null)

        builder.setView(view)

        val dialog = builder.create()

        val inputBts = listOf<Button>(view.findViewById(R.id.input_dialog_ok), view.findViewById(R.id.input_dialog_cancel))
        inputBts[0].setOnClickListener {
            val inputUrl = view.findViewById<EditText>(R.id.input_imgae_url)
            val imgUrl : String = inputUrl.text.toString()
            if(URLUtil.isValidUrl(imgUrl)){
                imgDataList?.add(imgUrl)
                imgAdapter!!.notifyDataSetChanged()
            }
            else{
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


    private fun Context.toast(message:String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

}

