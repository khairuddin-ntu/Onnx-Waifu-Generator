package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlin.random.Random

private const val MAX_SEED_VALUE = 100_000

/**
 * Main UI
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainUi(mainViewModel: MainViewModel = viewModel()) {
    val isGenerating = mainViewModel.isGenerating
    val generatedImages = mainViewModel.imageList

    val pagerState = rememberPagerState(generatedImages?.lastIndex ?: 0)

    var model by rememberSaveable { mutableStateOf(OnnxModel.SKYTNT) }
    var seed by rememberSaveable { mutableStateOf(0) }
    var isRandomSeed by rememberSaveable { mutableStateOf(false) }
    var trunc1 by rememberSaveable { mutableStateOf(1f) }
    var trunc2 by rememberSaveable { mutableStateOf(1f) }
    var noise by rememberSaveable { mutableStateOf(0.5f) }

    val generateShape: () -> Unit = {
        val finalSeed: Int
        if (isRandomSeed) {
            finalSeed = Random.nextInt(0, MAX_SEED_VALUE)
            seed = finalSeed
        } else {
            finalSeed = seed
        }

        mainViewModel.generateImage(model, finalSeed, floatArrayOf(trunc1, trunc2), noise)
    }

    LaunchedEffect(generatedImages) {
        pagerState.scrollToPage(generatedImages?.lastIndex ?: 0)
    }

    // UI
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Text("Model")
        Spacer(Modifier.padding(bottom = 8.dp))
        Column(Modifier.selectableGroup()) {
            OnnxModel.values().forEach { onnxModel ->
                Row(
                    Modifier.selectable(
                        selected = model == onnxModel,
                        onClick = { model = onnxModel },
                        role = Role.RadioButton,
                        enabled = !isGenerating
                    )
                ) {
                    RadioButton(
                        selected = model == onnxModel,
                        onClick = null,
                        enabled = !isGenerating
                    )
                    Text(onnxModel.label)
                }
            }
        }
        Spacer(Modifier.padding(bottom = 16.dp))
        LabelledSlider(
            label = "Seed",
            value = seed.toFloat(),
            valueRange = 0f..(MAX_SEED_VALUE.toFloat()),
            onValueChange = { seed = it.toInt() },
            isEnabled = !isGenerating && !isRandomSeed
        )
        Row(
            modifier = Modifier.align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isRandomSeed,
                onCheckedChange = { isRandomSeed = it },
                enabled = !isGenerating
            )
            Text("Random")
        }
        ModelParamSlider(
            label = "Truncation 1",
            value = trunc1,
            maxValue = 2f,
            onValueChange = { trunc1 = it },
            isEnabled = !isGenerating
        )
        ModelParamSlider(
            label = "Truncation 2",
            value = trunc2,
            maxValue = 2f,
            onValueChange = { trunc2 = it },
            isEnabled = !isGenerating
        )
        ModelParamSlider(
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
