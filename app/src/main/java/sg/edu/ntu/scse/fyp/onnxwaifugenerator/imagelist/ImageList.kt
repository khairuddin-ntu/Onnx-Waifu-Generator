package sg.edu.ntu.scse.fyp.onnxwaifugenerator.imagelist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.collectLatest
import sg.edu.ntu.scse.fyp.onnxwaifugenerator.R
import java.io.File

private const val TAG = "ImageList"

@Composable
fun ImageList(listViewModel: ImageListViewModel = viewModel(), launchForm: () -> Unit) {
    var images by rememberSaveable { mutableStateOf<List<File>>(emptyList()) }

    LaunchedEffect(Unit) {
        listViewModel.imageList.collectLatest { newList ->
            Log.d(TAG, "listViewModel.imageList: Collecting list of size ${newList.size}")
            images = newList
        }
    }

    Box {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(3)
        ) {
            items(
                items = images,
                key = File::getAbsolutePath
            ) { image ->
                val painter = rememberAsyncImagePainter(image)
                Image(
                    modifier = Modifier.size(with(LocalDensity.current) { 512.toDp() }),
                    painter = painter,
                    contentDescription = ""
                )
            }
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
