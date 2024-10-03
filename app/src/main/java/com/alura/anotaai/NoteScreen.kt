package com.alura.anotaai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alura.anotaai.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    onBackClicked: () -> Unit = {},
    onNoteSaved: (note: Note) -> Unit = {}
) {
    // State to hold the note text
    var noteText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Nova Nota") },
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
                                title = noteText,
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
        content = { paddingValues ->
            // Main content of the screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Text field for writing a note
                BasicTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (noteText.isEmpty()) {
                                Text(
                                    text = "Escreva sua nota aqui...",
                                    style = LocalTextStyle.current.copy(fontSize = 20.sp)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        },
        bottomBar = {
            // Bottom bar with 3 icon buttons
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
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add photo from camera"
                        )
                    }

                    // Icon for adding an image from the gallery
                    IconButton(onClick = { /* Handle gallery action */ }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Add image from gallery"
                        )
                    }

                    // Icon for starting audio recording
                    IconButton(onClick = { /* Handle audio recording action */ }) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Start audio recording"
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNoteScreen() {
    NoteScreen()
}
