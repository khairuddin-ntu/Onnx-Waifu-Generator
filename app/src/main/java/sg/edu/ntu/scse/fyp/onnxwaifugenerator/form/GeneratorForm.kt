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
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.OnnxModel

@Composable
fun GeneratorForm(
    model: OnnxModel,
    setModel: (OnnxModel) -> Unit,
    seed: Int,
    setSeed: (Int) -> Unit,
    isRandomSeed: Boolean,
    setRandomSeed: (Boolean) -> Unit,
    trunc1: Float,
    setTrunc1: (Float) -> Unit,
    trunc2: Float,
    setTrunc2: (Float) -> Unit,
    noise: Float,
    setNoise: (Float) -> Unit,
    generateShape: () -> Unit,
    isGenerating: Boolean
) {
    Column {
        ModelSelector(
            selectedModel = model,
            setModel = setModel,
            enabled = !isGenerating
        )
        Spacer(Modifier.padding(bottom = 16.dp))
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
            label = "Truncation 1",
            value = trunc1,
            maxValue = 2f,
            onValueChange = setTrunc1,
            isEnabled = !isGenerating
        )
        FloatParamSlider(
            label = "Truncation 2",
            value = trunc2,
            maxValue = 2f,
            onValueChange = setTrunc2,
            isEnabled = !isGenerating
        )
        FloatParamSlider(
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
    }
}

@Preview
@Composable
fun GeneratorFormPreview() {
    GeneratorForm(
        model = OnnxModel.SKYTNT,
        setModel = {},
        seed = 0,
        setSeed = {},
        isRandomSeed = false,
        setRandomSeed = {},
        trunc1 = 1f,
        setTrunc1 = {},
        trunc2 = 1f,
        setTrunc2 = {},
        noise = 0.5f,
        setNoise = {},
        generateShape = {},
        isGenerating = false,
    )
}
