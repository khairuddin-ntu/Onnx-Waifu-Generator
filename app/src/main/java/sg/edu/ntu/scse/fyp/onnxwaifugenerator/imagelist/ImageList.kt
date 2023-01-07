package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagelist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageList(listViewModel: ImageListViewModel = viewModel(), launchForm: () -> Unit) {
    val images by listViewModel.imageList.collectAsState(emptyList())
    val pagerState = rememberPagerState()

    // Updates on first launch & whenever generatedImages is updated
    LaunchedEffect(images) {
        pagerState.scrollToPage(
            if (images.isEmpty()) 0
            else images.lastIndex
        )
    }

    Box {
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
        FloatingActionButton(
            onClick = launchForm,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.spacing_default))
        ) {
            Icon(Icons.Rounded.Add, "")
        }
    }
}
