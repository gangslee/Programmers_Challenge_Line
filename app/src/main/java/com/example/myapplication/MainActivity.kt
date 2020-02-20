package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.aboutDB.PostData
import com.example.myapplication.adapters.AddMemoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

  private var writeIntent = Intent()
  private var memoFormatList : ArrayList<PostData> = arrayListOf()
  private var memoAdapter : AddMemoAdapter = AddMemoAdapter(this, memoFormatList)
  private var detailTextList : ArrayList<String> = arrayListOf()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val lm = LinearLayoutManager(this@MainActivity)
    memoList.layoutManager = lm
    memoList.setHasFixedSize(true)
    memoList.adapter = memoAdapter
    memoAdapter.memoClick = object : AddMemoAdapter.MemoClick{
      override fun onClick(position: Int) {
        detailTextList.clear()
        detailTextList
          .addAll(arrayListOf(memoFormatList[position].title, memoFormatList[position].content,
            memoFormatList[position].imgList, memoFormatList[position].date, memoFormatList[position].id.toString()))

        val intentToDetail = Intent(applicationContext, DetailActivity::class.java)
        intentToDetail.putExtra("txtList", detailTextList)
        startActivity(intentToDetail)
      }

    }

    val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    viewModel.getAll().observe(this, Observer {
      memoFormatList.addAll(it)
      memoAdapter.notifyDataSetChanged()
    })

    fab.setOnClickListener {
      writeIntent = Intent(applicationContext, WriteActivity::class.java)
      startActivity(writeIntent)
    }
  }

  private fun Context.toast(message:String){
    Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
  }

}