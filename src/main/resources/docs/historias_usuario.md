Historias de Usuario
=====================
**HU-01 — Iniciar sesión**

Como usuario registrado, quiero acceder a la aplicación introduciendo mi correo y contraseña para poder gestionar mi colección personal de películas.

✅ Criterios de aceptación:

Si las credenciales son válidas, se muestra el listado de películas del usuario.

Si las credenciales son incorrectas, aparece un mensaje de error claro.

No se muestran películas de otros usuarios.

El formulario impide dejar campos vacíos.

**HU-02 — Visualizar mis películas**

Como usuario autenticado, quiero ver todas mis películas en una tabla para revisar y gestionar fácilmente mi colección.

✅ Criterios de aceptación:

La tabla muestra las columnas: Título, Año, Director y Género.

Solo se cargan películas asociadas al usuario activo.

Se permite hacer scroll si hay muchas películas.

Si no hay películas, se muestra una lista vacía.

**HU-03 — Ver detalle de una película**

Como usuario, quiero ver los detalles completos de una película seleccionada para conocer su descripción y ver su cartel.

✅ Criterios de aceptación:

Se muestran Título, Año, Director, Descripción, Género e Imagen (URL).

Existe un botón o acción para volver al listado.

Si no hay imagen disponible, se muestra un mensaje.

**HU-04 — Añadir una nueva película**

Como usuario, quiero añadir una nueva película mediante un formulario para ampliar mi colección personal.

✅ Criterios de aceptación:

Campos obligatorios: Título, Año, Director y Género.

Campos opcionales: Descripción e Imagen (URL).

Validaciones: el año debe ser numérico y los campos obligatorios no pueden estar vacíos.

Al guardar, la nueva película aparece inmediatamente en el listado asociada al usuario actual.

Si se cancela, no se realiza ningún cambio.

**HU-05 — Eliminar una película**

Como usuario, quiero eliminar una película seleccionada para mantener mi colección actualizada.

✅ Criterios de aceptación:

Se solicita confirmación antes de eliminar.

Una vez eliminada, la película desaparece inmediatamente del listado.

No es posible eliminar películas que pertenecen a otro usuario.

Si ocurre un error, se muestra un mensaje claro.

**HU-06 — Cerrar sesión**

Como usuario, quiero cerrar sesión para salir de forma segura y proteger mi información.

✅ Criterios de aceptación:

Al cerrar sesión, se vuelve a la pantalla de inicio de sesión.

Se limpia la información del usuario activo.

No se pueden ver películas sin haber iniciado sesión.

**HU-07 — Conservar mis cambios**

Como usuario, quiero que al volver a abrir la aplicación mi colección siga igual, para no perder las películas que añadí o eliminé.

✅ Criterios de aceptación:

Tras cerrar y abrir la aplicación, las altas y bajas anteriores se ven reflejadas.

No se pierden cambios si cierro desde el listado o desde el detalle.


# Requisitos No Funcionales
=============================

**Navegación clara**

Como usuario, quiero poder desplazarme fácilmente entre las pantallas principales para entender en todo momento dónde estoy y cómo volver atrás.

✅ Criterios de aceptación:

Pantallas principales: Login, Listado y Detalle.

Controles visibles para Añadir, Eliminar, Ver detalle y Volver.

Transiciones claras entre vistas.

**Interfaz simple y accesible**

Como usuario, quiero una interfaz clara, coherente y accesible para poder usar la aplicación sin dificultad.

✅ Criterios de aceptación:

Etiquetas y mensajes breves y comprensibles.

Estados vacíos y errores gestionados con claridad.

Uso de componentes Swing estándar y diseño coherente en toda la aplicación.

