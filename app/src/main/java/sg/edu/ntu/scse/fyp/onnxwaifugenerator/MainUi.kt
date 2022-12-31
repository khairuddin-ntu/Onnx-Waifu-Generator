package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.*
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.form.FormData
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.form.FormSaver
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.form.GeneratorForm
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration.ImageGenerationReceiver
import kotlin.random.Random

/**
 * Main UI
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainUi(mainViewModel: MainViewModel = viewModel()) {
    val isGenerating = mainViewModel.isGenerating
    val generatedImages = mainViewModel.imageList

    val pagerState = rememberPagerState()

    val (formData, setFormData) = rememberSaveable(stateSaver = FormSaver) {
        mutableStateOf(FormData())
    }

    val generateShape: () -> Unit = {
        val (model, seed, isRandomSeed, trunc1, trunc2, noise) = formData

        val finalSeed: Int
        if (isRandomSeed) {
            finalSeed = Random.nextInt(0, MAX_SEED_VALUE)
            setFormData(formData.copy(seed = finalSeed))
        } else {
            finalSeed = seed
        }

        mainViewModel.generateImage(model, finalSeed, floatArrayOf(trunc1, trunc2), noise)
    }

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
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        GeneratorForm(formData, setFormData, generateShape, isGenerating)
        Spacer(Modifier.padding(top = 8.dp))
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
