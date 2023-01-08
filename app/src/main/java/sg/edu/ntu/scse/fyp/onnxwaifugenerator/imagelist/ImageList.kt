package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagelist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.R
import java.io.File

private const val TAG = "ImageList"

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageList(listViewModel: ImageListViewModel = viewModel(), launchForm: () -> Unit) {
    var images by rememberSaveable { mutableStateOf<List<File>>(emptyList()) }
    val pagerState = rememberPagerState()

    // Updates on first launch & whenever generatedImages is updated
    LaunchedEffect(images) {
        pagerState.scrollToPage(
            if (images.isEmpty()) 0
            else images.lastIndex
        )
    }

    LaunchedEffect(Unit) {
        listViewModel.imageList.collectLatest { newList ->
            Log.d(TAG, "listViewModel.imageList: Collecting list of size ${newList.size}")
            images = newList
        }
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
