package com.miempresa.miscursoscrud_firebase

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    // El ViewModel se inyecta y recuerda en esta función
    viewModel: CursosViewModel = viewModel()
) {
    // Si el diálogo está visible, lo mostramos
    if (viewModel.showDialog.value) {
        CursoDialog(
            viewModel = viewModel,
            // Si hay un curso para editar, pasamos ese curso; si no, es nuevo
            cursoToEdit = viewModel.cursoToEdit.value,
            onDismiss = { viewModel.hideDialog() },
            onSave = { curso -> viewModel.saveCurso(curso) }
        )
    }

    // Scaffold provee la estructura básica de la app (barra superior, FAB, contenido)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Cursos - ${FirebaseAuth.getInstance().currentUser?.email ?: ""}") },
                actions = {
                    // Botón de Cerrar Sesión
                    TextButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogout()
                    }) {
                        Text("Cerrar Sesión", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            // FAB para crear un nuevo curso
            FloatingActionButton(onClick = {
                viewModel.showCreateDialog() // Abrir el diálogo para crear
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Curso")
            }
        }
    ) { paddingValues ->
        // Llama a la pantalla que muestra la lista de cursos
        CursosScreen(
            modifier = Modifier.padding(paddingValues),
            viewModel = viewModel
        )
    }
}