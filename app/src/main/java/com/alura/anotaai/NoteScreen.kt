package com.alura.anotaai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alura.anotaai.model.Note
import com.alura.anotaai.ui.notescreen.ListNotes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    onBackClicked: () -> Unit = {},
    onNoteSaved: (note: Note) -> Unit = {}
) {
    // State to hold the note text
    var noteText by remember { mutableStateOf("") }
    var noteTextAppBar by remember { mutableStateOf("Nova Nota") }
    var list1 by remember { mutableStateOf(listOf("1", "2", "3", "4", "5")) }

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
                            Note(
                                title = noteTextAppBar,
                                description = noteText,
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon for adding a photo from the camera
                    IconButton(onClick = { /* Handle camera action */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "Add photo from camera"
                        )
                    }

                    // Icon for adding an image from the gallery
                    IconButton(onClick = { /* Handle gallery action */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_gallery),
                            contentDescription = "Add image from gallery"
                        )
                    }

                    // Icon for starting audio recording
                    IconButton(onClick = { /* Handle audio recording action */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_mic),
                            contentDescription = "Start audio recording"
                        )
                    }

                    // Icon for adding a text note
                    IconButton(onClick = {
                        list1 = list1.toMutableList().apply {
                            add(noteText)
                        }
                        noteText = ""
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Add text note"
                        )
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
                    noteText = noteText,
                    onNoteTextChanged = { noteText = it },
                    list1 = list1,
                    onNewList = { list1 = it }
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

