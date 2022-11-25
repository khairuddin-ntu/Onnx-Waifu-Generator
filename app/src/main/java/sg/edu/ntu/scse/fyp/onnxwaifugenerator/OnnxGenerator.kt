package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.res.Resources
import android.util.Log
import androidx.annotation.RawRes
import java.nio.FloatBuffer
import kotlin.random.Random

const val RANDOM_INPUT_SIZE = 1024

private const val TAG = "OnnxGenerator"

/**
 * This class contains Onnx-related code to generate images from given models
 */
class OnnxGenerator(res: Resources) {
    /**
     * Onnx environment
     */
    private val env = OrtEnvironment.getEnvironment()

    /**
     * Mapping model
     */
    private val mappingSession: OrtSession

    /**
     * Synthesis model
     */
    private val synthesisSession: OrtSession

    init {
        // Read model files
        val mappingModel = loadModel(res, R.raw.g_mapping)
        val synthesisModel = loadModel(res, R.raw.g_synthesis)

        // Load model files into Onnx runtime
        mappingSession = env.createSession(mappingModel)
        synthesisSession = env.createSession(synthesisModel)
    }

    /**
     * Generates an image using models
     *
     * @param seed Seed used for random number generator
     * @param psi Array of slider values
     * @param noise Noise value for synthesis
     *
     * @return Output from mapping & synthesis models as byte array
     */
    fun generateImage(seed: Int, psi: FloatArray, noise: Float): ByteArray {
        Log.d(TAG, "++generateImage++")
        val startTime = System.currentTimeMillis()
        val ran = Random(seed)

        // Run mapping
        val zBuffer = FloatBuffer.wrap(FloatArray(RANDOM_INPUT_SIZE) { ran.nextFloat() })
        val zTensor = OnnxTensor.createTensor(env, zBuffer, longArrayOf(1, RANDOM_INPUT_SIZE.toLong()))

        val psiBuffer = FloatBuffer.wrap(psi)
        val psiTensor = OnnxTensor.createTensor(env, psiBuffer, longArrayOf(psi.size.toLong()))

        val mappingInputs = mapOf(
            mappingSession.inputNames.elementAt(0) to zTensor,
            mappingSession.inputNames.elementAt(1) to psiTensor
        )

        val mappingOutput = mappingSession.run(mappingInputs)

        // Run synthesis
        val noiseBuffer = FloatBuffer.wrap(floatArrayOf(noise))
        val noiseTensor = OnnxTensor.createTensor(env, noiseBuffer, longArrayOf(1))

        val synthesisInputs = mapOf(
            synthesisSession.inputNames.elementAt(0) to mappingOutput.get(0) as OnnxTensor,
            synthesisSession.inputNames.elementAt(1) to noiseTensor
        )

        val synthesisOutput = synthesisSession.run(synthesisInputs).get(0) as OnnxTensor
        Log.d(TAG, "generateImage: Synthesis output info = ${synthesisOutput.info}")
        Log.d(TAG, "--generateImage--   Time taken to run = ${System.currentTimeMillis() - startTime}ms")
        return synthesisOutput.byteBuffer.array()
    }

    /**
     * Closes sessions so that they are cleared from memory
     */
    fun close() {
        mappingSession.close()
        synthesisSession.close()
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