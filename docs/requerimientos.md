# 📄 Requerimientos específicos

## Requerimientos funcionales

### RF-01: Inicio de sesión
Descripción:
El sistema debe permitir a los usuarios autenticarse mediante correo electrónico y contraseña.
Entradas:

Correo electrónico
Contraseña

Procesamiento:
El sistema valida que los campos no estén vacíos y que las credenciales correspondan a una cuenta registrada.

Salidas:
Acceso a la pantalla principal si las credenciales son válidas
Mensaje de error descriptivo si las credenciales son incorrectas o los campos están vacíos

Condiciones:
La sesión debe permanecer activa en el dispositivo hasta que el usuario cierre sesión explícitamente


### RF-02: Registro de usuario
Descripción:
El sistema debe permitir a nuevos usuarios crear una cuenta mediante formulario.
Entradas:

Nombre
Correo electrónico
Contraseña

Procesamiento:
El sistema valida que el correo tenga formato válido (usuario@dominio.extensión), que la contraseña tenga mínimo 8 caracteres y que el correo no esté registrado previamente. Si todo es válido, crea la cuenta.
Salidas:

Confirmación de registro exitoso y acceso a la pantalla principal
Mensaje de error si algún dato es inválido o el correo ya existe


### RF-03: Cierre de sesión
Descripción:
El sistema debe permitir al usuario finalizar su sesión activa.

Entradas:
Selección de la opción "Cerrar sesión" por parte del usuario

Procesamiento:
El sistema finaliza la sesión activa y elimina los datos de autenticación almacenados en el dispositivo.
Salidas:

Redirección a la pantalla de inicio de sesión


### RF-04: Edición de perfil
Descripción:
El sistema debe permitir al usuario visualizar y modificar la información básica de su perfil.

Entradas:
Nombre actualizado
Foto de perfil (cámara o galería)

Procesamiento:
El sistema valida que el nombre no esté vacío, actualiza los datos del perfil y, si se proporcionó una nueva foto, la procesa y almacena reemplazando la anterior.
Salidas:

Confirmación de perfil actualizado exitosamente
Los nuevos datos se reflejan en la interfaz del usuario


### RF-05: Agregar prenda
Descripción:
El sistema debe permitir al usuario añadir prendas a su armario digital mediante fotografía tomada con la cámara del dispositivo o seleccionada desde la galería.

Entradas:
Imagen de la prenda (cámara o galería)
Categoría seleccionada por el usuario (superior, inferior, calzado, accesorio)
Color principal de la prenda
Nombre de la prenda (opcional)

Procesamiento:
El sistema procesa y comprime la imagen, asocia los atributos ingresados y almacena la prenda en el armario del usuario.

Salidas:
Confirmación de prenda añadida exitosamente
La prenda queda visible en el armario del usuario


### RF-06: Visualización del armario
Descripción:
El sistema debe permitir al usuario visualizar todas las prendas registradas en su armario digital.

Entradas:
Acceso del usuario a la sección de armario

Procesamiento:
El sistema recupera y muestra todas las prendas almacenadas del usuario, organizadas por categoría.

Salidas:
Vista en cuadrícula con las imágenes de las prendas registradas
Mensaje informativo si el armario está vacío


### RF-07: Filtrado de prendas
Descripción:
El sistema debe permitir al usuario filtrar las prendas de su armario según criterios específicos.

Entradas:
Criterio de filtrado seleccionado por el usuario (categoría, color)

Procesamiento:
El sistema recupera únicamente las prendas que correspondan al criterio seleccionado y actualiza la vista del armario.

Salidas:
Vista filtrada con las prendas que coinciden con el criterio seleccionado
Mensaje informativo si no hay prendas que coincidan con el filtro aplicado


### RF-08: Edición de prendas
Descripción:
El sistema debe permitir al usuario modificar la información de una prenda registrada.

Entradas:
Selección de una prenda existente
Nuevos valores para los campos editables (nombre, categoría, color)

Procesamiento:
El sistema actualiza los datos de la prenda con los nuevos valores ingresados.

Salidas:
Confirmación de cambios guardados exitosamente
La prenda refleja la información actualizada en el armario


### RF-09: Eliminación de prendas
Descripción:
El sistema debe permitir al usuario eliminar prendas de su armario.

