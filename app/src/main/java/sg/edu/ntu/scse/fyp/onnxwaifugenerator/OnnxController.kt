package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import ai.onnxruntime.OrtEnvironment
import android.content.res.Resources
import androidx.annotation.RawRes

class OnnxController {
    private var onnxGenerator: OnnxGenerator? = null

    fun generateImage(
        res: Resources,
        model: OnnxModel,
        seed: Int,
        psi: FloatArray,
        noise: Float
    ): Pair<FloatArray, LongArray> {
        onnxGenerator?.close()

        val env = OrtEnvironment.getEnvironment()
        val generator = when (model) {
            OnnxModel.SKYTNT -> OnnxGenerator(
                env,
                env.createSession(loadModel(res, R.raw.g_mapping)),
                env.createSession(loadModel(res, R.raw.g_synthesis))
            )
            OnnxModel.CUSTOM -> TODO()
        }
        val output = generator.generateImage(seed, psi, noise)
        onnxGenerator = generator
        return output
    }

    fun close() {
        onnxGenerator?.close()
    }

    /**
     * Reads model file from raw folder
     *
     * @param res Resources object from Android UI
     * @param rawId Resource ID of model file
     *
     * @return Model file as byte array
     */
    private fun loadModel(res: Resources, @RawRes rawId: Int): ByteArray {
        val modelInputStream = res.openRawResource(rawId)
        val modelBuffer = ByteArray(modelInputStream.available())
        modelInputStream.read(modelBuffer)
        return modelBuffer
    }
}