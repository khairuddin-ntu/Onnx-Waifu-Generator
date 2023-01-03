package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagerepo

import android.content.Context
import androidx.room.Room

object ImageRepository {
    @Volatile
    private var DATABASE_INSTANCE: ImageDatabase? = null

    fun getDatabase(context: Context) = DATABASE_INSTANCE ?: synchronized(this) {
        Room.databaseBuilder(context, ImageDatabase::class.java, "image_db")
            .build().also(this::DATABASE_INSTANCE::set)
    }
}
