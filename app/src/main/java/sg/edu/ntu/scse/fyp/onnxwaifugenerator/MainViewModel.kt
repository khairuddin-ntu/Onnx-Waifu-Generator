package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import android.app.Application
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.*
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration.ImageGenerationService
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration.OnnxController
import java.io.File

private const val TAG = "MainViewModel"

class MainViewModel(app: Application) : AndroidViewModel(app) {
    var isGenerating by mutableStateOf(false)
        private set

    private val images = Channel<List<File>>()
    val imageList = images.receiveAsFlow()

    private val onnxController = OnnxController(app.resources)
    private val imageListDir = File(app.filesDir, "generated_images")

    init {
        if (!imageListDir.exists()) {
            imageListDir.mkdir()
        }

        updateImageList()
    }

    override fun onCleared() {
        images.close()
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
        viewModelScope.launch { images.send(imageListDir.listFiles()?.sorted() ?: emptyList()) }
    }
}
