package sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.OrtSession.SessionOptions
import android.content.res.Resources
import android.util.Log
import androidx.annotation.RawRes
import java.nio.FloatBuffer
import kotlin.random.Random

private const val TAG = "OnnxGenerator"

private val sessionOptions = SessionOptions().apply {
    // Disabled arena memory allocator because Onnx won't clear arena when session is closed
    // and new ones are created with each new session
    setCPUArenaAllocator(false)
}

/**
 * This class contains Onnx-related code to generate images from given models
 *
 * @param env Onnx environment
 * @param res Resources object to retrieve ONT file
 * @param mappingRes Raw resource ID of mapping model
 * @param synthesisRes Raw resource ID of synthesis model
 * @param zSize Size of input noise
 */
class OnnxGenerator(
    res: Resources,
    @RawRes
    private val mappingRes: Int,
    @RawRes
    private val synthesisRes: Int,
    private val zSize: Int
) {
    private val env = OrtEnvironment.getEnvironment()

    private val mappingSession: OrtSession
    private val synthesisSession: OrtSession

    init {
        mappingSession = env.createSession(loadModel(res, mappingRes), sessionOptions)
        synthesisSession = env.createSession(loadModel(res, synthesisRes), sessionOptions)
    }

    /**
     * Generates an image using models
     *
     * @param seed Seed used for random number generator
     * @param psi Array of slider values
     * @param noise Noise value for synthesis
     *
     * @return Output from mapping & synthesis models as float array and shape of output Tensor as
     * a long array
     */
    fun generateImage(seed: Int, psi: FloatArray, noise: Float): Pair<FloatArray, LongArray> {
        Log.d(TAG, "++generateImage++")
        val startTime = System.currentTimeMillis()
        val ran = Random(seed)

        // Run mapping
        val zBuffer = FloatBuffer.wrap(FloatArray(zSize) { ran.nextFloat() })
        val zTensor = OnnxTensor.createTensor(env, zBuffer, longArrayOf(1, zSize.toLong()))

        val psiBuffer = FloatBuffer.wrap(psi)
        val psiTensor = OnnxTensor.createTensor(env, psiBuffer, longArrayOf(psi.size.toLong()))

        val mappingOutput = mappingSession.run(
            mapOf(
                mappingSession.inputNames.elementAt(0) to zTensor,
                mappingSession.inputNames.elementAt(1) to psiTensor
            )
        )
        zTensor.close()
        psiTensor.close()

        val mappingData = mappingOutput.get(0) as OnnxTensor
        val mapToSyn = OnnxTensor.createTensor(env, mappingData.floatBuffer, mappingData.info.shape)
        mappingOutput.close()

        // Run synthesis
        val noiseBuffer = FloatBuffer.wrap(floatArrayOf(noise))
        val noiseTensor = OnnxTensor.createTensor(env, noiseBuffer, longArrayOf(1))

        val synthesisInputs = mapOf(
            synthesisSession.inputNames.elementAt(0) to mapToSyn,
            synthesisSession.inputNames.elementAt(1) to noiseTensor
        )

        val synthesisOutput = synthesisSession.run(synthesisInputs)
        mapToSyn.close()
        noiseTensor.close()

        val synthesisData = synthesisOutput.get(0) as OnnxTensor
        val imageData = synthesisData.floatBuffer.array()
        val outputInfo = synthesisData.info
        synthesisOutput.close()

        Log.d(TAG, "generateImage: Synthesis output info = $outputInfo")
        Log.d(
            TAG,
            "--generateImage--   Time taken to run = ${System.currentTimeMillis() - startTime}ms"
        )
        return imageData to outputInfo.shape
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
