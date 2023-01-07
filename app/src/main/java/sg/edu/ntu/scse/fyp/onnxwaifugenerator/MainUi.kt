package sg.edu.ntu.scse.fyp.onnxwaifugenerator

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.NAV_GENERATE
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.common.NAV_IMAGE_LIST
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.form.GeneratorForm
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagelist.ImageList
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.onnxgeneration.ImageGenerationReceiver

/**
 * Main UI
 */
@Composable
fun MainUi(mainViewModel: MainViewModel = viewModel()) {
    val isGenerating = mainViewModel.isGenerating
    val navController = rememberNavController()

    ImageGenerationReceiver(mainViewModel::onImageGenerated)

    NavHost(navController, startDestination = NAV_IMAGE_LIST) {
        composable(NAV_IMAGE_LIST) { ImageList() }
        composable(NAV_GENERATE) {
            GeneratorForm(
                generateImage = mainViewModel::generateImage,
                isGenerating = isGenerating
            )
        }
    }

    // UI
//    Column(
//        Modifier
//            .fillMaxSize()
//            .padding(
//                top = dimensionResource(R.dimen.spacing_default),
//                start = dimensionResource(R.dimen.spacing_default),
//                end = dimensionResource(R.dimen.spacing_default)
//            )
//    ) {
//        GeneratorForm(
//            generateImage = mainViewModel::generateImage,
//            isGenerating = isGenerating
//        )
//        Spacer(Modifier.padding(top = dimensionResource(R.dimen.spacing_small)))
//        Box(
//            Modifier
//                .fillMaxWidth()
//                .height(with(LocalDensity.current) { 1024.toDp() })
//        ) {
//            if (isGenerating) {
//                CircularProgressIndicator(Modifier.align(Alignment.TopCenter))
//            } else {
//                ImageList()
//            }
//        }
//    }
}
