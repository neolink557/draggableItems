package com.example.myapplication.ui

import androidx.compose.ui.graphics.Color

data class BlobClothModel(
    val name:String,
    val id:String,
    @BlobClothType
    val type:String,
    val src: String,
    val color: Color = Color.White
)