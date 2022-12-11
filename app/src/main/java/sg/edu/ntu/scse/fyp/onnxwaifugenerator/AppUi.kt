package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.random.Random

/**
 * Main UI
 */
@Composable
fun AppUi(mainViewModel: MainViewModel = viewModel()) {
    val isGenerating = mainViewModel.isGenerating
    val generatedImage = mainViewModel.generatedImage

    val (model, setModel) = remember { mutableStateOf(OnnxModel.SKYTNT) }
    val (seed, setSeed) = remember { mutableStateOf(0) }
    val (isRandomSeed, setRandomSeed) = remember { mutableStateOf(false) }
    val (trunc1, setTrunc1) = remember { mutableStateOf(1f) }
    val (trunc2, setTrunc2) = remember { mutableStateOf(1f) }
    val (noise, setNoise) = remember { mutableStateOf(0.5f) }

    val generateShape: () -> Unit = {
        val finalSeed: Int
        if (isRandomSeed) {
            finalSeed = Random.nextInt(0, Int.MAX_VALUE)
            setSeed(finalSeed)
        } else {
            finalSeed = seed
        }

        // Performs shape generation in a background thread
        mainViewModel.generateShape(model, finalSeed, floatArrayOf(trunc1, trunc2), noise)
    }

    // UI
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Text("Model")
        Spacer(Modifier.padding(bottom = 8.dp))
        Column(Modifier.selectableGroup()) {
            OnnxModel.values().forEach { onnxModel ->
                Row(
                    Modifier.selectable(
                        selected = model == onnxModel,
                        onClick = { setModel(onnxModel) },
                        role = Role.RadioButton,
                        enabled = !isGenerating
                    )
                ) {

                    RadioButton(
                        selected = model == onnxModel,
                        onClick = null,
                        enabled = !isGenerating
                    )
                    Text(onnxModel.label)
                }
            }
        }
        Spacer(Modifier.padding(bottom = 16.dp))
        LabelledSlider(
            label = "Seed",
            value = seed.toFloat(),
            valueRange = 0f..Int.MAX_VALUE.toFloat(),
            onValueChange = { setSeed(it.toInt()) },
            isEnabled = !isGenerating && !isRandomSeed
        )
        Row(
            modifier = Modifier.align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isRandomSeed,
                onCheckedChange = setRandomSeed,
                enabled = !isGenerating
            )
            Text("Random")
        }
        ModelParamSlider(
            label = "Truncation 1",
            value = trunc1,
            maxValue = 2f,
            onValueChange = setTrunc1,
            isEnabled = !isGenerating
        )
        ModelParamSlider(
            label = "Truncation 2",
            value = trunc2,
            maxValue = 2f,
            onValueChange = setTrunc2,
            isEnabled = !isGenerating
        )
        ModelParamSlider(
            label = "Noise",
            value = noise,
            maxValue = 1f,
            onValueChange = setNoise,
            isEnabled = !isGenerating
        )
        Button(
            onClick = generateShape,
            enabled = !isGenerating
        ) {
            Text(text = "Generate")
        }
        if (isGenerating) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        }
        if (generatedImage != null && !isGenerating) {
            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                bitmap = generatedImage.asImageBitmap(),
                contentDescription = ""
            )
        }
    }
}
