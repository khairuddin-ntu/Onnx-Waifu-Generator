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

    val (model, setModel) = rememberSaveable { mutableStateOf(OnnxModel.SKYTNT) }
    val (seed, setSeed) = rememberSaveable { mutableStateOf(0) }
    val (isRandomSeed, setRandomSeed) = rememberSaveable { mutableStateOf(false) }
    var trunc1 by rememberSaveable { mutableStateOf(1f) }
    var trunc2 by rememberSaveable { mutableStateOf(1f) }
    var noise by rememberSaveable { mutableStateOf(0.5f) }

    val generateShape: () -> Unit = {
        val finalSeed: Int
        if (isRandomSeed) {
            finalSeed = Random.nextInt(0, MAX_SEED_VALUE)
            setSeed(finalSeed)
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

    // UI
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        GeneratorForm(
            model, setModel,
            seed, setSeed, isRandomSeed, setRandomSeed,
            isGenerating
        )
        FloatParamSlider(
            label = "Truncation 1",
            value = trunc1,
            maxValue = 2f,
            onValueChange = { trunc1 = it },
            isEnabled = !isGenerating
        )
        FloatParamSlider(
            label = "Truncation 2",
            value = trunc2,
            maxValue = 2f,
            onValueChange = { trunc2 = it },
            isEnabled = !isGenerating
        )
        FloatParamSlider(
            label = "Noise",
            value = noise,
            maxValue = 1f,
            onValueChange = { noise = it },
            isEnabled = !isGenerating
        )
        Button(
            onClick = generateShape,
            enabled = !isGenerating
        ) {
            Text(text = "Generate")
        }
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
