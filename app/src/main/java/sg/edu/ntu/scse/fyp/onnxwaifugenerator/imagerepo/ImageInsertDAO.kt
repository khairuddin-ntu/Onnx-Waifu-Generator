package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagerepo

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ImageInsertDAO {
    @Insert
    suspend fun addImage(image: RenderedImage)
}
