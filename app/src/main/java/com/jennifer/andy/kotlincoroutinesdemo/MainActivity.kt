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

        /***
         * 如果异常没有被处理(比如try catch)，而且 CoroutineContext 没有一个 CoroutineExceptionHandler 时，
         * 异常会到达默认线程的 ExceptionHandler。在 JVM 中，异常会被打印在控制台；
         * 而在 Android 中，无论异常在那个 Dispatcher 中发生，都会导致您的应用崩溃。
         */
//        val result1 = async(Dispatchers.IO) {
//            throw RuntimeException("hehehe")
//            "you"
//        }
//
//        val result2 = async(Dispatchers.IO) {
//            println("fuck")
//            "fuck"
//        }
//
//        //使用SupervisorJob 或者使用 supervisorScope 到子协程出现异常的时候，不会取消其他协程。
//        try {
//            val data1 = result1.await()
//            val data2 = result2.await()
//
//            textView?.text = data1 + data2
//
//        } catch (e: Exception) {
//            println("捕获到异常")
//
//        }

        //使用 job 或者使用 coroutineScope 时，如果子协程出现异常，那么会取消其他协程，同时取消自己，并将异常向上传递
//        coroutineScope {
//
//            launch {
//                throw RuntimeException("我是 coroutineScope 异常")
//            }
//            launch {
//                println("我是coroutineScope")
//            }
//        }

        //如果在创建协程的时候，该异常不会被捕获，但是整个协程作用域下的协程都会被取消，
        // 异常不会被捕获的原因是因为 handler 没有被安装给正确的 CoroutineContext。内部协程会在异常出现时传播异常并传递给它的父级，
        // 由于父级并不知道 handler 的存在，异常就没有被抛出。

        //异常捕获的条件
        //1.异常是被自动抛出异常的协程所抛出的 (使用 launch，而不是 async 时)；
        //2.CoroutineScope 的 CoroutineContext 中或在一个根协程 (CoroutineScope 或者 supervisorScope 的直接子协程) 中。

        //例子1,该异常不会被捕获，因为不满足条件1
//        coroutineScope {
//
//            launch (CoroutineExceptionHandler { context, exception ->
//                println(exception.message)
//            }){
//                throw RuntimeException("我是 coroutineScope 异常")
//            }
//
//            launch {
//                println("我是coroutineScope")
//            }
//        }

        //例子2，该异常不会被捕获，因为不满足条件2，因为 coroutineScope 内部创建了一个根协程，但是并没有增加CoroutineExceptionHandler
        coroutineScope {
            launch(CoroutineExceptionHandler { context, exception -> println(exception.message) }) {

                launch {
                    throw RuntimeException("我是 coroutineScope 异常1")
                }

                launch {
                    println("我是coroutineScope1")
                }
            }

        }

        //例子3，异常会被捕获,因为满足1，2条件
//        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main + CoroutineExceptionHandler { context, exception ->
//            println(exception.message)
//        })
//        scope.launch {
//            launch {
//                throw RuntimeException("我是 coroutineScope 异常2")
//            }
//
//            launch {
//                println("我是coroutineScope2")
//            }
//        }

        //例子4，异常会被捕获,因为满足1，2条件
//        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
//        scope.launch(CoroutineExceptionHandler { context, exception ->
//            println(exception.message)
//        }) {
//            launch {
//                throw RuntimeException("我是 coroutineScope 异常3")
//            }
//
//            launch {
//                println("我是coroutineScope3")
//            }
//        }


    }
}