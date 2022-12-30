package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GeneratorForm(
    model: OnnxModel,
    setModel: (OnnxModel) -> Unit,
    isGenerating: Boolean
) {
    ModelSelector(
        selectedModel = model,
        setModel = setModel,
        enabled = !isGenerating
    )
}

@Preview
@Composable
fun GeneratorFormPreview() {
    GeneratorForm(
        model = OnnxModel.SKYTNT,
        setModel = {},
        isGenerating = false
    )
}
