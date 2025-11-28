package com.miempresa.miscursoscrud_firebase

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    // Obtenemos la instancia de FirebaseAuth
    val auth = FirebaseAuth.getInstance()
    // Obtenemos el contexto actual para mostrar Toasts
    val context = LocalContext.current

    // Estados para el correo electrónico, la contraseña y el estado de carga
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // ------------------ TÍTULO SUPERIOR ------------------
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Programación Móvil - Tecsup",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ------------------ IMAGEN TECSUP ------------------
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "logo Tecsup",
                modifier = Modifier
                    .size(120.dp) // Ajusta el tamaño si quieres más grande o más pequeño
                    .align(Alignment.CenterHorizontally)
            )
        }

        // ------------------ CONTENIDO PRINCIPAL (Formulario) ------------------
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Ingresar
            Button(
                onClick = {
                    // Validar si los campos están vacíos
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Iniciar el estado de carga
                    isLoading = true

                    // Llamada a Firebase para iniciar sesión
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            // Detener el estado de carga al completarse la tarea
                            isLoading = false

                            if (task.isSuccessful) {
                                // Éxito: Mostrar mensaje y navegar a la siguiente pantalla
                                Toast.makeText(context, "Inicio exitoso ✅", Toast.LENGTH_SHORT).show()
                                onLoginSuccess()
                            } else {
                                // Fracaso: Mostrar mensaje de error
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
                Text(if (isLoading) "Cargando..." else "Ingresar")
            }

            // Botón para navegar a Registrarse
            TextButton(onClick = onNavigateToRegister) {
                Text("¿No tienes cuenta? Regístrate aquí")
            }
        }

        // ------------------ PIE DE PÁGINA ------------------
        Text(
            text = "Juan León S. - Tecsup",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}