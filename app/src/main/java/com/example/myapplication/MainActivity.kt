package com.example.myapplication


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.aboutDB.PostData
import com.example.myapplication.adapters.AddMemoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

  private var memoFormatList : ArrayList<PostData> = arrayListOf()
  private var memoAdapter : AddMemoAdapter = AddMemoAdapter(this, memoFormatList)
  private var detailTextList : ArrayList<String> = arrayListOf()
  private fun selector(l : PostData) : String = l.date

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val toolbar: Toolbar = findViewById(R.id.main_actionBar)
    setSupportActionBar(toolbar)
    val actionBarTemp = supportActionBar
    actionBarTemp?.apply {
      setDisplayShowCustomEnabled(true)
      setDisplayShowTitleEnabled(false)
    }
    //add custom actionbar

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
    //setting memo list recyclerview & adapter

    val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    viewModel.getAll().observe(this, Observer {
      memoFormatList.addAll(it)
      sorting()
      memoAdapter.notifyDataSetChanged()
    })
    //load datas from local db

    fab.setOnClickListener {
      val writeIntent = Intent(applicationContext, WriteActivity::class.java)
      writeIntent.putExtra("isNew", true)
      startActivity(writeIntent)
    }
    //move to write page
  }

  private fun sorting(){
    memoFormatList.sortBy {selector(it)}
    memoFormatList.reverse()
  }
  //sorting list bt modified time

}