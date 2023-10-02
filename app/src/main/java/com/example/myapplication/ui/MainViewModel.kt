package com.example.myapplication.ui

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var isCurrentlyDragging by mutableStateOf(false)
        private set

    var items by mutableStateOf(emptyList<BlobClothModel>())
        private set

    var colors by mutableStateOf(emptyList<Color>())
        private set

    var blobClothMap = MutableLiveData(mapOf<String, BlobClothModel?>())
        private set

    var currentBlobCloth by mutableStateOf<Any?>(null)
        private set


    init {
        val map = mutableMapOf<String, BlobClothModel?>()
        map[BlobClothType.HAT] = null
        map[BlobClothType.CLOTHES] = null
        map[BlobClothType.BODY] = BlobClothModel(
            name = "Blob",
            src = "blob",
            type = BlobClothType.BODY,
            id = "blob"
        )
        blobClothMap.postValue(map)

        colors = listOf(
            Color(0xFFFFAA99), // Warm
            Color(0xFFFF99CC), // Warm
            Color(0xFFFF88BB), // Warm
            Color(0xFFFF77AA), // Warm
            Color(0xFFFF6699), // Warm
            Color(0xFFFF5588), // Warm
            Color(0xFFFF4477), // Warm
            Color(0xFFFF3366), // Warm
            Color(0xFFFF2255), // Warm
            Color(0xFFFF1144), // Warm
            Color(0xFFFFCC99), // Warm
            Color(0xFFFFCCFF), // Warm
            Color(0xFFFF99CC), // Warm
            Color(0xFFFF99FF), // Warm
            Color(0xFFFF6666), // Warm
            Color(0xFFFFCC66), // Warm
            Color(0xFFFF9966), // Warm
            Color(0xFFFF6699), // Warm
            Color(0xFFFF9966), // Warm
            Color(0xFFFF6699), // Warm
            Color(0xFFA0FFA0), // Green
            Color(0xFF99FFCC), // Green
            Color(0xFFCCFF99), // Green
            Color(0xFF99FF99), // Green
            Color(0xFF66FF66), // Green
            Color(0xFFDDDDFF), // Cool
            Color(0xFFAAAAFF), // Cool
            Color(0xFF8888FF), // Cool
            Color(0xFF6666FF),  // Cool
            Color(0xFF2222CC)  // Cool
        )

        items = listOf(
            BlobClothModel("horns", "1", "hat", "horns"),
            BlobClothModel("bandage", "2", "hat", "bandage"),
            BlobClothModel("formalhat", "1", "hat", "formalhat"),
            BlobClothModel("unicornhorn", "2", "hat", "unicornhorn"),
            BlobClothModel("hairstyle1", "1", "hat", "hairstyle1"),
            BlobClothModel("gymband", "2", "hat", "gymband"),
            BlobClothModel("cap", "1", "hat", "cap"),
            BlobClothModel("coldcap", "2", "hat", "coldcap"),
            BlobClothModel("horns", "1", "hat", "horns"),
            BlobClothModel("bandage", "2", "hat", "bandage"),
        )
    }

    fun startDragging(currentBlobClothModel: Any?) {
        currentBlobCloth = currentBlobClothModel
        isCurrentlyDragging = true
    }

    fun stopDragging() {
        currentBlobCloth = null
        isCurrentlyDragging = false
    }

    fun updateCloth(currentBlobClothModel: BlobClothModel) {
        currentBlobCloth = currentBlobClothModel
        val map = blobClothMap.value?.toMutableMap() ?: mutableMapOf()
        map[currentBlobClothModel.type] = currentBlobClothModel
        blobClothMap.value = map
    }

}

@Retention(AnnotationRetention.SOURCE)
annotation class BlobClothType {
    companion object {
        const val CLOTHES = "clothes"
        const val HAT = "hat"
        const val BODY = "body"
    }
}