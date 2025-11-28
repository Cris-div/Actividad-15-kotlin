package com.miempresa.miscursoscrud_firebase

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

// Usamos el mismo ViewModel que el lab, pero agregamos Hilt/Factory si es necesario en un entorno real.
// Para este lab, una simple extensión de ViewModel es suficiente.
class CursosViewModel : ViewModel() {

    // Instancias de Firebase
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // OBTENER EL ID DEL USUARIO. Es la clave para el filtrado.
    private val userId: String = auth.currentUser?.uid ?: ""

    // Estado observable para la lista de cursos
    private val _cursos = mutableStateOf<List<Curso>>(emptyList())
    val cursos: State<List<Curso>> = _cursos

    // Estado para controlar la visibilidad del diálogo (Crear/Editar)
    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog

    // Estado para el curso que se está editando (null si es una creación)
    private val _cursoToEdit = mutableStateOf<Curso?>(null)
    val cursoToEdit: State<Curso?> = _cursoToEdit

    init {
        // Inicia la escucha de datos desde Firestore al inicializar el ViewModel
        fetchCursos()
    }

    // Función principal para leer los cursos con filtro por usuario
    private fun fetchCursos() {
        if (userId.isBlank()) return // No hacer nada si el usuario no está logueado

        firestore.collection("cursos")
            // CRÍTICO: Filtra los cursos para que SÓLO se muestren los del usuario actual
            .whereEqualTo("userId", userId)
            // Escucha en tiempo real (Listener)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Manejar error de escucha (ej. log o Toast)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val cursosList = snapshot.documents.mapNotNull { doc ->
                        // Mapea el documento a un objeto Curso y asegura que tenga su ID
                        doc.toObject(Curso::class.java)?.copy(id = doc.id)
                    }
                    _cursos.value = cursosList
                }
            }
    }

    // ------------------ CRUD OPERACIONES ------------------

    // CREATE / UPDATE
    fun saveCurso(curso: Curso) {
        if (userId.isBlank()) return

        if (curso.id.isBlank()) {
            // CREATE: Agregar nuevo documento
            val newCurso = curso.copy(userId = userId)
            firestore.collection("cursos").add(newCurso)
        } else {
            // UPDATE: Actualizar documento existente
            // Aseguramos que no se intente cambiar el userId
            val updatedCurso = curso.copy(userId = userId)
            firestore.collection("cursos").document(curso.id).set(updatedCurso)
        }
        // Cerrar el diálogo después de guardar
        hideDialog()
    }

    // DELETE
    fun deleteCurso(cursoId: String) {
        if (userId.isBlank()) return

        // Eliminar el documento por su ID
        firestore.collection("cursos").document(cursoId).delete()
    }

    // ------------------ MANEJO DEL DIÁLOGO ------------------

    fun showCreateDialog() {
        _cursoToEdit.value = null // Para asegurar que sea una creación
        _showDialog.value = true
    }

    fun showEditDialog(curso: Curso) {
        _cursoToEdit.value = curso // Cargar los datos para editar
        _showDialog.value = true
    }

    fun hideDialog() {
        _showDialog.value = false
        _cursoToEdit.value = null // Limpiar el estado
    }
}