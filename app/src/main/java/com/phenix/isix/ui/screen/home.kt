package com.phenix.isix.ui.screen

import android.content.Intent
import android.os.Build
import android.view.WindowInsets
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.phenix.isix.JavaMainActivity
import com.phenix.isix.LocalNavController
import com.phenix.isix.ui.LoginComponent

@Composable
fun HomeScreen() {
    val navController = LocalNavController.current
    val view = LocalView.current
    val ctx = LocalContext.current

    Column {
        Text("This is the home screen")
        Button(onClick = {
            navController.navigate("details")
        }) {
            Text("Go to Detail")
        }
        Button(onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                view.windowInsetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            }
        }) {
            Text("Toggle to full.")
        }
        Button(onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                view.windowInsetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            }
        }) {
            Text("Exit full.")
        }
        Button(onClick = {
            ctx.startActivity(Intent(ctx, JavaMainActivity::class.java))
        }) {
            Text("To JavaMainActivity.")
        }

        HorizontalDivider()

        LoginComponent {
            it?.let {
                Text("Logged in user: $it")
            } ?: Text("No user logged in")
        }
    }

}