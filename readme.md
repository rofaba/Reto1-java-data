🎬 Aplicación de Gestión de Películas

Aplicación de escritorio desarrollada en Java Swing que permite gestionar una colección personal de películas mediante una interfaz sencilla y funcional.
El sistema permite iniciar sesión, visualizar, añadir, eliminar y consultar los detalles de las películas, con persistencia local en archivos CSV.

🚀 Funcionalidades principales

Inicio de sesión con validación de usuario.

Visualización del listado de películas propias.

Detalle de cada película (año, director, descripción, imagen, género).

Alta y eliminación de películas.

Cierre de sesión y carga persistente desde archivos CSV.

🧩 Tecnologías utilizadas

Java 17

Swing

Maven

CSV (Apache Commons CSV)

Arquitectura MVC simplificada

📂 Estructura del proyecto
/ (raíz del proyecto)
│
├── src/
│   ├── main/java/org/example/
│   │   ├── model/           → Clases de dominio (Pelicula, Usuario)
│   │   ├── infra/           → Repositorios y contexto de sesión
│   │   └── view/            → Vistas Swing (Login, MainFrame, etc.)
│
├── data/
│   ├── peliculas.csv
│   └── usuarios.csv
│
├── pom.xml
└── README.md

🧠 Autenticación

Los usuarios se gestionan en el archivo usuarios.csv con los campos:

id,email,password


Cada película se asocia a un usuario mediante su userId.

💾 Persistencia

La información se almacena en:

usuarios.csv → Usuarios del sistema.

peliculas.csv → Películas del usuario autenticado.

Las operaciones de alta y baja se reflejan automáticamente en estos archivos.

👤 Autor
Rodrigo Faure Bascur
📚 Proyecto correspondiente al módulos de Desarrollo de Interfaces y Acceso a Datos (DAM 2 - CESUR Málaga Este).
Docente de referencia: Francisco Romero