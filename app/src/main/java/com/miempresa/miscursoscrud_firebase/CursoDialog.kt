package com.miempresa.miscursoscrud_firebase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun CursoDialog(
    viewModel: CursosViewModel,
    cursoToEdit: Curso?, // Null si es creación, con datos si es edición
    onDismiss: () -> Unit,
    onSave: (Curso) -> Unit
) {
    // Estados iniciales basados en si estamos creando o editando
    var nombre by remember { mutableStateOf(cursoToEdit?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(cursoToEdit?.descripcion ?: "") }
    var creditosText by remember { mutableStateOf(cursoToEdit?.creditos?.toString() ?: "") }

    // Título dinámico
    val title = if (cursoToEdit == null) "Crear Nuevo Curso" else "Editar Curso"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                // Campo Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Curso") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                // Campo Descripción
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                // Campo Créditos (solo números)
                OutlinedTextField(
                    value = creditosText,
                    onValueChange = { creditosText = it.filter { char -> char.isDigit() } },
                    label = { Text("Créditos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val creditos = creditosText.toIntOrNull() ?: 0
                    if (nombre.isBlank() || descripcion.isBlank() || creditos <= 0) {
                        // Aquí podrías mostrar un Toast de validación si fuera necesario
                        return@Button
                    }

                    // Crea/Actualiza el objeto Curso
                    val curso = (cursoToEdit ?: Curso()).copy(
                        nombre = nombre,
                        descripcion = descripcion,
                        creditos = creditos,
                        id = cursoToEdit?.id ?: "" // Mantiene el ID si es una edición
                    )
                    onSave(curso)
                }
            ) {
                Text(if (cursoToEdit == null) "Guardar" else "Actualizar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}