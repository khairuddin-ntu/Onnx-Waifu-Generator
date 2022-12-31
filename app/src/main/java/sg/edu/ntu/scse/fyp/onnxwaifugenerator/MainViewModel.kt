package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import android.app.Application
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.*
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration.ImageGenerationService
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration.OnnxController
import java.io.File

private const val TAG = "MainViewModel"

class MainViewModel(app: Application) : AndroidViewModel(app) {
    var isGenerating by mutableStateOf(false)
        private set

    var imageList by mutableStateOf<List<File>>(emptyList())
        private set

    private val onnxController = OnnxController(app.resources)
    private val imageListDir = File(app.filesDir, "generated_images")

    init {
        if (!imageListDir.exists()) {
            imageListDir.mkdir()
        }

        updateImageList()
    }

    override fun onCleared() {
        onnxController.close()
    }

    fun generateImage(
        modelType: OnnxModel, seed: Int, psi: FloatArray, noise: Float
    ) {
        val context = getApplication<Application>()
        context.startForegroundService(
            Intent(context, ImageGenerationService::class.java)
                .putExtra(KEY_MODEL, modelType.name)
                .putExtra(KEY_SEED, seed)
                .putExtra(KEY_TRUNCATIONS, psi)
                .putExtra(KEY_NOISE, noise)
        )

        isGenerating = true
    }

    fun onImageGenerated() {
        updateImageList()
        isGenerating = false
    }

    private fun updateImageList() {
        imageList = imageListDir.listFiles()?.sorted() ?: emptyList()
    }
}
