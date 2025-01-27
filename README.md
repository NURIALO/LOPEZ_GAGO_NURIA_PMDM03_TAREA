# Pokémon App

## Introducción
La **Pokémon App** es una aplicación diseñada para administrar y explorar información sobre una lista de Pokémons. Permite a los usuarios capturar Pokemons de una lista principal, guardarlos en una lista propia como capturarlos, y además puedes gestionarlos, borrarlos, obtener información, ect. a través de tu lista personalizada. Además, incluye opciones de configuración como selección de idioma, habilitación de eliminación de Pokémon, y autenticación segura para proteger los datos de los usuarios.

---

## Características principales
- **Autenticación**: Inicia sesión de manera segura a través de tu email y contraseña, utilizando Firebase Authentication.
- **Pokédex interactiva**: Explora una lista de Pokémon con detalles como tipos, altura y peso, Id, puedes visualizar la imagen de cada pokemon, los tipos, etc.
- **Gestión de Pokémon capturados**:
  - Visualiza y administra tu lista personalizada de Pokémon capturados.
  - Elimina Pokémon de la lista según tus preferencias.
- **Configuración personalizada**:
  - Cambia el idioma de la aplicación entre Español e Inglés.
  - Habilita o deshabilita la opción para eliminar Pokémons.
- **Interfaz amigable**: Tiene un diseño muy sencillo e intuitivo, con imágenes y tipos de Pokémon visualmente representados. antes de capturar los Pokemons, puedes ver los tipos de forma gráfica.

---

## Tecnologías utilizadas
- **Lenguaje**: Java
- **Android SDK**: Desarrollo nativo para Android.
- **Firebase**:
  - Authentication: Para la autenticación segura de usuarios (en el caso de esta aplicación mediante e-mail).
  - Firestore: Para el almacenamiento de Pokémon capturados, guarda todos los datos necesarios para recuperarlos y mostralos cuando sean necesarios.
- **Retrofit**: Para consumir la API de Pokémon y manejar las peticiones HTTP, como cargar la imagen y el resto de los datos.
- **Glide**: Para cargar imágenes de manera eficiente.
- **RecyclerView**: Para mostrar listas dinámicas de Pokémon en la Pokédex y la lista de capturados.
- **View Binding**: Para simplificar el acceso a las vistas en el diseño (Se han usado prácticamente en todas las clases .java).

---

## Instrucciones de uso
### Clonar el repositorio
1. Clona este repositorio en tu máquina local:
   ```bash
   git clone https://github.com/tu-usuario/nombre-repositorio.git
   ```
2. Una vez clonado el repositiorio, abre el proyecto en **Android Studio**.

### Configuración de dependencias
1. Asegúrate de tener instaladas las siguientes herramientas:
   - Android Studio (última versión).
   - Java JDK (versión 8 o superior).
   - Conexión a Internet para sincronizar las dependencias de Gradle.

2. Sincroniza el proyecto con Gradle:
   - Abre **Android Studio**.
   - Ve a `File > Sync Project with Gradle Files`.

### Ejecución
1. Conecta un dispositivo Android o también tienes disponilbe el uso de emuladores.
2. Presiona el botón **Run** en Android Studio para compilar y ejecutar la aplicación.
3. Puedes visualizar la lista de Pokedex disponibles, capturar los Pokémons que quieras,  y además puedes configurar la aplicación según tus preferencias!

---

## Conclusiones del desarrollador
El desarrollo de esta aplicación fue una experiencia enriquecedora que permitió integrar múltiples tecnologías modernas en un solo proyecto, aunque hay que admitir 
que enfrentarse a Android Studio no es tarea fácil. Algunos de los desafíos principales incluyeron:

- **Consumo de APIs**: Aprender a usar Retrofit para manejar peticiones HTTP y trabajar con datos JSON dinámicos (la parte más complicada ha sido la estructura de los tipos de Pokemons).
- **Firebase**: Configurar correctamente la autenticación y el almacenamiento en Firestore (Realmente esta parte fué fácil gracias a la profesora que explicó detalladamente como configurar un acceso ya predeterminado).
- **Interfaz de usuario**: Diseñar una experiencia visual atractiva e intuitiva para usuarios de todas las edades, usar recyclerviews para cargar las listas tanto de Pokemons como Pokemons capturados, configurar una pantalla
-                         de ajustes con PreferenceScreen.

### Aprendizajes clave
- Implementar arquitecturas robustas para manejar grandes volúmenes de datos de una API externa.
- Optimizar el manejo de imágenes y recursos en tiempo real utilizando Glide para que se pudieran visualizar de forma completa antes de mostrar las imágenes.
- Desarrollar funcionalidades interactivas utilizando RecyclerView y eventos dinámicos.

---



---

¡Gracias por visitar este proyecto! 😊
