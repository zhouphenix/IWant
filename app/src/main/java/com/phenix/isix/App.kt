package com.phenix.isix

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.phenix.ilog.Configuration
import com.phenix.ilog.FileTree
import com.phenix.ilog.IDebugTree
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import kotlin.system.exitProcess

class App : Application(), LifecycleEventObserver {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(
            if (BuildConfig.DEBUG)
                FileTree(cacheDir.resolve("logs"), Configuration(packageName))
            else
                IDebugTree()
        )

        Thread.setDefaultUncaughtExceptionHandler(this::handleUncaughtException)

        Timber.v("┌——————————————————————————————————————————————————————————————————————┐")
        Timber.v("|>> $packageName onCreate...")
        Timber.i(
            """
            BUILD_TYPE: ${BuildConfig.BUILD_TYPE}
            DEBUG: ${BuildConfig.DEBUG}
            VERSION_CODE: ${BuildConfig.VERSION_CODE}
            VERSION_NAME: ${BuildConfig.VERSION_NAME}
            APPLICATION_ID: ${BuildConfig.APPLICATION_ID}
            """
        )
        Timber.v("└——————————————————————————————————————————————————————————————————————┘")

        Timber.e(RuntimeException("测试log level E"))
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    // TODO imber.e("Uncaught Exception in thread $thread", exception) 不打印exception堆栈
    private fun handleUncaughtException(thread: Thread, exception: Throwable) {
        // 1. 记录日志
        Timber.e(exception, "Uncaught Exception in thread $thread")

        // 2. 显示自定义的错误提示
        if (Looper.getMainLooper().thread == thread) {
            // 如果是在主线程发生的异常，显示 Toast 提示
            Toast.makeText(this, "应用发生错误，即将退出...", Toast.LENGTH_LONG).show()
        } else {
            // 如果是在子线程发生的异常，可以在这里处理
            Timber.e("Exception occurred in background thread")
        }

        // 3. 上传错误报告（可选）
        uploadErrorReport(exception)

        // 4. 退出应用
        //exitApp()

        // 5. restart
        restartApp()
    }

    private fun uploadErrorReport(exception: Throwable) {
        // 你可以在这里实现将错误报告上传到服务器的逻辑
        // 例如，使用 Firebase Crashlytics 或其他第三方服务
        Timber.d("Uploading error report...$exception")
    }

    private fun exitApp() {
        Timber.d("exitApp...")
        // 你可以选择退出应用，或者重启应用
        // 这里我们简单地结束所有活动并退出进程
        exitProcess(10)
    }

    private fun restartApp() {
        // 获取当前应用的包名
        val packageName = this.packageName

        // 创建一个 Intent，用于启动应用的主 Activity
        val restartIntent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            // 清除任务栈，确保应用从头开始启动
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (restartIntent != null) {
            // 使用 AlarmManager 延迟 2 秒后启动应用
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                restartIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
            )

            // 设置延迟 2 秒后启动应用
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, pendingIntent)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Timber.i("Process is terminated.")
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        Timber.i(">> event: $event")
        if (event == Lifecycle.Event.ON_START) {
            Timber.i("App enters the front-end.")
        } else if (event == Lifecycle.Event.ON_STOP) {
            Timber.i("App enters the backend.")
        }
    }

    companion object {
        // 定义 applicationScope
        val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    }
}