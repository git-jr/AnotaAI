package com.alura.anotaai.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil3.compose.AsyncImage
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executor


@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraScreen(
    onImageSaved: (String) -> Unit = {},
    onError: () -> Unit = {}
) {
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // 1 Camera Controller
        val context = LocalContext.current
        val cameraController = remember {
            LifecycleCameraController(context)
        }

        // 2 Camera Preview
        Box(
            modifier = Modifier
                .fillMaxSize()
                ,
        ) {
            CameraPreview(cameraController)
            // Botão para capturar a foto

            Card(
                modifier = Modifier
                    .padding(bottom = 56.dp)
                    .align(Alignment.BottomCenter)
                    .clickable {
                        capturePhoto(context, cameraController) { bitmap ->
                            capturedImage = bitmap
                        }
                    },
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = "Camera",
                    )
                    Text("Tirar foto")
                }
            }
        }

        capturedImage?.let {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    it,
                    contentDescription = "Preview",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(56.dp)
                    ,
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Card(
                        modifier = Modifier
                            .clickable { capturedImage = null },
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Camera",
                            )
                            Text("Tirar outra")
                        }
                    }

                    Card(
                        modifier = Modifier
                            .clickable {
                                capturedImage?.let {
                                    saveBitmapToInternalStorage(
                                        context,
                                        it,
                                        onSaved = { filePath ->
                                            onImageSaved(filePath)
                                            capturedImage = null
                                        },
                                        onError = { message ->
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                                .show()
                                            onError()
                                        }
                                    )
                                }
                            },
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Camera",
                            )
                            Text("Usar foto")
                        }
                    }
                }
            }
        }
    }

}

private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val correctedBitmap: Bitmap = image
                .toBitmap()
                .rotateBitmap(image.imageInfo.rotationDegrees)

            onPhotoCaptured(correctedBitmap)
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraContent", "Error capturing image", exception)
        }
    })
}

// Função para rotacionar o Bitmap
fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(-rotationDegrees.toFloat())
        postScale(-1f, -1f)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}


// Função para salvar o Bitmap no armazenamento interno
private fun saveBitmapToInternalStorage(
    context: Context, bitmap: Bitmap,
    onSaved: (String) -> Unit = {},
    onError: (String) -> Unit = {}
) {
    // filename com data e hora atual
    val fileName = "image_${System.currentTimeMillis()}.jpg"
    var fileOutputStream: FileOutputStream? = null
    try {
        fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            fileOutputStream
        ) // Salva em JPEG, qualidade 100
        Log.d("CameraContent", "Image saved successfully in internal storage")
        val filePath = context.getFileStreamPath(fileName).absolutePath
        onSaved(filePath)
    } catch (e: IOException) {
        Log.e("CameraContent", "Error saving image", e)
        onError("Erro ao salvar a imagem")
    } finally {
        fileOutputStream?.close()
    }
}

