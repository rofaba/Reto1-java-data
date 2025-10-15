HU-01 — Iniciar sesión

Como usuario, quiero iniciar sesión con mi email y contraseña para acceder a mi colección de películas.
Criterios de aceptación:

Si las credenciales son válidas, accedo al Listado.

Si no, veo un mensaje de error claro.

No se muestran películas de otros usuarios.

HU-02 — Ver listado de mis películas

Como usuario autenticado, quiero ver todas mis películas en una tabla para revisar rápidamente mi colección.
Criterios de aceptación:

Tabla con Título, Año, Director, Género (y acceso a detalle).

Solo carga películas asociadas a mi usuario.

Orden y scroll básicos.

HU-03 — Ver detalle de una película

Como usuario, quiero abrir una vista de detalle para ver Descripción y el cartel (Imagen/URL) de la película seleccionada.
Criterios de aceptación:

Muestra Título, Año, Director, Descripción, Género, Imagen.

Botón Volver al listado.

HU-04 — Añadir una nueva película

Como usuario, quiero añadir una película rellenando un formulario mínimo para ampliar mi colección.
Criterios de aceptación:

Obligatorios: Título, Año, Director, Género (Descripción/Imagen opcionales).

La nueva película queda asociada a mi usuario y aparece en el listado.

Validaciones básicas (año numérico, campos no vacíos).

HU-05 — Eliminar una película

Como usuario, quiero eliminar una película seleccionada para mantener actualizada mi colección.
Criterios de aceptación:

Requiere confirmación.

Tras eliminar, la fila desaparece del listado.

No puedo eliminar películas que no son mías.

HU-06 — Cerrar sesión

Como usuario, quiero cerrar sesión para salir de la aplicación de forma segura.
Criterios de aceptación:

Regresa a la pantalla de Login.

Se limpia el estado del usuario.

HU-07 — Persistencia local en CSV (no funcional orientada a usuario)

Como usuario, quiero que mis altas y bajas queden guardadas en archivos locales CSV para no perder cambios al reiniciar.
Criterios de aceptación:

usuarios.csv: Id, Email, Contraseña.

peliculas.csv: Id, Título, Año, Director, Descripción, Género, Imagen, UserId.

Añadir/Eliminar se refleja en los CSV.

HU-08 — Navegación clara entre pantallas (usabilidad)

Como usuario, quiero una navegación simple entre Login → Listado → Detalle para usar la app sin confusión.
Criterios de aceptación:

Tres pantallas clave: Login, Listado, Detalle.

Controles visibles para Añadir, Eliminar, Detalle y Volver.

HU-09 — Interfaz simple y accesible (usabilidad)

Como usuario, quiero una interfaz clara, intuitiva y accesible para completar mis tareas sin esfuerzo.
Criterios de aceptación:

Etiquetas y mensajes comprensibles.

Estados vacíos y errores gestionados con mensajes breves.

Componentes Swing estándar y coherentes con el mockup.