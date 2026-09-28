# Análisis Técnico - Taller 5: Navegación y Colecciones en Jetpack Compose

**Estudiante:** Juan Esteban González Granada  
**Asignatura:** Programación Móvil  
**Universidad:** Universidad de Caldas

---

## 1. Introducción y Objetivos

El presente informe analiza la implementación de una aplicación en Jetpack Compose orientada a la gestión de catálogos mediante listas dinámicas, filtrado en tiempo real y navegación entre pantallas. Además, se aborda la identificación y solución del problema recurrente de duplicación de pantallas en la pila de navegación (*Back Stack*).

---

## 2. Análisis de Componentes y Rendimiento

### 2.1 Uso de `LazyColumn` frente a `Column`
Para la renderización del catálogo de tecnologías se seleccionó `LazyColumn`. A diferencia de una `Column` tradicional —la cual inicializa y renderiza la totalidad de los elementos en memoria desde el primer momento—, `LazyColumn` solo procesa e infla en pantalla los elementos visibles en la vista actual (*viewport*). Esto optimiza drásticamente el consumo de memoria RAM y evita caídas de fotogramas (*lag*) cuando la colección escala a cientos o miles de registros.

### 2.2 Filtrado y Estado Dinámico
El filtrado en tiempo real se gestiona mediante un estado mutable (`rememberSaveable`) conectado a la barra de búsqueda (`OutlinedTextField`). Cada interacción del usuario actualiza la variable `query`, recalculando de forma inmediata la lista filtrada mediante operaciones sobre la colección base sin reestructurar innecesariamente la jerarquía de la UI.

---

## 3. Gestión de Navegación y Corrección de Pila (*Back Stack*)

### 3.1 Predicción del Bug de Duplicación
En aplicaciones donde el usuario interactúa frecuentemente con elementos de una lista, presionar repetidamente sobre una misma tarjeta o accionar eventos rápidos de navegación puede encolar múltiples instancias de la misma destino en el `NavController`. Como resultado, al intentar retornar a la pantalla principal presionando el botón "Volver", la aplicación obliga al usuario a retroceder a través de múltiples pantallas idénticas duplicadas en la pila.

### 3.2 Solución con `launchSingleTop = true`
Para corregir este comportamiento, la llamada al método `navigate` en la función `AppNavigation` se configuró definiendo la opción `launchSingleTop = true`:

```kotlin
onElementoClick = { id ->
    navController.navigate("detalle/$id") {
        launchSingleTop = true
    }
}