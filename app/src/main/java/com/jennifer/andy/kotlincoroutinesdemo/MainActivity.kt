package com.jennifer.andy.kotlincoroutinesdemo

import android.os.Bundle
import android.widget.Button
import kotlinx.coroutines.*


/**
 * Author:  andy.xwt
 * Date:    2019/1/14 11:30
 * Description:该activity所有的协程都运行在主协程中
 */

class MainActivity : ScopedAppActivity() {

    private var textView: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.tv_hello)
        textView?.setOnClickListener {
            asyncShowData()
        }
    }


    private fun asyncShowData() = launch {
        //运行在主协程的上下文中
        showIOData()
    }

    private suspend fun showIOData() {
        val deferred = async(Dispatchers.IO) {
            delay(2000)
            "hello kotlin coroutines"
        }
        //切换线程
        withContext(Dispatchers.Main) {
            val data = deferred.await()
            // Show data in UI
            textView?.setText(data)
        }

    }
}