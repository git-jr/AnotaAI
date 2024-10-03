package com.alura.anotaai

import android.os.Bundle
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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alura.anotaai.model.Note
import com.alura.anotaai.model.sampleNotes


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnotaAITheme {
                var showNoteScreen by remember { mutableStateOf(false) }
                var noteList by remember { mutableStateOf(sampleNotes) }

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
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemListScreen(
    modifier: Modifier = Modifier,
    itemsList: List<Note> = emptyList(),
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
fun ItemRow(note: Note) {
    // Single item row layout
    Card(
        modifier = Modifier
            .fillMaxWidth(),
//        elevation = 4.dp
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