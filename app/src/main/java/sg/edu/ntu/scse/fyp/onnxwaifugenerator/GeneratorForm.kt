package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.runtime.Composable

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

}
