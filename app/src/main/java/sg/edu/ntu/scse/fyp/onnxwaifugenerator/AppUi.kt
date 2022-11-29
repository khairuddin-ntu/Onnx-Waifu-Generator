package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

private const val TAG = "AppUi"

/**
 * Main UI
 */
@Composable
fun AppUi() {
    val resources = LocalContext.current.resources

    val scope = rememberCoroutineScope()
    val (onnxGenerator, _) = remember { mutableStateOf(OnnxGenerator(resources)) }
    val (bitmap, setBitmap) = remember { mutableStateOf<Bitmap?>(null) }

    // Closes Onnx Generator when UI is destroyed
    DisposableEffect(onnxGenerator) {
        onDispose {
            onnxGenerator.close()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Button(onClick = {
            scope.launch {
                generateShape(onnxGenerator, setBitmap)
            }
        }) {
            Text(text = "Generate")
        }
        if (bitmap != null) {
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                bitmap = bitmap.asImageBitmap(),
                contentDescription = ""
            )
        }
    }
}

/**
 * Performs shape generation in a background thread
 *
 * @param onnxGenerator Object used to generate images from user input
 * @param setBitmap Function to update Bitmap in UI
 */
private suspend fun generateShape(onnxGenerator: OnnxGenerator, setBitmap: (Bitmap) -> Unit) =
    withContext(Dispatchers.Default) {
        val (modelOutput, shape) = onnxGenerator.generateImage(0, floatArrayOf(0f, 0f), 0f)

        Log.d(TAG, "generateShape: Output shape = ${shape.joinToString()}")
        Log.d(TAG, "generateShape: Converting model output to Bitmap")
        val imgWidth = shape[3].toInt()
        val imgHeight = shape[2].toInt()

        val minVal = modelOutput.minOrNull() ?: -1.0f
        val maxVal = modelOutput.maxOrNull() ?: 1.0f
        val delta = maxVal - minVal

        val pixels = IntArray(imgWidth * imgHeight * 4)
        for (i in 0 until imgWidth * imgHeight) {
            pixels[i] = Color.rgb(
                ((modelOutput[i] - minVal) / delta * 255.0f).roundToInt(),
                ((modelOutput[i + imgWidth * imgHeight] - minVal) / delta * 255.0f).roundToInt(),
                ((modelOutput[i + 2 * imgWidth * imgHeight] - minVal) / delta * 255.0f).roundToInt()
            )
        }

        val bitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, imgWidth, 0, 0, imgWidth, imgHeight)
        setBitmap(bitmap)

        // Save to file in app storage
//        val fileName = "model-output-${System.currentTimeMillis()}.png"
//        val file = File(applicationContext.filesDir, fileName)
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, file.outputStream())
//        Log.d(
//            TAG,
//            "generateShape: Bitmap stored in ${applicationContext.filesDir.absolutePath}/$fileName"
//        )
    }