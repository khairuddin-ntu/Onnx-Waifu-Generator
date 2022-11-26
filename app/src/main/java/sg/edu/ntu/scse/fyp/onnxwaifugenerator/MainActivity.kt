package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.ui.theme.OnnxWaifuGeneratorTheme
import java.io.File

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
        val (modelOutput, shape) = onnxGenerator.generateImage(0, floatArrayOf(0f, 0f), 0f)

        Log.d(TAG, "generateShape: Output shape = ${shape.joinToString()}")
        Log.d(TAG, "generateShape: Converting model output to Bitmap")
        val imgWidth = shape[3].toInt()
        val imgHeight = shape[2].toInt()
        val bitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.RGBA_F16)
        bitmap.copyPixelsFromBuffer(modelOutput)

        val fileName = "model-output-${System.currentTimeMillis()}.png"
        val file = File(applicationContext.filesDir, fileName)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, file.outputStream())
        Log.d(
            TAG,
            "generateShape: Bitmap stored in ${applicationContext.filesDir.absolutePath}/$fileName"
        )
    }
}
