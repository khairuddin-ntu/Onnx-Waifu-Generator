package sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.ACTION_SERVICE_RESPONSE

@Composable
fun ImageGenerationReceiver(onImageGenerated: () -> Unit) {
    val context = LocalContext.current

    val notifyUi by rememberUpdatedState(onImageGenerated)

    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                notifyUi()
            }
        }

        context.registerReceiver(receiver, IntentFilter(ACTION_SERVICE_RESPONSE))

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
}