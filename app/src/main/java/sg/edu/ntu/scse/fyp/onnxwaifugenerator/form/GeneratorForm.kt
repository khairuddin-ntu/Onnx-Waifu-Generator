package sg.edu.ntu.scse.fyp.onnxwaifugenerator.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
    val (formData, setFormData) = rememberSaveable(stateSaver = FormSaver) {
        mutableStateOf(FormData())
    }

    val generateShape: () -> Unit = {
        val (model, seed, isRandomSeed, trunc1, trunc2, noise) = formData

        val finalSeed: Int
        if (isRandomSeed) {
            finalSeed = Random.nextInt(0, MAX_SEED_VALUE)
            setFormData(formData.copy(seed = finalSeed))
        } else {
            finalSeed = seed
        }

        generateImage(model, finalSeed, floatArrayOf(trunc1, trunc2), noise)
    }

    Column {
        ModelSelector(
            selectedModel = formData.model,
            setModel = { setFormData(formData.copy(model = it)) },
            enabled = !isGenerating
        )
        Spacer(Modifier.padding(bottom = dimensionResource(R.dimen.spacing_default)))
        SeedSlider(
            seedValue = formData.seed,
            setSeed = { setFormData(formData.copy(seed = it)) },
            isEnabled = !isGenerating && !formData.isRandomSeed
        )
        Row(
            modifier = Modifier.align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = formData.isRandomSeed,
                onCheckedChange = { setFormData(formData.copy(isRandomSeed = it)) },
                enabled = !isGenerating
            )
            Text("Random")
        }
        FloatParamSlider(
            label = stringResource(R.string.label_slider_truncation1),
            value = formData.trunc1,
            maxValue = 2f,
            onValueChange = { setFormData(formData.copy(trunc1 = it)) },
            isEnabled = !isGenerating
        )
        FloatParamSlider(
            label = stringResource(R.string.label_slider_truncation2),
            value = formData.trunc2,
            maxValue = 2f,
            onValueChange = { setFormData(formData.copy(trunc2 = it)) },
            isEnabled = !isGenerating
        )
        FloatParamSlider(
            label = stringResource(R.string.label_slider_noise),
            value = formData.noise,
            maxValue = 1f,
            onValueChange = { setFormData(formData.copy(noise = it)) },
            isEnabled = !isGenerating
        )
        Button(
            onClick = generateShape,
            enabled = !isGenerating
        ) {
            Text(stringResource(R.string.label_button_generate))
        }
    }
}

@Preview
@Composable
fun GeneratorFormPreview() {
    GeneratorForm(
        generateImage = { _, _, _, _ -> },
        isGenerating = false,
    )
}
