package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationManagerCompat
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.CHANNEL_IMAGE_GENERATION

class OnnxWaifuGenApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Create notification channel if not present
        val notificationManager = NotificationManagerCompat.from(this)
        if (notificationManager.notificationChannels.none { it.id == CHANNEL_IMAGE_GENERATION }) {
            val notificationChannel = NotificationChannel(
                CHANNEL_IMAGE_GENERATION,
                getString(R.string.title_notificationChannel_imageGen),
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.setShowBadge(false)
            notificationChannel.setSound(null, null)

            notificationManager.createNotificationChannel(notificationChannel)
        }

        // Retaining this code in case support for older Android versions is added
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//        }
    }
}