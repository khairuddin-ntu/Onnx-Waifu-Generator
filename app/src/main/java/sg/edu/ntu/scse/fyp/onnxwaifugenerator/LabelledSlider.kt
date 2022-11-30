package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LabelledSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    steps: Int = 0,
    isEnabled: Boolean
) {
    Row {
        Text(label)
        Spacer(Modifier.weight(1f))
        Text(value.toString())
    }
    Slider(
        value = value,
        valueRange = valueRange,
        onValueChange = onValueChange,
        steps = steps,
        enabled = isEnabled
    )
}
