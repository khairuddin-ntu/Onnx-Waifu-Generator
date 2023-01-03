package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageList(listViewModel: ImageListViewModel = viewModel()) {
    val images by listViewModel.imageList.collectAsState(emptyList())
    val pagerState = rememberPagerState()

    // Updates on first launch & whenever generatedImages is updated
    LaunchedEffect(images) {
        pagerState.scrollToPage(
            if (images.isEmpty()) 0
            else images.lastIndex
        )
    }

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        count = images.size,
        state = pagerState
    ) { i ->
        val painter = rememberAsyncImagePainter(images[i])
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painter,
            contentDescription = ""
        )
    }
}
