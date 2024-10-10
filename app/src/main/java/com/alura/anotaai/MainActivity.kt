package com.alura.anotaai

import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.alura.anotaai.model.NoteItem
import com.alura.anotaai.model.sampleNoteItems
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : ComponentActivity() {
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private var fileName: String = ""
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"


        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        }
        val context = this

        setContent {
            AnotaAITheme {
                var showNoteScreen by remember { mutableStateOf(false) }
                var noteList by remember { mutableStateOf(sampleNoteItems) }

//                var itemsList by remember { mutableStateOf(listOf("Item 1", "Item 2", "Item 3")) }
                var itemCounter by remember { mutableIntStateOf(3) }

                ItemListScreen(
                    itemsList = noteList,
                    onNewItemClicked = {
                        showNoteScreen = true
                    }
                )

                if (showNoteScreen) {
                    NoteScreen(
                        onBackClicked = { showNoteScreen = false },
                        onNoteSaved = { note ->
                            showNoteScreen = false
                            itemCounter++
                            noteList = noteList.toMutableList().apply {
                                add(note)
                            }
                        },
                        onStartRecording = {
                            startRecording()
                        },
                        onStopRecording = {
                            stopRecording()
                        },
                        onPlayRecording = {
                            startPlaying()
                        }
                    )
                }
            }
        }
    }

    private fun startRecording() {
        val context = this
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

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

    private fun startPlaying() {
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
    itemsList: List<NoteItem> = emptyList(),
    onNewItemClicked: () -> Unit = {}
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
            // List of items displayed using LazyColumn
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(itemsList) { item ->
                    ItemRow(item)
                }
            }
        }
    )
}

@Composable
fun ItemRow(noteItem: NoteItem) {
    // Single item row layout
    Card(
        modifier = Modifier
            .fillMaxWidth(),
//        elevation = 4.dp
    ) {
        Text(
            text = noteItem.title,
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