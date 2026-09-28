package com.ucaldas.taller5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

data class Elemento(
    val id: Int,
    val titulo: String,
    val categoria: String,
    val descripcionCorta: String,
    val descripcionLarga: String
)

val listaElementos = listOf(
    Elemento(1, "Kotlin", "Lenguajes", "Lenguaje principal para Android", "Kotlin es un lenguaje de programación moderno y conciso desarrollado por JetBrains. Es la opción oficial recomendada por Google para desarrollar en Android."),
    Elemento(2, "Jetpack Compose", "Frontend", "UI declarativa en Android", "Es el toolkit moderno de Android para crear interfaces nativas con menos código, herramientas potentes y APIs intuitivas en Kotlin."),
    Elemento(3, "Spring Boot", "Backend", "Framework de Java/Kotlin", "Facilita la creación de aplicaciones web y microservicios independientes, listos para producción con poca configuración."),
    Elemento(4, "PostgreSQL", "Base de Datos", "Base de datos relacional", "Sistema de gestión de bases de datos relacional de código abierto, reconocido por su estabilidad, potencia y soporte de consultas complejas."),
    Elemento(5, "Angular", "Frontend", "Framework web de Google", "Plataforma de desarrollo basada en TypeScript para construir aplicaciones web robustas de una sola página (SPA)."),
    Elemento(6, "TypeScript", "Lenguajes", "Superset con tipado estático", "Extiende JavaScript agregando tipos de datos estáticos, lo que ayuda a prevenir errores antes de ejecutar el código."),
    Elemento(7, "Docker", "Herramientas", "Contenedores de software", "Permite empaquetar una aplicación con todas sus dependencias en un contenedor para desplegarla en cualquier servidor sin problemas."),
    Elemento(8, "Nginx", "Herramientas", "Servidor web y proxy", "Servidor web de alto rendimiento muy usado como proxy inverso, balanceador de carga y caché para optimizar sitios web."),
    Elemento(9, "Tailwind CSS", "Frontend", "Framework CSS utilitario", "Proporciona clases de utilidad directo en el HTML/código para diseñar interfaces rápidas y personalizadas sin escribir CSS desde cero."),
    Elemento(10, "Python", "Lenguajes", "Lenguaje versátil y sencillo", "Muy popular en ciencia de datos, inteligencia artificial y scripts debido a su sintaxis clara y fácil de leer."),
    Elemento(11, "MongoDB", "Base de Datos", "Base de datos NoSQL", "Almacena datos en documentos similares a JSON en lugar de tablas, ideal para proyectos con estructuras de datos cambiantes."),
    Elemento(12, "Android Studio", "Herramientas", "IDE oficial para Android", "Entorno de desarrollo con emuladores, perfiladores de rendimiento y diseño visual integrado para crear apps móviles.")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaScreen(
                elementos = listaElementos,
                onElementoClick = { id ->
                    navController.navigate("detalle/$id") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = "detalle/{elementoId}",
            arguments = listOf(navArgument("elementoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("elementoId") ?: 0
            val elemento = listaElementos.firstOrNull { it.id == id }
            if (elemento != null) {
                DetalleScreen(elemento = elemento, onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun ListaScreen(elementos: List<Elemento>, onElementoClick: (Int) -> Unit) {
    var query by remember { mutableStateOf("") }

    val filtrados = remember(query) {
        if (query.isBlank()) elementos
        else elementos.filter {
            it.titulo.contains(query, ignoreCase = true) ||
                    it.categoria.contains(query, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Catálogo de Tecnologías", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar por título o categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(filtrados, key = { it.id }) { elemento ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onElementoClick(elemento.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(elemento.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = elemento.categoria,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(elemento.descripcionCorta, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun DetalleScreen(elemento: Elemento, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text(elemento.titulo, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Categoría: ${elemento.categoria}",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(elemento.descripcionLarga, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text("Volver")
        }
    }
}