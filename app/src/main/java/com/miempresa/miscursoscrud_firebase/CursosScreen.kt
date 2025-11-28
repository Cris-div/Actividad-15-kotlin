package com.miempresa.miscursoscrud_firebase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CursosScreen(
    modifier: Modifier = Modifier,
    viewModel: CursosViewModel
) {
    // Observa la lista de cursos desde el ViewModel
    val cursos = viewModel.cursos.value

    if (cursos.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No tienes cursos registrados. ¡Agrega uno!", style = MaterialTheme.typography.titleMedium)
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            items(cursos, key = { it.id }) { curso ->
                // Llama al composable que dibuja cada tarjeta de curso
                CursoItem(
                    curso = curso,
                    onEdit = { viewModel.showEditDialog(curso) },
                    onDelete = { viewModel.deleteCurso(curso.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Composable para cada item de la lista (READ)
@Composable
fun CursoItem(curso: Curso, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onEdit),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = curso.nombre,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = curso.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Créditos: ${curso.creditos}",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            // Botón de Editar
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar")
            }

            // Botón de Eliminar
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
            }
        }
    }
}