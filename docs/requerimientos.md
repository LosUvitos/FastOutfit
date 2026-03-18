# 📄 3. Requerimientos específicos

## 3.1 Requerimientos funcionales

### **RF-01: Autenticación de usuario**
**Descripción:**  
El sistema debe permitir a los usuarios iniciar sesión mediante correo electrónico y contraseña, o mediante autenticación con Google.

**Entradas:**  
- Correo electrónico  
- Contraseña  
- Credenciales de Google  

**Procesamiento:**  
El sistema validará las credenciales ingresadas contra el servicio de autenticación correspondiente.

**Salidas:**  
- Acceso al sistema si las credenciales son válidas  
- Mensaje de error en caso contrario  

**Postcondición:**  
La sesión del usuario debe permanecer almacenada en el dispositivo móvil, evitando solicitar credenciales nuevamente en futuros accesos.

---

### **RF-02: Registro de usuario**
**Descripción:**  
El sistema debe permitir a nuevos usuarios registrarse mediante formulario o autenticación con Google.

**Entradas:**  
- Nombre  
- Correo electrónico  
- Contraseña  
- Credenciales de Google  

**Condiciones** 
- La logitud minima de contraseña debe ser de 8 caracteres
- El correo electrónico debe semanticamente validado

**Procesamiento:**  
El sistema validará los datos ingresados y creará una nueva cuenta de usuario.

**Salidas:**  
- Confirmación de registro exitoso  
- Mensaje de error si los datos son inválidos o el usuario ya existe  


---

### **RF-03: Recomendación de outfits**
**Descripción:**  
El sistema debe recomendar al usuario combinaciones de ropa.

**Entradas:**  
- Datos del usuario  
- Prendas registradas en el armario  

**Procesamiento:**  
El sistema generará una combinación de prendas basada en criterios definidos.

**Salidas:**  
- Outfit recomendado  

**Condiciones:**  
- La recomendación debe generarse automáticamente una vez al día  
- El usuario también podrá solicitar recomendaciones manualmente en cualquier momento  

---

### **RF-04: Gestión de prendas mediante cámara**
**Descripción:**  
El sistema debe permitir al usuario añadir prendas a su armario utilizando la cámara del dispositivo.

**Entradas:**  
- Imagen capturada por la cámara  
- Categoria selecccionada por el usuario(legs, feet, chest, head)

**Procesamiento:**  
El sistema procesará la imagen y almacenará la prenda en el armario del usuario.

**Salidas:**  
- Confirmación de prenda añadida  
- Visualización de la prenda en el armario  
---
# 3.2 Requerimientos No Funcionales

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