package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.roundToInt

private const val TAG = "MainViewModel"

class MainViewModel(app: Application) : AndroidViewModel(app) {
    var isGenerating by mutableStateOf(false)
        private set

    var imageList by mutableStateOf<List<File>?>(null)
        private set

    private val onnxController = OnnxController(app.resources)
    private val imageListDir = File(app.filesDir, "generated_images")

    init {
        if (!imageListDir.exists()) {
            imageListDir.mkdir()
        }

        imageList = imageListDir.listFiles()?.sorted()
    }

    override fun onCleared() {
        onnxController.close()
    }

    fun generateImage(
        modelType: OnnxModel, seed: Int, psi: FloatArray, noise: Float
    ) {
        isGenerating = true

        // Performs shape generation in a background thread
        viewModelScope.launch(Dispatchers.Default) {
            val (modelOutput, shape) = onnxController.generateImage(modelType, seed, psi, noise)

            Log.d(TAG, "generateShape: Output shape = ${shape.joinToString()}")
            val imgWidth = shape[3].toInt()
            val imgHeight = shape[2].toInt()
            val bitmap = convertModelToBitmap(modelOutput, imgWidth, imgHeight)

            // Save to file in app storage
            withContext(Dispatchers.IO) {
                val file = File(imageListDir, "model-output-${System.currentTimeMillis()}.png")
                val fileOutputStream = file.outputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.close()
                Log.d(TAG, "generateShape: Bitmap stored in ${file.absolutePath}")
            }

            bitmap.recycle()

            withContext(Dispatchers.Main) {
                imageList = imageListDir.listFiles()?.sorted()
                isGenerating = false
            }
        }
    }

    private fun convertModelToBitmap(
        modelOutput: FloatArray,
        imgWidth: Int, imgHeight: Int
    ): Bitmap {
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
        return bitmap
    }
}