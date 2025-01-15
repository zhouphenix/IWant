package com.phenix.isix.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.phenix.eventbus.EventBus
import com.phenix.isix.ui.screen.UserLoggedInEvent
import com.phenix.isix.ui.screen.UserLoggedOutEvent
import kotlinx.coroutines.Job
import timber.log.Timber

@Composable
fun LoginComponent(channel: String = "user_events", content: @Composable (String?) -> Unit ) {
    // 定义一个可变的状态来保存用户登录状态
    var loggedInUser by remember { mutableStateOf<String?>(null) }

    // 使用 LaunchedEffect 来启动协程并订阅事件
    val job = remember { mutableStateOf<Job?>(null) }

    DisposableEffect(key1 = channel) {
        Timber.i("LoginComponent $channel $this")
        job.value = EventBus.subscribe(channel = channel) { event ->
            when (event) {
                is UserLoggedInEvent -> loggedInUser = event.userId
                is UserLoggedOutEvent -> loggedInUser = null
            }
        }

        onDispose {
            job.value?.cancel()
        }
    }

    content(loggedInUser)
}