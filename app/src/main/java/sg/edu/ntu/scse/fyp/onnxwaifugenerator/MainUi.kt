package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.*
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.form.GeneratorForm
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration.ImageGenerationReceiver

/**
 * Main UI
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainUi(mainViewModel: MainViewModel = viewModel()) {
    val isGenerating = mainViewModel.isGenerating
    val generatedImages = mainViewModel.imageList

    val pagerState = rememberPagerState()

    // Updates on first launch & whenever generatedImages is updated
    LaunchedEffect(generatedImages) {
        pagerState.scrollToPage(
            if (generatedImages?.isEmpty() == true) 0
            else {
                generatedImages?.lastIndex ?: 0
            }
        )
    }

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
        GeneratorForm(mainViewModel::generateImage, isGenerating)
        Spacer(Modifier.padding(top = dimensionResource(R.dimen.spacing_small)))
        Box(
            Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { 1024.toDp() })
        ) {
            if (isGenerating) {
                CircularProgressIndicator(Modifier.align(Alignment.TopCenter))
            } else if (generatedImages != null) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    count = generatedImages.size,
                    state = pagerState
                ) { i ->
                    val painter = rememberAsyncImagePainter(generatedImages[i])
                    Image(
                        modifier = Modifier.fillMaxWidth(),
                        painter = painter,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}
