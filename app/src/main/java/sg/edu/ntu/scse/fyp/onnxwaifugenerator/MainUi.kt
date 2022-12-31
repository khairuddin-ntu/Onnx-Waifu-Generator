package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.form.GeneratorForm
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagelist.ImageList
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration.ImageGenerationReceiver

/**
 * Main UI
 */
@Composable
fun MainUi(mainViewModel: MainViewModel = viewModel()) {
    val isGenerating = mainViewModel.isGenerating
    val generatedImages by mainViewModel.imageList.collectAsState(emptyList())

    ImageGenerationReceiver(mainViewModel::onImageGenerated)

    // UI
    Column(
        Modifier
            .fillMaxSize()
            .padding(
                top = dimensionResource(R.dimen.spacing_default),
                start = dimensionResource(R.dimen.spacing_default),
                end = dimensionResource(R.dimen.spacing_default)
            )
    ) {
        GeneratorForm(
            generateImage = mainViewModel::generateImage,
            isGenerating = isGenerating
        )
        Spacer(Modifier.padding(top = dimensionResource(R.dimen.spacing_small)))
        Box(
            Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { 1024.toDp() })
        ) {
            if (isGenerating) {
                CircularProgressIndicator(Modifier.align(Alignment.TopCenter))
            } else {
                ImageList(images = generatedImages)
            }
        }
    }
}
