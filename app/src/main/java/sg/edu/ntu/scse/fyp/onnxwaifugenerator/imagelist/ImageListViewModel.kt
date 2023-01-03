package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.FOLDER_GENERATES_IMAGES
import java.io.File

class ImageListViewModel(app: Application) : AndroidViewModel(app) {
    private val images: Channel<List<File>>
    val imageList: Flow<List<File>>

    private val fileObserver: ImageGenFolderObserver

    init {
        val imageListDir = File(app.filesDir, FOLDER_GENERATES_IMAGES)
        if (!imageListDir.exists()) {
            imageListDir.mkdir()
        }

        images = Channel()
        imageList = images.receiveAsFlow()
        fileObserver = ImageGenFolderObserver(imageListDir)
        fileObserver.startListening(this::updateImageList)
    }

    override fun onCleared() {
        fileObserver.stopListening()
        images.close()
    }

    private fun updateImageList(list: List<File>) {
        viewModelScope.launch { images.send(list) }
    }
}
