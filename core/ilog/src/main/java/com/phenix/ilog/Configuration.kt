package com.phenix.ilog

data class Configuration(
    val packageName: String,
    var logName: String = "ilog",
    var logFileNamePattern: String = "yyyyMMdd_HH:mm",
    var maxFileSize: Long = 10 * 1024 * 1024,
)
