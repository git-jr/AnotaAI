package com.alura.anotaai

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.alura.anotaai.model.Note
import com.alura.anotaai.model.NoteItemAudio
import com.alura.anotaai.model.NoteItemImage
import com.alura.anotaai.model.NoteItemText
import com.alura.anotaai.ui.camera.CameraInitializer
import com.alura.anotaai.ui.notescreen.ListNotes
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
    val context = LocalContext.current
    var noteText by remember { mutableStateOf("") }
    var noteTextAppBar by remember { mutableStateOf("Nova Nota ") }
    var noteState: Note by remember { mutableStateOf(Note()) }
    noteToEdit?.let {
        noteState = it
        noteTextAppBar = it.title
    }

    var isRecording by remember { mutableStateOf(false) }
    var addAudioNote by remember { mutableStateOf(false) }
    var audioDuration: Int by remember { mutableIntStateOf(0) }
    var audioPath by remember { mutableStateOf("") }

    var showCameraScreen by remember { mutableStateOf(false) }

    // para adicionar a nota de áudio ao fim de uma gravação
    LaunchedEffect(isRecording) {
        if (addAudioNote) {
            noteState = noteState.copy(
                listItems = noteState.listItems.toMutableList().apply {
                    add(
                        NoteItemAudio(
                            link = audioPath,
                            duration = audioDuration
                        )
                    )
                }
            )
            addAudioNote = false
        }
    }

    // time da gravacao
    LaunchedEffect(isRecording) {
        if (!isRecording) {
            audioDuration = 0
        } else {
            while (isRecording) {
                audioDuration++
                delay(1000)
            }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let { uri ->
                PermissionUtils(context).persistUriPermission(uri)
                noteState = noteState.copy(
                    title = noteTextAppBar,
                    listItems = noteState.listItems.toMutableList().apply {
                        add(
                            NoteItemImage(
                                link = uri.toString(),
                            )
                        )
                    }
                )
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
                            value = noteTextAppBar,
                            onValueChange = { noteTextAppBar = it },
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
                        onNoteSaved(noteState.copy(title = noteTextAppBar))
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
                    Crossfade(targetState = isRecording) {
                        if (it) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val textTemp = "Gravando: ${audioDuration.audioDisplay()}"

                                Text(
                                    text = textTemp,
                                    fontSize = 20.sp
                                )
                                IconButton(
                                    onClick = {
                                        onStopRecording()
                                        isRecording = false
                                        addAudioNote = true
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
                                    showCameraScreen = true
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
                                    if (isRecording) {
                                        addAudioNote = true
                                        onStopRecording()
                                    } else {
                                        audioPath =
                                            "${context.externalCacheDir?.absolutePath}/audio${System.currentTimeMillis()}.acc"
                                        onStartRecording(audioPath)
                                    }
                                    isRecording = true
                                }) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_mic),
                                        contentDescription = "Start audio recording"
                                    )
                                }

                                // Icon for adding a text note
                                IconButton(onClick = {
                                    if (noteText.isBlank()) return@IconButton
                                    noteState = noteState.copy(
                                        title = noteTextAppBar,
                                        listItems = noteState.listItems.toMutableList().apply {
                                            add(
                                                NoteItemText(
                                                    content = noteText,
                                                )
                                            )
                                        }
                                    )
                                    noteText = ""
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
                    noteState = noteState,
                    noteText = noteText,
                    onNoteTextChanged = { noteText = it },
                    onPlayAudio = onPlayAudio,
                    onStopAudio = onStopAudio,
                    onUpdatedItem = { updateItem, id ->
                        // atualizar a lista primeiro
                        val updatedList = noteState.listItems.map { item ->
                            if (item.id == id && item is NoteItemText) item.copy(content = updateItem) else item
                        }

                        // atualizar o estado da nota
                        noteState = noteState.copy(
                            listItems = updatedList
                        )
                    },
                    onDeletedItem = { id ->
                        noteState = noteState.copy(
                            listItems = noteState.listItems.filter { item -> item.id != id }
                        )
                        Log.d("NoteScreen", "Item deleted: $noteState")
                    }
                )
            }
        },
    )

    if (showCameraScreen) {
        CameraInitializer(
            onImageSaved = { filePath ->
                noteState = noteState.copy(
                    title = noteTextAppBar,
                    listItems = noteState.listItems.toMutableList().apply {
                        add(
                            NoteItemImage(
                                link = filePath,
                            )
                        )
                    }
                )
                showCameraScreen = false
            },
            onError = {
                Toast.makeText(context, "Erro ao salvar imagem", Toast.LENGTH_SHORT)
                    .show()
                showCameraScreen = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoteScreen() {
    NoteScreen()
}

