package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagerepo

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

@Dao
abstract class ImageListDAO {
    @Query("SELECT filePath FROM RenderedImage")
    protected abstract fun getAllImagePaths(): Flow<List<String>>

    fun getAllImages() = getAllImagePaths().map { it.map(::File) }
}
