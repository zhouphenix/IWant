package com.phenix.isix.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.phenix.isix.LocalNavController
import com.phenix.isix.ui.LoginComponent

@Composable
fun OtherScreen (modifier: Modifier = Modifier) {

    val navController = LocalNavController.current

    Column(modifier = modifier) {
        Text("This is the other screen")
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Go to back")
        }

        Button(onClick = {
            navController.navigate("other2")
        }) {
            Text("Go to Next")
        }

        HorizontalDivider()

        LoginComponent { loggedInUser ->
            // 显示当前登录用户的状态
            if (loggedInUser != null) {
                Text("Logged in user: $loggedInUser")
            } else {
                Text("No user logged in")
            }
        }
    }
}