Entradas:
Selección de la prenda a eliminar
Confirmación explícita del usuario mediante mensaje de advertencia

Procesamiento:
Tras la confirmación, el sistema elimina la prenda del armario y la desvincula de los outfits guardados que la contenían.

Salidas:
Mensaje de confirmación de eliminación exitosa
La prenda deja de aparecer en el armario y en los outfits asociados


### RF-10: Recomendación automática de outfits
Descripción:
El sistema debe generar automáticamente una sugerencia de outfit diario combinando prendas del armario del usuario.
Entradas:

Prendas registradas en el armario del usuario
Solicitud manual del usuario o apertura diaria de la aplicación

Procesamiento:
El sistema selecciona una combinación de prendas de distintas categorías siguiendo criterios de compatibilidad por color y categoría. La recomendación automática se genera una vez por día; el usuario puede solicitar una nueva manualmente en cualquier momento.

Salidas:
Outfit recomendado con visualización de las prendas seleccionadas
Mensaje informativo si el armario no tiene prendas suficientes para generar una combinación


### RF-11: Creación manual de outfits
Descripción:
El sistema debe permitir al usuario construir un outfit eligiendo manualmente prendas de su armario.

Entradas:
Selección de una o más prendas del armario
Nombre del outfit (opcional)

Procesamiento:
El sistema combina las prendas seleccionadas y guarda el outfit en la sección correspondiente.

Salidas:
El outfit queda guardado y visible en la sección de outfits del usuario


### RF-12: Guardado de outfits favoritos
Descripción:
El sistema debe permitir al usuario marcar outfits generados como favoritos para acceder a ellos fácilmente.

Entradas:
Selección de la opción "Guardar como favorito" sobre un outfit recomendado

Procesamiento:
El sistema registra el outfit como favorito y lo almacena en la sección de outfits del usuario.

Salidas:
Confirmación de outfit guardado
El outfit aparece en la sección de outfits favoritos


### RF-13: Historial de outfits usados
Descripción:
El sistema debe permitir al usuario registrar qué outfit utilizó en un día determinado.

Entradas:
Selección de la opción "Usar hoy" sobre un outfit
Fecha del registro (tomada automáticamente del dispositivo)

Procesamiento:
El sistema asocia el outfit seleccionado a la fecha actual y lo registra en el historial del usuario. Si ya existe un outfit registrado para ese día, el nuevo reemplaza al anterior.

Salidas:
Confirmación de registro exitoso
El outfit queda visible en el historial con su fecha correspondiente


### RF-14: Notificación de outfit del día
Descripción:
El sistema debe enviar una notificación local diaria al usuario recordándole consultar el outfit recomendado para ese día.

Entradas:
Configuración de notificaciones activada por el usuario

Procesamiento:
El sistema programa una notificación local para ser enviada una vez al día en un horario fijo. La notificación se genera únicamente si el usuario ha concedido los permisos correspondientes.

Salidas:
Notificación visible en el panel de notificaciones del dispositivo
Al seleccionar la notificación, el usuario es dirigido directamente a la sección de recomendación de outfits

# Requerimientos No Funcionales

## Usabilidad

RNF-01
El sistema debe permitir que un usuario nuevo, sin experiencia previa con la aplicación, complete el registro de una prenda (incluyendo fotografía y clasificación de atributos) en un tiempo máximo de 60 segundos, medido desde que accede a la función "Agregar prenda" hasta que el sistema confirma el guardado exitoso.

RNF-02
El sistema debe permitir que un usuario genere un outfit sugerido en un máximo de 3 toques (tap) desde la pantalla principal, sin necesidad de navegar por menús secundarios ni configuraciones previas.

RNF-03
El sistema debe mantener consistencia visual en el 100% de las pantallas, garantizando que todos los componentes de interfaz (botones, tarjetas, tipografía, iconografía y colores) se adhieran a la guía de estilos definida en el documento de diseño del proyecto. Cualquier desviación en color primario, tipografía base o tamaño de componentes interactivos se considerará incumplimiento verificable.

## Rendimiento

RNF-04
El tiempo de carga inicial de la aplicación no debe superar los 3 segundos en dispositivos gama media con conexión estándar 4G y/o WiFi o de forma offline.

RNF-05
La generación de un outfit automático no debe superar los 3 segundos desde que el usuario solicita la acción.

