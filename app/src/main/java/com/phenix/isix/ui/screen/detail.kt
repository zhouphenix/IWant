package com.phenix.isix.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.phenix.eventbus.IEvent
import com.phenix.isix.LocalNavController
import com.phenix.isix.ui.LoginComponent

// 具体的事件类型
data class UserLoggedInEvent(override val channel: String, val userId: String) : IEvent
data class UserLoggedOutEvent(override val channel: String, val userId: String) : IEvent

@Composable
fun DetailsScreen(modifier: Modifier = Modifier) {
    val navController = LocalNavController.current

    Column(modifier = modifier) {
        Text("This is the details screen")
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Go to back")
        }
        Button(onClick = {
            navController.navigate("other")
        }) {
            Text("Go to Next")
        }
        HorizontalDivider()
        Text("测试EventBus -> post")

        LoginComponent { loggedInUser ->
            // 显示当前登录用户的状态
            if (loggedInUser != null) {
                Text("Logged in user: $loggedInUser")
            } else {
                Text("No user logged in")
            }
        }
        Login()
    }
}


