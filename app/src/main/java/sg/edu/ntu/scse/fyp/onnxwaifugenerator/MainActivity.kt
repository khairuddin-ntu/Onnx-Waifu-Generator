package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.ui.theme.OnnxWaifuGeneratorTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    /**
     * Object used to generate images from user input.
     *
     * lateinit = variable initialized after class instantiation
     */
    private lateinit var onnxGenerator: OnnxGenerator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // OnnxGenerator initialized here because resources only accessible during and after
        // onCreate() is called
        onnxGenerator = OnnxGenerator(resources)

        setContent {
            val scope = rememberCoroutineScope()

            OnnxWaifuGeneratorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppUi {
                        scope.launch {
                            generateShape()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        onnxGenerator.close()
        super.onDestroy()
    }

    private suspend fun generateShape() = withContext(Dispatchers.Unconfined) {
        onnxGenerator.generateImage(0, floatArrayOf(0f, 0f), 0f)
    }
}
