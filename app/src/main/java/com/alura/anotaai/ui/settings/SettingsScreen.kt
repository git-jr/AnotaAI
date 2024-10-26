package com.alura.anotaai.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.alura.anotaai.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Anota Aí",
                            style = MaterialTheme.typography.titleLarge,

                            )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                )
                HorizontalDivider()
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 150.dp, bottom = paddingValues.calculateBottomPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Logo do app e nome do app
                AsyncImage(
                    R.mipmap.ic_launcher_foreground,
                    contentDescription = "App Logo",
                    modifier = Modifier.size(300.dp),
                )

                // Exibir quantas notas foram criadas
                Text(
                    text = "Notas criadas: ${state.notesCount}",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Botão para excluir todas as notas
                Button(
                    onClick = { viewModel.showDeleteDialog(true) }
                ) {
                    Text(text = "Excluir todas as notas")
                }
            }

            if (state.showConfirmDeleteDialog) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.showDeleteDialog(false)
                    },
                    title = { Text(text = "Confirmar exclusão") },
                    text = { Text("Tem certeza que exluir todas as notas?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.deleteAllNotes()
                                viewModel.showDeleteDialog(false)
                            }
                        ) {
                            Text("Sim")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                viewModel.showDeleteDialog(false)
                            }
                        ) {
                            Text("Não")
                        }
                    }
                )
            }
        }
    )
}