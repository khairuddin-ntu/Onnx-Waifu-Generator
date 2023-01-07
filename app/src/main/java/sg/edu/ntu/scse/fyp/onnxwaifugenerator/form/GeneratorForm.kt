package sg.edu.ntu.scse.fyp.onnxwaifugenerator.form

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.R
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.MAX_SEED_VALUE
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.OnnxModel
import kotlin.random.Random

@Composable
fun GeneratorForm(
    generateImage: (OnnxModel, Int, FloatArray, Float) -> Unit,
    isGenerating: Boolean
) {
    val (model, setModel) = rememberSaveable { mutableStateOf(OnnxModel.SKYTNT) }
    val (seed, setSeed) = rememberSaveable { mutableStateOf(0) }
    val (isRandomSeed, setRandomSeed) = rememberSaveable { mutableStateOf(false) }
    val (trunc1, setTrunc1) = rememberSaveable { mutableStateOf(1f) }
    val (trunc2, setTrunc2) = rememberSaveable { mutableStateOf(1f) }
    val (noise, setNoise) = rememberSaveable { mutableStateOf(0.5f) }

    val generateShape: () -> Unit = {
        val finalSeed: Int
        if (isRandomSeed) {
            finalSeed = Random.nextInt(0, MAX_SEED_VALUE)
            setSeed(finalSeed)
        } else {
            finalSeed = seed
        }

        generateImage(model, finalSeed, floatArrayOf(trunc1, trunc2), noise)
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.spacing_default))
    ) {
        ModelSelector(
            selectedModel = model,
            setModel = setModel,
            enabled = !isGenerating
        )
        Spacer(Modifier.padding(bottom = dimensionResource(R.dimen.spacing_default)))
        SeedSlider(
            seedValue = seed,
            setSeed = setSeed,
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
        FloatParamSlider(
            label = stringResource(R.string.label_slider_truncation1),
            value = trunc1,
            maxValue = 2f,
            onValueChange = setTrunc1,
            isEnabled = !isGenerating
        )
        FloatParamSlider(
            label = stringResource(R.string.label_slider_truncation2),
            value = trunc2,
            maxValue = 2f,
            onValueChange = setTrunc2,
            isEnabled = !isGenerating
        )
        FloatParamSlider(
            label = stringResource(R.string.label_slider_noise),
            value = noise,
            maxValue = 1f,
            onValueChange = setNoise,
            isEnabled = !isGenerating
        )
        Button(
            onClick = generateShape,
            enabled = !isGenerating
        ) {
            Text(stringResource(R.string.label_button_generate))
        }
        if (isGenerating) {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

@Preview
@Composable
fun NotGeneratingPreview() {
    GeneratorForm(
        generateImage = { _, _, _, _ -> },
        isGenerating = false,
    )
}

@Preview
@Composable
fun GeneratingPreview() {
    GeneratorForm(
        generateImage = { _, _, _, _ -> },
        isGenerating = true,
    )
}
