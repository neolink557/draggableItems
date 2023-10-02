package com.example.myapplication.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun MainScreen(
    mainViewModel: MainViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val blobClothes by mainViewModel.blobClothMap.observeAsState(null)
        var needsColor by remember {
            mutableStateOf(false)
        }
        BlobClothesMenu(mainViewModel = mainViewModel, { needsColor = it })
        Blob(mainViewModel = mainViewModel, blobClothes = blobClothes) { needsColor = it }
        AnimatedVisibility(
            needsColor,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            ColorPicker(mainViewModel = mainViewModel)
        }
    }
}

@Composable
fun BlobClothesMenu(mainViewModel: MainViewModel, changeColorState: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .background(Color.Black.copy(0.85f), RoundedCornerShape(10.dp))
    ) {
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .horizontalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {

            mainViewModel.items.forEach { blob ->
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(.3f), RoundedCornerShape(10.dp))
                        .height(75.dp)
                        .width(75.dp)
                        .clickable {
                            mainViewModel.updateCloth(blob)
                            changeColorState(true)
                            mainViewModel.currentBlobCloth?.let {
                                val current = it as BlobClothModel
                                mainViewModel.updateCloth(current.copy(color = Color.White))
                            }
                        }
                ) {
                    DragTarget(
                        dataToDrop = blob,
                        viewModel = mainViewModel
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize(),
                            painter = painterResource(typeMapper(blob.src)),
                            contentDescription = "blob"
                        )
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorPicker(mainViewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.Black.copy(0.85f), RoundedCornerShape(10.dp))
    ) {
        mainViewModel.currentBlobCloth?.let {
            val currentBlob = mainViewModel.currentBlobCloth as BlobClothModel
            Column {
                Text(
                    text = currentBlob.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    },
                    modifier = Modifier.padding(20.dp),
                    color = Color.White
                )
                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(20.dp),
                    cells = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    items(mainViewModel.colors.size) { index ->
                        Box(
                            modifier = Modifier
                                .background(mainViewModel.colors[index], RoundedCornerShape(10.dp))
                                .height(75.dp)
                                .width(75.dp)
                                .clickable {
                                    mainViewModel.updateCloth(currentBlob.copy(color = mainViewModel.colors[index]))
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Blob(
    mainViewModel: MainViewModel,
    blobClothes: Map<String, BlobClothModel?>?,
    changeColorState: (Boolean) -> Unit
) {
    DropItem<BlobClothModel>(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) { isInBound, blobItem ->
        if (!blobClothes.isNullOrEmpty()) {
            //Hat
            blobClothes[BlobClothType.BODY]?.let { body ->
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(top = 50.dp)
                        .clickable {
                            changeColorState(false)
                        },
                    painter = painterResource(id = com.example.myapplication.R.drawable.blob),
                    colorFilter = ColorFilter.tint(body.color, blendMode = BlendMode.Modulate),
                    contentDescription = "blob"
                )

                //HeadBox
                Box(
                    modifier = Modifier
                        .width(250.dp)
                        .padding(top = 50.dp)
                        .height(340.dp)
                        .padding(top = 50.dp)
                        .clickable {
                            changeColorState(true)
                            mainViewModel.updateCloth(body)
                        }
                )
            }

            //Hat
            blobClothes[BlobClothType.HAT]?.let { cloth ->
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(165.dp)
                        .clickable {
                            changeColorState(false)
                            mainViewModel.updateCloth(cloth)
                        },
                    painter = painterResource(typeMapper(cloth.src)),
                    colorFilter = ColorFilter.tint(cloth.color, blendMode = BlendMode.Modulate),
                    contentDescription = "blob"
                )

                //HeadBox
                Box(
                    modifier = Modifier
                        .width(250.dp)
                        .padding(top = 20.dp)
                        .height(130.dp)
                        .padding(top = 50.dp)
                        .clickable {
                            changeColorState(true)
                            mainViewModel.updateCloth(cloth)
                        }
                )
            }

        }
        if (isInBound) {
            mainViewModel.currentBlobCloth?.let { blobCloth ->
                val blob = blobCloth as BlobClothModel
                Image(
                    modifier = Modifier
                        .clickable {
                            mainViewModel.updateCloth(blob)
                            changeColorState(true)
                        }
                        .fillMaxWidth()
                        .height(165.dp)
                        .alpha(if (mainViewModel.isCurrentlyDragging) .5f else 1f),
                    painter = painterResource(typeMapper(blob.src)),
                    contentDescription = "blob"
                )
            }
            blobItem?.let {
                if (!mainViewModel.isCurrentlyDragging) mainViewModel.updateCloth(
                    blobItem
                )
                changeColorState(true)
            }
        }
    }
}


fun typeMapper(item: String): Int {
    return when (item) {
        "horns" -> com.example.myapplication.R.drawable.horns
        "bandage" -> com.example.myapplication.R.drawable.bandage
        "formalhat" -> com.example.myapplication.R.drawable.formalhat
        "unicornhorn" -> com.example.myapplication.R.drawable.unicornhorn
        "hairstyle1" -> com.example.myapplication.R.drawable.hairstyle1
        "gymband" -> com.example.myapplication.R.drawable.gymband
        "cap" -> com.example.myapplication.R.drawable.cap
        "coldcap" -> com.example.myapplication.R.drawable.coldcap
        else -> com.example.myapplication.R.drawable.horns
    }
}
