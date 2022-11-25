package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Main UI
 *
 * @param generateShape Function to run when Generate button is clicked
 */
@Composable
fun AppUi(generateShape: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Button(onClick = generateShape) {
            Text(text = "Generate")
        }
    }
}