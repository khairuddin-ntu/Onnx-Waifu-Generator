package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagelist

import android.os.FileObserver
import java.io.File

class ImageGenFolderObserver(private val dir: File) : FileObserver(dir) {
    private var listener: ((List<File>) -> Unit)? = null

    override fun onEvent(event: Int, path: String?) {
        if (event != CREATE) return
        listener?.invoke(dir.listFiles()?.sorted() ?: emptyList())
    }

    fun startListening(listener: (List<File>) -> Unit) {
        this.listener = listener
        startWatching()
    }

    fun stopListening() {
        this.listener = null
        stopWatching()
    }
}
