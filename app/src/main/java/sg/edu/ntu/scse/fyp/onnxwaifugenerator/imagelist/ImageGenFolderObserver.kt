package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagelist

import android.os.FileObserver
import java.io.File

class ImageGenFolderObserver(private val dir: File) : FileObserver(dir) {
    private var listener: ((List<File>) -> Unit)? = null

    private val files: List<File>
        get() = dir.listFiles()?.sorted() ?: emptyList()

    override fun onEvent(event: Int, path: String?) {
        if (event != CREATE) return
        listener?.invoke(files)
    }

    fun startListening(listener: (List<File>) -> Unit) {
        this.listener = listener
        listener(files)
        startWatching()
    }

    fun stopListening() {
        this.listener = null
        stopWatching()
    }
}
