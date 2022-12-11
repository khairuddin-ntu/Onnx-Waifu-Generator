package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

private const val TAG = "MainViewModel"

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val onnxController = OnnxController(app.resources)

    override fun onCleared() {
        onnxController.close()
    }

    suspend fun generateShape(
        modelType: OnnxModel, seed: Int, psi: FloatArray, noise: Float
    ) = withContext(Dispatchers.Default) {
        val (modelOutput, shape) = onnxController.generateImage(modelType, seed, psi, noise)

        Log.d(TAG, "generateShape: Output shape = ${shape.joinToString()}")
        val imgWidth = shape[3].toInt()
        val imgHeight = shape[2].toInt()

        convertModelToBitmap(modelOutput, imgWidth, imgHeight)
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