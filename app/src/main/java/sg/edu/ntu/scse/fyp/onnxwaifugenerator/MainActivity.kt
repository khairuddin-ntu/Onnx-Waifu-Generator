package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration.ImageGenerationService
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.ui.theme.OnnxWaifuGeneratorTheme

class MainActivity : ComponentActivity() {
    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        serviceIntent = Intent(this, ImageGenerationService::class.java)
        // TODO: Change to foreground service
        startService(serviceIntent)

        setContent {
            OnnxWaifuGeneratorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainUi()
                }
            }
        }
    }

    override fun onDestroy() {
        stopService(serviceIntent)
        super.onDestroy()
    }
}
