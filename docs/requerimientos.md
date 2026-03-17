# 📄 3. Requisitos específicos

## 3.1 Requisitos funcionales

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

**Procesamiento:**  
El sistema procesará la imagen y almacenará la prenda en el armario del usuario.

**Salidas:**  
- Confirmación de prenda añadida  
- Visualización de la prenda en el armario  
