package com.alura.anotaai.ui.notescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.alura.anotaai.audioDisplay
import com.alura.anotaai.model.Note
import com.alura.anotaai.model.NoteItemAudio
import com.alura.anotaai.model.NoteItemImage
import com.alura.anotaai.model.NoteItemText
import com.alura.anotaai.model.NoteType
import kotlinx.coroutines.delay

@Composable
fun ListNotes(
    modifier: Modifier = Modifier,
    noteText: String = "",
    onNoteTextChanged: (String) -> Unit = {},
    noteState: Note = Note(),
    onPlayAudio: (String) -> Unit = {},
    onStopAudio: () -> Unit = {},
    onUpdatedItem: (String, String) -> Unit = { _, _ -> }
) {
    val stateList = rememberLazyListState()

    LazyColumn(
        state = stateList,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(maxHeight = 100.dp),
            ) {
                BasicTextField(
                    value = noteText,
                    onValueChange = { onNoteTextChanged(it) },
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
            HorizontalDivider(
                Modifier.padding(top = 8.dp)
            )

        }

        items(noteState.listItems.reversed()) { item ->
            when (item.type) {
                NoteType.TEXT -> {
                    ItemNoteText(
                        modifier = Modifier,
                        item = item as NoteItemText,
                        onUpdated = { updatedItemText ->
                            onUpdatedItem(
                                updatedItemText, item.id
                            )
                        }
                    )
                }

                NoteType.IMAGE -> {
                    ItemNoteImage(
                        modifier = Modifier,
                        item = item as NoteItemImage,
                    )
                }

                NoteType.AUDIO -> {
                    ItemNoteAudio(
                        modifier = Modifier,
                        item = item as NoteItemAudio,
                        onPlayAudio = onPlayAudio,
                        onStopAudio = onStopAudio
                    )
                }
            }
        }
    }
}


@Composable
private fun ItemNoteText(
    modifier: Modifier = Modifier,
    item: NoteItemText,
    onUpdated: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var stateText by remember { mutableStateOf(item.content) }
    Card(
        modifier = modifier,
        onClick = { isEditing = true }
    ) {
        if (isEditing) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    modifier = Modifier
                        .weight(0.8f),
                    value = stateText,
                    onValueChange = {
                        stateText = it
                    },
                    textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(16.dp)
                        ) {
                            innerTextField()
                        }
                    }
                )
                IconButton(
                    onClick = {
                        isEditing = false
                        onUpdated(stateText)
                    },
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxHeight()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Close",
                    )
                }
            }
        } else {
            Text(
                item.content,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun ItemNoteImage(
    modifier: Modifier = Modifier,
    item: NoteItemImage
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        onClick = { expanded = !expanded }
    ) {
        if (expanded) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(),
                model = item.link,
                contentScale = ContentScale.Fit,
                contentDescription = item.transcription
            )
        } else {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(),
                model = item.link,
                contentScale = ContentScale.Crop,
                contentDescription = item.transcription
            )
        }
    }
}

@Composable
private fun ItemNoteAudio(
    modifier: Modifier = Modifier,
    item: NoteItemAudio,
    onPlayAudio: (String) -> Unit,
    onStopAudio: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    val icon = if (isPlaying) Icons.Filled.Close else Icons.Filled.PlayArrow

    // quando o tempo de duração do audio acabar, o isPlaying é setado para false
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            delay(item.duration * 1000L)
            isPlaying = false
        }
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Green.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                "Áudio ${item.duration.audioDisplay()}",
                modifier = Modifier
                    .padding(16.dp)
            )
            IconButton(
                onClick = {
                    if (isPlaying) {
                        onStopAudio()
                        isPlaying = false
                    } else {
                        onPlayAudio(item.link)
                        isPlaying = true
                    }
                },
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Play",
                    tint = Color.Black,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ListNotesPreview() {
    ListNotes(
        noteState = Note(
            listItems = listOf(
                NoteItemText("Texto 1"),
                NoteItemImage("https://picsum.photos/200"),
                NoteItemAudio("https://audio.com", 42),
                NoteItemText("Texto 2"),
            )
        )
    )
}