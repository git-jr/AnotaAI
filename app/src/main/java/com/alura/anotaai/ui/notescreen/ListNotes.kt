package com.alura.anotaai.ui.notescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alura.anotaai.dragContainer
import com.alura.anotaai.draggableItems
import com.alura.anotaai.rememberDragDropState

@Composable
fun ListNotes(
    modifier: Modifier = Modifier,
    noteText: String = "",
    onNoteTextChanged: (String) -> Unit = {},
    list1: List<String> = emptyList(),
    onNewList: (List<String>) -> Unit
) {
    val draggableItems by remember {
        derivedStateOf { list1.size }
    }
    val stateList = rememberLazyListState()

    val dragDropState =
        rememberDragDropState(
            lazyListState = stateList,
            draggableItemsNum = draggableItems,
            onMove = { fromIndex, toIndex ->
                val newList = list1.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
                onNewList(newList)
            })

    LazyColumn(
        modifier = modifier
            .dragContainer(dragDropState),
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
        draggableItems(items = list1, dragDropState = dragDropState) { modifier, item ->
            ItemNote(
                modifier = modifier,
                item = item,
            )
        }

    }
}


@Composable
private fun ItemNote(
    modifier: Modifier = Modifier,
    item: String
) {
    Card(
        modifier = modifier
    ) {
        Text(
            "Item $item",
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
        list1 = listOf("1", "2", "3"),
        onNewList = {}
    )
}