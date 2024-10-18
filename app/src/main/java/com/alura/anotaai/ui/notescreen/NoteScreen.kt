package com.alura.anotaai.ui.notescreen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alura.anotaai.R
import com.alura.anotaai.extensions.audioDisplay
import com.alura.anotaai.model.Note
import com.alura.anotaai.ui.camera.CameraInitializer
import com.alura.anotaai.utils.PermissionUtils
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    noteToEdit: Note? = Note(),
    onBackClicked: () -> Unit = {},
    onNoteSaved: (note: Note) -> Unit = {},
    onStartRecording: (String) -> Unit = {},
    onStopRecording: () -> Unit = {},
    onPlayAudio: (String) -> Unit = {},
    onStopAudio: () -> Unit = {},
) {
    val viewModel = hiltViewModel<NoteViewModel>()
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        noteToEdit?.let {
            viewModel.getNoteById(it.id)
        }
    }

    LaunchedEffect(state.isRecording) {
        if (state.addAudioNote) {
            viewModel.addNewItemAudio()
        }

        if (!state.isRecording) {
            viewModel.updateAudioDuration(0)
        } else {
            repeat(Int.MAX_VALUE) {
                viewModel.updateAudioDuration(state.audioDuration + 1)
                delay(1000)
            }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let { uri ->
                PermissionUtils(context).persistUriPermission(uri)
                viewModel.addNewItemImage(uri.toString())
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BasicTextField(
                            value = state.noteTextAppBar,
                            onValueChange = {
                                viewModel.updateNoteTextAppBar(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                        )
                    }

                },
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onNoteSaved(state.note.copy(title = state.noteTextAppBar))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                contentPadding = PaddingValues(16.dp),
                tonalElevation = 8.dp
            ) {
                Column {
                    Crossfade(targetState = state.isRecording) {
                        if (it) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val textTemp = "Gravando: ${state.audioDuration.audioDisplay()}"

                                Text(
                                    text = textTemp,
                                    fontSize = 20.sp
                                )
                                IconButton(
                                    onClick = {
                                        onStopRecording()
                                        viewModel.updateIsRecording(false)
                                        viewModel.updateAddAudioNote(true)
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_stop),
                                        contentDescription = "Stop recording"
                                    )
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Icon for adding a photo from the camera
                                IconButton(onClick = {
                                    viewModel.updateShowCameraState(true)
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_camera),
                                        contentDescription = "Add photo from camera"
                                    )
                                }

                                // Icon for adding an image from the gallery
                                IconButton(onClick = {
                                    /* Handle gallery action */
                                    pickImageLauncher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_gallery),
                                        contentDescription = "Add image from gallery"
                                    )
                                }

                                // Icon for starting audio recording
                                IconButton(onClick = {
                                    if (state.isRecording) {
                                        viewModel.updateAddAudioNote(true)
                                        onStopRecording()
                                    } else {
                                        val audioPath =
                                            "${context.externalCacheDir?.absolutePath}/audio${System.currentTimeMillis()}.acc"
                                        viewModel.setAudioPath(audioPath)
                                        onStartRecording(audioPath)
                                    }
                                    viewModel.updateIsRecording(!state.isRecording)
                                }) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_mic),
                                        contentDescription = "Start audio recording"
                                    )
                                }

                                // Icon for adding a text note
                                IconButton(onClick = {
                                    if (state.noteText.isBlank()) return@IconButton
                                    viewModel.addNewItemText()
                                }) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "Add text note"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(
                        horizontal = 8.dp
                    ),
            ) {
                ListNotes(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                    noteState = state.note,
                    noteText = state.noteText,
                    onNoteTextChanged = { viewModel.updateNoteText(it) },
                    onPlayAudio = onPlayAudio,
                    onStopAudio = onStopAudio,
                    onUpdatedItem = { updateItem, id ->
                        viewModel.updateItemText(updateItem, id)
                    },
                    onDeletedItem = { itemNote ->
                        viewModel.deleteItemNote(itemNote)
                    }
                )
            }
        },
    )

    if (state.showCameraScreen) {
        CameraInitializer(
            onImageSaved = { filePath ->
                viewModel.addNewItemImage(filePath)
                viewModel.updateShowCameraState(false)
            },
            onError = {
                Toast.makeText(context, "Erro ao salvar imagem", Toast.LENGTH_SHORT)
                    .show()
                viewModel.updateShowCameraState(false)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoteScreen() {
    NoteScreen()
}

