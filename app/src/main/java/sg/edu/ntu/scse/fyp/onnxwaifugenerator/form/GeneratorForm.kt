package sg.edu.ntu.scse.fyp.onnxwaifugenerator.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GeneratorForm(
    formData: FormData, setFormData: (FormData) -> Unit,
    generateShape: () -> Unit,
    isGenerating: Boolean
) {
    Column {
        ModelSelector(
            selectedModel = formData.model,
            setModel = { setFormData(formData.copy(model = it)) },
            enabled = !isGenerating
        )
        Spacer(Modifier.padding(bottom = 16.dp))
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
            label = "Truncation 1",
            value = formData.trunc1,
            maxValue = 2f,
            onValueChange = { setFormData(formData.copy(trunc1 = it)) },
            isEnabled = !isGenerating
        )
        FloatParamSlider(
            label = "Truncation 2",
            value = formData.trunc2,
            maxValue = 2f,
            onValueChange = { setFormData(formData.copy(trunc2 = it)) },
            isEnabled = !isGenerating
        )
        FloatParamSlider(
            label = "Noise",
            value = formData.noise,
            maxValue = 1f,
            onValueChange = { setFormData(formData.copy(noise = it)) },
            isEnabled = !isGenerating
        )
        Button(
            onClick = generateShape,
            enabled = !isGenerating
        ) {
            Text(text = "Generate")
        }
    }
}

@Preview
@Composable
fun GeneratorFormPreview() {
    GeneratorForm(
        formData = FormData(),
        setFormData = {},
        generateShape = {},
        isGenerating = false,
    )
}
