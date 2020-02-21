package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_actionbar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList

class DetailActivity : AppCompatActivity() {

    private var vpImgList : ArrayList<String> = arrayListOf()
    private var txtList : ArrayList<String> = arrayListOf()
    private var vpAdapter : ViewPagerAdapter = ViewPagerAdapter(this, vpImgList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar : Toolbar = findViewById(R.id.detail_actionBar)
        setSupportActionBar(toolbar)
        val actionBarTemp = supportActionBar
        actionBarTemp?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        txtList.addAll(intent.extras?.getStringArrayList("txtList") as ArrayList<String>)
        detail_title.text = txtList[0]
        detail_content.text = txtList[1]
        detail_date.text = txtList[3]

        vpImgList.addAll(txtList[2].substring(1, txtList[2].length-1).split(", "))
        vpAdapter.notifyDataSetChanged()
        viewpager_detail.adapter = vpAdapter
        if(vpImgList[0]!="") tab_layout.setupWithViewPager(viewpager_detail, true)

        modify_Memo.setOnClickListener {
            val intentToModify = Intent(applicationContext, WriteActivity::class.java)
            intentToModify.putExtra("txtList", txtList).putExtra("isNew", false)
            startActivity(intentToModify)
        }

        delete_Memo.setOnClickListener {
            showDialogDelete()
        }
    }

    private fun showDialogDelete(){
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val builder = AlertDialog.Builder(this@DetailActivity)
        builder.setTitle("메모 삭제")
            .setMessage("해당 메모를 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val intentToMain = Intent(applicationContext, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    viewModel.delete(txtList[4].toInt())
                    startActivity(intentToMain)
                }
            }
            .setNegativeButton("취소"
            ) { dialog, _ -> dialog?.dismiss() }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun Context.toast(message:String){
        Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
    }
}
