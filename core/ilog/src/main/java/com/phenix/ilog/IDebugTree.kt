package com.phenix.ilog

import timber.log.Timber

open class IDebugTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        println("log>> $message -- $t")
        super.log(priority, tag, getLink() + message, t)
    }

    companion object {
        fun getLink(): String {
            val stackTrace = Thread.currentThread().stackTrace
            val index = stackTrace.indexOfLast { it.className.startsWith("timber.log.Timber$") }
            val targetElement = stackTrace[index + 1]
            val methodName = targetElement.methodName
            val lineNumber = targetElement.lineNumber
            val fileName = targetElement.fileName
            // link pattern： fileName:lineNumber
            val link = "[($fileName:$lineNumber)#$methodName-${Thread.currentThread()}]"
            return link
        }
    }
}