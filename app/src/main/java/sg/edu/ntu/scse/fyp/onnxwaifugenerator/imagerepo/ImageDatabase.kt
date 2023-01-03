package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagerepo

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [RenderedImage::class])
abstract class ImageDatabase : RoomDatabase() {
    abstract fun imageListDao(): ImageListDAO

    abstract fun imageInsertDao(): ImageInsertDAO
}
