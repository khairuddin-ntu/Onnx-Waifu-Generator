package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagerepo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RenderedImage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val filePath: String,
    val seed: Int,
    val psi1: Float,
    val psi2: Float,
    val noise: Float
)
