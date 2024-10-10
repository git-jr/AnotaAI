package com.alura.anotaai

import android.media.MediaRecorder
import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import com.alura.anotaai.model.NoteItem
import com.alura.anotaai.model.NoteItemAudio
import com.alura.anotaai.model.NoteItemImage
import com.alura.anotaai.model.NoteItemText
import com.alura.anotaai.ui.notescreen.ListNotes
import kotlinx.coroutines.delay
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    onBackClicked: () -> Unit = {},
    onNoteSaved: (noteItem: NoteItem) -> Unit = {},
    onStartRecording: () -> Unit = {},
    onStopRecording: () -> Unit = {},
    onPlayRecording: () -> Unit = {},
) {
    var noteText by remember { mutableStateOf("") }
    var noteTextAppBar by remember { mutableStateOf("Nova Nota") }
    var noteState by remember {
        mutableStateOf(
            Note(
                title = "Título",
            )
        )
    }

    var isRecording by remember { mutableStateOf(false) }
    var addAudioNote by remember { mutableStateOf(false) }
    var audioDuration: Int by remember { mutableIntStateOf(0) }

    // para adicionar a nota de áudio ao fim de uma gravação
    LaunchedEffect(isRecording) {
        if (addAudioNote) {
            noteState = noteState.copy(
                listItems = noteState.listItems.toMutableList().apply {
                    add(
                        NoteItemAudio(
                            link = "Áudio",
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

    // logica para calcular o tempo de gravação
    var recorder: MediaRecorder? = null

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
                            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
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
                        onNoteSaved(
                            NoteItem(
                                title = noteTextAppBar,
                                description = noteState.title,
                            )
                        )
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
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val textTemp = "Gravando: ${audioDuration.audioDisplay()}"

                                Text(
                                    text = textTemp,
                                    fontSize = 20.sp
                                )
                                IconButton(onClick = {
                                    onStopRecording()
                                    isRecording = false
                                    addAudioNote = true
                                }) {
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
                                    onPlayRecording()
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
                                        onStartRecording()
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
                    onNoteTextChanged = { noteText = it }
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNoteScreen() {
    NoteScreen()
}

