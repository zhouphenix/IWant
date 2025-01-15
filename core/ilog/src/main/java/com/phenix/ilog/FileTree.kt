package com.phenix.ilog

import android.os.Process
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileTree(
    private val logDir: File,
    private val config: Configuration,
) : IDebugTree() {

    private val mLogFileNameFormatter =
        SimpleDateFormat(config.logFileNamePattern, Locale.getDefault())

    private var mBufferedWriter: BufferedWriter

    init {
        if (!logDir.exists() && !logDir.mkdirs()) {
            error("Failed to create folder ${logDir.path}")
        }
        val (_, logName, _, maxFileSize) = config
        val logFile = logDir.resolve(logName)
        if (logFile.exists() && logFile.length() > maxFileSize) {
            val newLogFile = logDir.resolve("log_${mLogFileNameFormatter.format(Date())}")
            logFile.renameTo(newLogFile)
        }
        mBufferedWriter = BufferedWriter(FileWriter(logFile, true))
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        val (packageName, logName, _, maxFileSize) = config
        val logFile = logDir.resolve(logName)
        if (logFile.exists() && logFile.length() > maxFileSize) {
            mBufferedWriter.run {
                flush()
                close()
            }
            val newLogFile = logDir.resolve("log_${mLogFileNameFormatter.format(Date())}")
            if (!logFile.renameTo(newLogFile)) {
                Log.w(TAG, "Failed to rename to ${newLogFile.path} ")
            }
            mBufferedWriter = BufferedWriter(FileWriter(logFile, true))
        }
        // log template: [header][formatMsg]
        val header =
            "${FORMAT_HEADER_TIME.format(Date())} ${Process.myPid()}-${Process.myTid()}  ${
                abbreviateString(
                    tag ?: ""
                )
            } ${abbreviateString(packageName, 35)} ${getLogLevel(priority)} "

        // format message
        val formatMsg = message.replace(Regex("\\n"), "\n%-${header.length}s".format(""))
        mBufferedWriter.run {
            write(header)
            write(getLink())
            write(formatMsg)
            newLine()
            flush()
        }
    }

    companion object {
        private const val TAG = "FileTree"

        @JvmStatic
        private val FORMAT_HEADER_TIME = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH)

        private fun getLogLevel(priority: Int): String = when (priority) {
            Log.INFO -> "I"
            Log.VERBOSE -> "V"
            Log.DEBUG -> "D"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            Log.ASSERT -> "A"
            else -> "U"
        }

        fun abbreviateString(str: String, maxLength: Int = 23): String =
            if (str.length > maxLength) {
                val firstPart = str.substring(0, maxLength / 2 - 1)
                val lastPart = str.substring(str.length - maxLength / 2 + 1)
                "$firstPart...$lastPart"
            } else {
                "%-${maxLength}s".format(str)
            }
    }
}