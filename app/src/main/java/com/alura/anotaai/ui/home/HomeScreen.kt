package com.alura.anotaai.ui.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.alura.anotaai.R
import com.alura.anotaai.extensions.toDisplayDate
import com.alura.anotaai.model.Note
import com.alura.anotaai.model.NoteType


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onAddNewNote: () -> Unit = {},
    onOpenNote: (String) -> Unit = {}
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Anota Aí",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )

                HorizontalDivider()
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddNewNote()
                },
                content = { Icon(Icons.Default.Add, contentDescription = "Add Item") }
            )
        },
        content = { paddingValues ->
            Crossfade(targetState = state.notes.isNotEmpty(), label = "hasItems") { hasItems ->
                if (hasItems) {
                    var itemToDelete by remember { mutableStateOf<Note?>(null) }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(state.notes) { item ->
                            ItemNote(
                                note = item,
                                onClick = { onOpenNote(item.id) },
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
                                        viewModel.removeNote(itemId)
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
fun ItemNote(
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
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            val thumb = if (note.thumbnail == NoteType.AUDIO.name) {
                R.drawable.ic_mic
            } else if (note.thumbnail == NoteType.TEXT.name) {
                R.drawable.ic_title
            } else {
                note.thumbnail
            }


            Column(
                modifier = Modifier
                    .weight(0.8f)
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = note.title,
                    fontSize = 20.sp
                )
                Text(
                    text = note.date.toDisplayDate(),
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Card(
                modifier = Modifier
                    .weight(0.2f)
                    .size(70.dp),
            ) {
                AsyncImage(
                    thumb,
                    contentDescription = "Note Thumbnail",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemListScreen() {
    HomeScreen()
}