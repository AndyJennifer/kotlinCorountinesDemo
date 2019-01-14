package com.jennifer.andy.kotlincoroutinesdemo

import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel


/**
 * Author:  andy.xwt
 * Date:    2019/1/14 11:29
 * Description:
 */

//构建主协程的上下文
abstract class ScopedAppActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}