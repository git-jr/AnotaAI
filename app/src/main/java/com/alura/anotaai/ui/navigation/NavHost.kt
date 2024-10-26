package com.alura.anotaai.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.alura.anotaai.ui.home.HomeScreen
import com.alura.anotaai.ui.notes.NoteScreen
import com.alura.anotaai.ui.settings.SettingsScreen

@Composable
fun NavHost(
    navController: NavHostController,
    startRecording: (String) -> Unit,
    stopRecording: () -> Unit,
    startPlaying: (String) -> Unit,
    stopPlaying: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NoteRoutes.Home
    ) {
        composable<NoteRoutes.Home> {
            HomeScreen(
                onAddNewNote = {
                    navController.navigate(NoteRoutes.NoteDetail())
                },
                onOpenNote = { noteId ->
                    navController.navigate(NoteRoutes.NoteDetail(noteId))
                },
                onOpenProfile = {
                    navController.navigate(NoteRoutes.Settings)
                }
            )
        }

        composable<NoteRoutes.NoteDetail> { backStackEntry ->
            val noteDetail: NoteRoutes.NoteDetail = backStackEntry.toRoute()
            NoteScreen(
                noteToEdit = noteDetail.noteId,
                onBack = { navController.popBackStack() },
                onStartRecording = { startRecording(it) },
                onStopRecording = stopRecording,
                onPlayAudio = { startPlaying(it) },
                onStopAudio = stopPlaying
            )
        }

        composable<NoteRoutes.Settings> {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}