package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

@Preview
@Composable
fun SeedSliderPreview() {
    Column(
        Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        SeedSlider(seedValue = 0, setSeed = {}, isEnabled = true)
    }
}

@Preview
@Composable
fun FloatSliderPreview() {
    Column(
        Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        FloatParamSlider(
            label = "Truncation 1",
            value = 0f,
            maxValue = 2f,
            onValueChange = {},
            isEnabled = true
        )
    }
}
