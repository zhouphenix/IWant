package com.phenix.isix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.phenix.isix.data.I6Constant
import com.phenix.isix.data.dataStore
import com.phenix.isix.ui.AppNavGraph
import com.phenix.isix.ui.theme.ISixTheme
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ISixTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.systemBars) { innerPadding ->
                    Timber.d("innerPadding : $innerPadding")
                    I6App(modifier = Modifier.padding(innerPadding))
                }
            }
        }

        this.dataStore.data.map { prefs ->
            prefs[I6Constant.KEY_MOCK_ERR] ?: false
        }.onEach {
            Timber.i("onEach KEY_MOCK_ERR:  $it")
            if (it) {
                this.dataStore.edit { prefs -> prefs[I6Constant.KEY_MOCK_ERR] = false }
                throw RuntimeException("0000000000000")
            }
        }.launchIn(lifecycleScope)
    }
}

val LocalNavController =
    compositionLocalOf<NavHostController> { error("No NavController provided") }

@Composable
fun I6App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        AppNavGraph(navController, modifier)
    }


//    val ctx = LocalContext.current
//    val scope = rememberCoroutineScope()
//    Column {
//        Text(
//            text = "Hello $name!",
//            modifier = modifier
//        )
//
//        Button(onClick = {
//            scope.launch {
//                ctx.dataStore.edit { prefs -> prefs[I6Constant.KEY_MOCK_ERR] = true }
//            }
//        }) {
//            Text(text = "测试异常重启App")
//        }
//    }
}

@Preview(showBackground = true)
@Composable
fun I6AppPreview() {
    I6App()
}
