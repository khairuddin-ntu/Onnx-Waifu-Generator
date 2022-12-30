package sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

private const val TAG = "ImageGenerationService"

class ImageGenerationService : Service() {
    override fun onCreate() {
        Log.d(TAG, "++onCreate++")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "++onStartCommand++")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        Log.d(TAG, "++onDestroy++")
        super.onDestroy()
    }
}