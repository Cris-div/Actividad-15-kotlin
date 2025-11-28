package com.miempresa.miscursoscrud_firebase

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    // Obtenemos la instancia de FirebaseAuth
    val auth = FirebaseAuth.getInstance()
    // Obtenemos el contexto actual para mostrar Toasts
    val context = LocalContext.current

    // Estados para el correo electrónico, la contraseña, la confirmación y el estado de carga
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center // Centra el contenido vertical y horizontalmente
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(16.dp))

            // Campo de Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Campo de Confirmar Contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Botón de Registrarse
            Button(
                onClick = {
                    // 1. Validar que no haya campos vacíos
                    if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                        Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // 2. Validar que las contraseñas coincidan
                    if (password != confirmPassword) {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // 3. Validar longitud mínima de la contraseña (mínimo 6 caracteres según Firebase)
                    if (password.length < 6) {
                        Toast.makeText(context, "Mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Iniciar el estado de carga
                    isLoading = true

                    // Llamada a Firebase para crear el usuario
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            // Detener el estado de carga al completarse la tarea
                            isLoading = false

                            if (task.isSuccessful) {
                                // Éxito: Mostrar mensaje y navegar a la siguiente pantalla
                                Toast.makeText(context, "Usuario creado ✅", Toast.LENGTH_SHORT).show()
                                onRegisterSuccess()
                            } else {
                                // Fracaso: Mostrar mensaje de error de Firebase
                                Toast.makeText(
                                    context,
                                    "Error: ${task.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                },
                // Deshabilitar el botón si está cargando
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Mostrar texto diferente si está cargando
                Text(if (isLoading) "Creando..." else "Registrarse")
            }

            // Botón para volver al Login
            TextButton(onClick = onNavigateBack) {
                Text("← Volver al login")
            }
        }
    }
}