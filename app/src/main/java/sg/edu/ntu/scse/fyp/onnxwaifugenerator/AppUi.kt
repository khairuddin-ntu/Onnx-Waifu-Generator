package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    val (trunc1, setTrunc1) = remember { mutableStateOf(0f) }
    val (trunc2, setTrunc2) = remember { mutableStateOf(0f) }

    val (image, setImage) = remember { mutableStateOf<Bitmap?>(null) }

    // Converts model output to Bitmap
    val modelToBitmap = { modelOutput: FloatArray, imgWidth: Int, imgHeight: Int ->
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
        bitmap
    }

    val generateShape: () -> Unit = {
        // Performs shape generation in a background thread
        scope.launch(Dispatchers.Default) {
            val (modelOutput, shape) = onnxGenerator.generateImage(
                0, floatArrayOf(trunc1, trunc2), 0f
            )

            Log.d(TAG, "generateShape: Output shape = ${shape.joinToString()}")
            val imgWidth = shape[3].toInt()
            val imgHeight = shape[2].toInt()

            val bitmap = modelToBitmap(modelOutput, imgWidth, imgHeight)
            setImage(bitmap)

            // Save to file in app storage
//        val fileName = "model-output-${System.currentTimeMillis()}.png"
//        val file = File(applicationContext.filesDir, fileName)
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, file.outputStream())
//        Log.d(
//            TAG,
//            "generateShape: Bitmap stored in ${applicationContext.filesDir.absolutePath}/$fileName"
//        )
        }
    }

    // Action(s) to perform when UI is destroyed
    DisposableEffect(onnxGenerator) {
        onDispose {
            onnxGenerator.close()
        }
    }

    // UI
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Row {
            Text("Truncation 1")
            Spacer(Modifier.weight(1f))
            Text(trunc1.toString())
        }
        Slider(
            value = trunc1,
            valueRange = 0f..2.0f,
            onValueChange = setTrunc1
        )
        Row {
            Text("Truncation 2")
            Spacer(Modifier.weight(1f))
            Text(trunc2.toString())
        }
        Slider(
            value = trunc2,
            valueRange = 0f..2.0f,
            onValueChange = setTrunc2
        )
        Button(onClick = generateShape) {
            Text(text = "Generate")
        }
        if (image != null) {
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                bitmap = image.asImageBitmap(),
                contentDescription = ""
            )
        }
    }
}
