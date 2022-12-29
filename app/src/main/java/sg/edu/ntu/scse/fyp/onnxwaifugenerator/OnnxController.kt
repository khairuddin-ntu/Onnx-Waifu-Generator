package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import ai.onnxruntime.OrtEnvironment
import android.content.res.Resources
import androidx.annotation.RawRes

class OnnxController(res: Resources) {
    private val env = OrtEnvironment.getEnvironment()
    private val skytntGenerator by lazy {
        OnnxGenerator(env, res, R.raw.g_mapping, R.raw.g_synthesis, 1024)
    }

    private val customGenerator by lazy {
        OnnxGenerator(env, res, R.raw.g_mapping_aravind, R.raw.g_synthesis_aravind, 512)
    }

    fun generateImage(
        model: OnnxModel,
        seed: Int,
        psi: FloatArray,
        noise: Float
    ): Pair<FloatArray, LongArray> {
        val generator = when (model) {
            OnnxModel.SKYTNT -> skytntGenerator
            OnnxModel.CUSTOM -> customGenerator
        }

        return generator.generateImage(seed, psi, noise)
    }

    fun close() {
        skytntGenerator.close()
        customGenerator.close()
    }
}
