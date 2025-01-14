package com.phenix.isix.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.phenix.eventbus.EventBus
import kotlinx.coroutines.launch

@Composable
fun Login() {
    val coroutineScope = rememberCoroutineScope()

    Column {
        var userId by remember { mutableStateOf("123") }

        Button(onClick = {
            coroutineScope.launch {
                EventBus.post(UserLoggedInEvent("user_events", userId))
            }
        }) {
            Text("post ->> Login User")
        }

        Button(onClick = {
            EventBus.tryPost(UserLoggedOutEvent("user_events", userId))
        }) {
            Text("tryPost ->> Logout User")
        }

        // 模拟更改用户 ID
        TextField(value = userId, onValueChange = { userId = it })
    }
}