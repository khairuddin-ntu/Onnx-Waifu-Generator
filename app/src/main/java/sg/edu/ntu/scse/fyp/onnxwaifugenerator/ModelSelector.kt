package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSelector(
    selectedModel: OnnxModel,
    setModel: (OnnxModel) -> Unit,
    enabled: Boolean
) {
    val options = OnnxModel.values()
    var shouldExpand by remember { mutableStateOf(false) }

    val expanded = if (!enabled) false else shouldExpand

    // We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { shouldExpand = !shouldExpand },
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedModel.label,
            onValueChange = {},
            label = { Text("Model") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { shouldExpand = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.label) },
                    onClick = {
                        setModel(selectionOption)
                        shouldExpand = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}