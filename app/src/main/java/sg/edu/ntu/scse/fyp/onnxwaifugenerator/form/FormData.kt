package sg.edu.ntu.scse.fyp.onnxwaifugenerator.form

import androidx.compose.runtime.saveable.mapSaver
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.OnnxModel

data class FormData(
    val model: OnnxModel,
    val seed: Int,
    val isRandomSeed: Boolean,
    val trunc1: Float,
    val trunc2: Float,
    val noise: Float
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
