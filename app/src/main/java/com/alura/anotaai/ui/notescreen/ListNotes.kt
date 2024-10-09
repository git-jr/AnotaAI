package com.alura.anotaai.ui.notescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.alura.anotaai.model.Note
import com.alura.anotaai.model.NoteItemAudio
import com.alura.anotaai.model.NoteItemImage
import com.alura.anotaai.model.NoteItemText
import com.alura.anotaai.model.NoteType

@Composable
fun ListNotes(
    modifier: Modifier = Modifier,
    noteText: String = "",
    onNoteTextChanged: (String) -> Unit = {},
    noteState: Note = Note(),
) {
    val stateList = rememberLazyListState()

    LazyColumn(
        state = stateList,
        verticalArrangement = Arrangement.spacedBy(16.dp)
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

        items(noteState.listItems) { item ->
            when (item.type) {
                NoteType.TEXT -> {
                    ItemNoteText(
                        modifier = Modifier,
                        item = item as NoteItemText
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
                        item = item as NoteItemAudio
                    )
                }
            }
        }
    }
}


@Composable
private fun ItemNoteText(
    modifier: Modifier = Modifier,
    item: NoteItemText
) {
    Card(
        modifier = modifier
    ) {
        Text(
            "Item ${item.content}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
private fun ItemNoteImage(
    modifier: Modifier = Modifier,
    item: NoteItemImage
) {
    Card(
        modifier = modifier
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            model = item.link,
            contentScale = ContentScale.Crop,
            contentDescription = item.transcription
        )
    }
}

@Composable
private fun ItemNoteAudio(
    modifier: Modifier = Modifier,
    item: NoteItemAudio
) {
    Card(
        modifier = modifier
    ) {
        Text(
            "Áudio ${item.link} - ${item.duration}",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
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