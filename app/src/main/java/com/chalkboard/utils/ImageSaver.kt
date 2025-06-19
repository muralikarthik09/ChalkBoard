package com.chalkboard.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//TODO Save functionality
object ImageSaver {

    fun saveBitmapToGallery(
        context: Context,
        bitmap: ImageBitmap,
        format: ImageFormat = ImageFormat.PNG,
        onSuccess: (Uri) -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        try {
            val resolver = context.contentResolver
            val imageCollection = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    MediaStore.Images.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL_PRIMARY
                    )
                }

                else -> {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
            }

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(Date())
            val imageDetails = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "drawing_$timeStamp")
                put(MediaStore.Images.Media.MIME_TYPE, format.mimeType)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val imageUri = resolver.insert(imageCollection, imageDetails)
            imageUri?.let { uri ->
                resolver.openOutputStream(uri)?.use { outputStream ->
                    when (format) {
                        ImageFormat.PNG -> bitmap.asAndroidBitmap()
                            .compress(Bitmap.CompressFormat.PNG, 100, outputStream)

                        ImageFormat.JPEG -> bitmap.asAndroidBitmap()
                            .compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        imageDetails.clear()
                        imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
                        resolver.update(uri, imageDetails, null, null)
                    }

                    onSuccess(uri)
                }
            } ?: throw Exception("Failed to create new MediaStore record")
        } catch (e: Exception) {
            onError(e)
        }
    }

    enum class ImageFormat(val mimeType: String) {
        PNG("image/png"),
        JPEG("image/jpeg")
    }
}