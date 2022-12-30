package sg.edu.ntu.scse.fyp.onnxwaifugenerator.form

import androidx.compose.runtime.saveable.mapSaver
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.OnnxModel

data class FormData(
    val model: OnnxModel = OnnxModel.SKYTNT,
    val seed: Int = 0,
    val isRandomSeed: Boolean = false,
    val trunc1: Float = 1f,
    val trunc2: Float = 1f,
    val noise: Float = 0.5f
)

val FormSaver = run {
    val modelKey= "model"
    val seedKey = "seed"
    val randomSeedKey = "random_seed"
    val trunc1Key = "trunc1"
    val trunc2Key = "trunc2"
    val noiseKey = "noise"

    mapSaver(
        save = {
            mapOf(
                modelKey to it.model.name,
                seedKey to it.seed,
                randomSeedKey to it.isRandomSeed,
                trunc1Key to it.trunc1,
                trunc2Key to it.trunc2,
                noiseKey to it.noise
            )
        },
        restore = {
            FormData(
                OnnxModel.valueOf(it[modelKey] as String),
                it[seedKey] as Int,
                it[randomSeedKey] as Boolean,
                it[trunc1Key] as Float,
                it[trunc2Key] as Float,
                it[noiseKey] as Float
            )
        }
    )
}
