package sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.R
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.*
import java.io.File
import kotlin.math.roundToInt

private const val TAG = "ImageGenerationService"
private const val ID_NOTIFICATION = 2319

class ImageGenerationService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.Default)

    private lateinit var onnxController: OnnxController
    private lateinit var imageListDir: File

    override fun onCreate() {
        onnxController = OnnxController(resources)
        imageListDir = File(filesDir, "generated_images")
        if (!imageListDir.exists()) {
            imageListDir.mkdir()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "++onStartCommand++")

        // Get parameters
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

        // Create permanent notification
        startForeground(
            ID_NOTIFICATION, Notification.Builder(this, CHANNEL_IMAGE_GENERATION)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.title_notification_imageGen))
                .setContentText(
                    getString(
                        R.string.template_imageGenParams,
                        modelType.label, seed, psi[0], psi[1], noise
                    )
                )
                .build()
        )

        // Performs shape generation in a background thread
        serviceScope.launch {
            val (modelOutput, shape) = onnxController.generateImage(modelType, seed, psi, noise)

            Log.d(TAG, "generateShape: Output shape = ${shape.joinToString()}")
            val imgWidth = shape[3].toInt()
            val imgHeight = shape[2].toInt()
            val bitmap = convertModelToBitmap(modelOutput, imgWidth, imgHeight)

            // Save to file in app storage
            withContext(Dispatchers.IO) {
                val file = File(imageListDir, "model-output-${System.currentTimeMillis()}.png")
                val fileOutputStream = file.outputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.close()
                Log.d(TAG, "generateShape: Bitmap stored in ${file.absolutePath}")
            }

            bitmap.recycle()

            withContext(Dispatchers.Main) {
                stopForeground(STOP_FOREGROUND_REMOVE)
                sendBroadcast(Intent(ACTION_SERVICE_RESPONSE))
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        serviceScope.cancel()
        onnxController.close()
        super.onDestroy()
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