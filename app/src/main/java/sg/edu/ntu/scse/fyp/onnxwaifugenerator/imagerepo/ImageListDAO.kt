package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagerepo

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageListDAO {
    @Query("SELECT filePath FROM RenderedImage")
    fun getAllImagePaths(): Flow<List<String>>
}
