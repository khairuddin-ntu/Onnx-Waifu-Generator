package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagelist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagerepo.ImageRepository
import java.io.File

class ImageListViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = ImageRepository.getDatabase(app).imageListDao()

    private val images = Channel<List<File>>()
    val imageList = images.receiveAsFlow()

    private val daoJob: Job = dao.getAllImagePaths()
        .map { it.map(::File) }
        .onEach(images::send)
        .launchIn(viewModelScope)

    override fun onCleared() {
        daoJob.cancel()
        images.close()
    }
}
