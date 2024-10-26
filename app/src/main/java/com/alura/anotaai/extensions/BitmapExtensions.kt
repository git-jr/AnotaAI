package com.alura.anotaai.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.FileOutputStream
import java.io.IOException


fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(-rotationDegrees.toFloat())
        postScale(-1f, -1f)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.saveBitmapToInternalStorage(
    context: Context,
    onSaved: (String) -> Unit = {},
    onError: (String) -> Unit = {}
) {
    val fileName = "image_${System.currentTimeMillis()}.jpg"
    var fileOutputStream: FileOutputStream? = null
    try {
        fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        this.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            fileOutputStream
        )
        val filePath = context.getFileStreamPath(fileName).absolutePath
        onSaved(filePath)
    } catch (e: IOException) {
        onError("Erro ao salvar a imagem no armazenamento interno")
    } finally {
        fileOutputStream?.close()
    }
}
