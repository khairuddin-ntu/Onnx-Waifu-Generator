package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SeedSlider(
    seedValue: Int,
    setSeed: (Int) -> Unit,
    isEnabled: Boolean
) {
    Row {
        Text("Seed")
        Spacer(Modifier.weight(1f))
        Text(seedValue.toString())
    }
    Slider(
        value = seedValue.toFloat(),
        valueRange = 0f..(MAX_SEED_VALUE.toFloat()),
        onValueChange = { setSeed(it.toInt()) },
        enabled = isEnabled
    )
}

@Composable
fun FloatParamSlider(
    label: String,
    value: Float,
    maxValue: Float,
    onValueChange: (Float) -> Unit,
    isEnabled: Boolean
) {
    Row {
        Text(label)
        Spacer(Modifier.weight(1f))
        Text(String.format("%.1f", value))
    }
    Slider(
        value = value,
        valueRange = 0f..maxValue,
        onValueChange = onValueChange,
        steps = (maxValue / 0.1f).toInt() - 1,
        enabled = isEnabled
    )
}
