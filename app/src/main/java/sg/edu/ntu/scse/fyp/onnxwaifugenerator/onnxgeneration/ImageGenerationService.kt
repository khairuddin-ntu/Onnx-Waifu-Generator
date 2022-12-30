package sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.*

private const val TAG = "ImageGenerationService"

class ImageGenerationService : Service() {
    override fun onCreate() {
        Log.d(TAG, "++onCreate++")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "++onStartCommand++")

        val modelName = intent?.getStringExtra(KEY_MODEL) ?: OnnxModel.SKYTNT.name
        val seed = intent?.getIntExtra(KEY_SEED, 0)
        val truncations = intent?.getFloatArrayExtra(KEY_TRUNCATIONS) ?: floatArrayOf(1f, 1f)
        val noise = intent?.getFloatExtra(KEY_NOISE, 0.5f)

        Log.d(TAG, "onStartCommand: Parameters = { $modelName, $seed, ${truncations.joinToString()}, $noise }")

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        Log.d(TAG, "++onDestroy++")
        super.onDestroy()
    }
}