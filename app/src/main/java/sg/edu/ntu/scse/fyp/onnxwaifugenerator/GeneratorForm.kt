package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GeneratorForm(
    model: OnnxModel,
    setModel: (OnnxModel) -> Unit,
    seed: Int,
    setSeed: (Int) -> Unit,
    isRandomSeed: Boolean,
    setRandomSeed: (Boolean) -> Unit,
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
        isGenerating = false,
    )
}
