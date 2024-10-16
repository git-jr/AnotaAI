package com.alura.anotaai

import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alura.anotaai.ui.theme.AnotaAITheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import coil3.compose.AsyncImage
import com.alura.anotaai.model.Note
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : ComponentActivity() {
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    //    private var fileName: String = ""
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        setContent {
            AnotaAITheme {
                var showNoteScreen by remember { mutableStateOf(false) }
                val noteList = remember { mutableStateListOf<Note>() }
                var noteToEdit by remember { mutableStateOf<Note?>(null) }

                var itemCounter by remember { mutableIntStateOf(3) }

                ItemListScreen(
                    listNotes = noteList,
                    onNewItemClicked = {
                        noteToEdit = null
                        showNoteScreen = true
                    },
                    onOpenNote = { note ->
                        noteToEdit = note
                        showNoteScreen = true
                    },
                    onDeletedItem = { note ->
                        noteList.remove(note)
                    }
                )

                if (showNoteScreen) {
                    NoteScreen(
                        noteToEdit = noteToEdit,
                        onBackClicked = { showNoteScreen = false },
                        onNoteSaved = { note ->
                            showNoteScreen = false
                            itemCounter++
                            noteList.add(note)
                        },
                        onStartRecording = { audioPath ->
                            startRecording(audioPath)
                        },
                        onStopRecording = {
                            stopRecording()
                        },
                        onPlayAudio = { audioPath ->
                            startPlaying(audioPath)
                        },
                        onStopAudio = {
                            stopPlaying()
                        }
                    )
                }
            }
        }
    }

    private fun startRecording(audioPath: String) {
        val context = this
        val mediaRecorder: MediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
        recorder = mediaRecorder
            .apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setOutputFile(audioPath)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)

                try {
                    prepare()
                    Toast.makeText(context, "Gravando", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    Toast.makeText(context, "Erro ao iniciar gravação", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("LOG_TAG", "prepare() failed")
                }

                start()
            }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    private fun startPlaying(fileName: String) {
        val context = this
        stopPlaying()

        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
                Toast.makeText(context, "Reproduzindo", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(context, "Erro ao iniciar reprodução", Toast.LENGTH_SHORT)
                    .show()
                Log.e("LOG_TAG", "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    override fun onDestroy() {
        super.onDestroy()
        recorder?.release()
    }
}

@Composable
fun ItemListScreen(
    modifier: Modifier = Modifier,
    listNotes: List<Note> = emptyList(),
    onNewItemClicked: () -> Unit = {},
    onOpenNote: (Note) -> Unit = {},
    onDeletedItem: (Note) -> Unit = {}
) {
    // Scaffold to manage the floating action button and the content
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNewItemClicked()
                },
                content = { Icon(Icons.Default.Add, contentDescription = "Add Item") }
            )
        },
        content = { paddingValues ->
            Crossfade(targetState = listNotes.isNotEmpty()) { hasItems ->
                if (hasItems) {
                    // List of items displayed using LazyColumn
                    var itemToDelete by remember { mutableStateOf<Note?>(null) }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(listNotes) { item ->
                            ItemRow(
                                note = item,
                                onClick = { onOpenNote(item) },
                                onLongPress = {
                                    itemToDelete = item
                                }
                            )
                        }
                    }

                    itemToDelete?.let { itemId ->
                        AlertDialog(
                            onDismissRequest = { itemToDelete = null },
                            title = { Text(text = "Confirmação de Exclusão") },
                            text = { Text("Tem certeza que deseja excluir este item?") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        itemToDelete = null
                                        onDeletedItem(itemId)
                                    }
                                ) {
                                    Text("Sim")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { itemToDelete = null }
                                ) {
                                    Text("Não")
                                }
                            }
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 150.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            R.mipmap.ic_launcher_foreground,
                            contentDescription = "App Logo",
                            modifier = Modifier.size(300.dp),
                        )

                        Text(
                            text = "Crie suas notas clicando no botão abaixo",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ItemRow(
    note: Note,
    onClick: () -> Unit,
    onLongPress: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = { onLongPress() }
                )
            },
    ) {
        Text(
            text = note.title,
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemListScreen() {
    ItemListScreen()
}