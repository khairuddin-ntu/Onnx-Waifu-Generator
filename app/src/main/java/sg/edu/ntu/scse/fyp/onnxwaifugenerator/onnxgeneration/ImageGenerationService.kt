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

        if (intent == null) {
            return START_STICKY
        }

        val modelName = intent.getStringExtra(KEY_MODEL) ?: return START_STICKY
        val modelType = OnnxModel.valueOf(modelName)

        val seed = intent.getIntExtra(KEY_SEED, -1)
        if (seed < 0) {
            return START_STICKY
        }

        val psi = intent.getFloatArrayExtra(KEY_TRUNCATIONS) ?: return START_STICKY
        val noise = intent.getFloatExtra(KEY_NOISE, -1f)
        if (noise < 0) {
            return START_STICKY
        }

        Log.d(TAG, "onStartCommand: Parameters = { $modelName, $seed, ${truncations.joinToString()}, $noise }")

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        Log.d(TAG, "++onDestroy++")
        super.onDestroy()
    }
}