RNF-06
El sistema debe permitir el desplazamiento (scroll) del armario completo sin interrupciones perceptibles. El tiempo de respuesta entre el gesto de desplazamiento del usuario y la actualización visual de la lista no debe superar los 16 ms por fotograma (equivalente a 60 fps), garantizando una experiencia fluida sin saltos visibles, para armarios de hasta 300 prendas cargadas.

## Compatibilidad

RNF-07
La aplicación debe ser compatible con dispositivos Android con versión mínima Android 8.0 o superior.

RNF-08
La interfaz debe adaptarse correctamente a pantallas de entre 5.0" y 6.8" de diagonal en orientación vertical (portrait), sin pérdida de funcionalidad ni elementos visuales cortados o superpuestos. El soporte a orientación horizontal y tablets queda fuera del alcance de esta versión.

## Seguridad

RNF-09
Los datos del usuario deben almacenarse de forma segura mediante mecanismos de cifrado provistos por la plataforma Android en el almacenamiento local, y mediante los mecanismos de seguridad del servicio de backend utilizado para el almacenamiento remoto. Ningún dato sensible del usuario debe escribirse en texto plano en el almacenamiento interno ni en los registros del sistema.

RNF-10
Las imágenes de prendas almacenadas en el servicio de backend deben estar protegidas mediante reglas de acceso que restrinjan su lectura y escritura exclusivamente al usuario autenticado propietario de dichos archivos. Ningún usuario autenticado debe poder acceder a los recursos de otro usuario.

RNF-11
Las credenciales de acceso (email y contraseña) deben gestionarse a través del servicio de autenticación utilizado en el backend. La contraseña nunca debe almacenarse en texto plano ni en la base de datos local del dispositivo.

## Almacenamiento y datos

RNF-12
El sistema debe soportar el almacenamiento de hasta ### prendas por usuario sin degradación perceptible del rendimiento en las operaciones de consulta, filtrado y generación de outfits.

RNF-13
Cada imagen de prenda almacenada en el sistema no debe superar los 2 MB después de ser procesada por la aplicación. El sistema debe reducir automáticamente la resolución y calidad de la imagen antes de su almacenamiento local y subida al almacenamiento, de forma transparente para el usuario y sin solicitarle intervención manual.

RNF-14
El sistema no debe perder datos del usuario ante cierres inesperados de la aplicación (crash, kill del proceso por el sistema operativo o corte de energía).

## Escalabilidad

RNF-15
La arquitectura del sistema debe implementarse con separación en capas (Presentación, Dominio y Datos), de modo que la incorporación de nuevos módulos de procesamiento (como sugerencias basadas en machine learning o reconocimiento de imágenes) no requiera modificación de las capas de presentación ni de dominio existentes.

## Mantenibilidad

RNF-16
El código fuente debe seguir las convenciones de codificación oficiales del lenguaje de programación utilizado en el desarrollo.

RNF-17
El 80% de las clases públicas del proyecto deben contar con documentación que describa su propósito, parámetros y valor de retorno cuando aplique.

## Disponibilidad

RNF-18
El sistema debe permitir el acceso sin conexión a internet a las siguientes funcionalidades: visualización del armario completo, consulta del detalle de prendas, visualización de outfits guardados previamente y generación de outfits a partir de datos locales. La sincronización debe ejecutarse de forma automática cuando la conectividad sea restaurada, sin pérdida de los cambios realizados en modo offline.

RNF-19
Toda funcionalidad que dependa de servicios externos debe manejar los estados de error de red mostrando al usuario un mensaje descriptivo del fallo en un tiempo máximo de 2 segundos desde que se detecta la ausencia de respuesta del servicio. El sistema no debe quedar en estado bloqueado ni mostrar pantallas vacías sin retroalimentación al usuario.

## Fiabilidad

RNF-20
El sistema no debe perder datos del usuario ante cierres inesperados de la aplicación (crash, kill del proceso por el sistema operativo o corte de energía). Toda operación de escritura sobre la base de datos local debe ejecutarse dentro de transacciones atómicas.

RNF-21
Las operaciones críticas del sistema (guardar prenda, eliminar prenda, guardar outfit y autenticar usuario) deben completarse exitosamente en al menos el 99% de los intentos realizados bajo condiciones normales de operación (dispositivo con hardware mínimo soportado, conexión estable y almacenamiento local disponible).
