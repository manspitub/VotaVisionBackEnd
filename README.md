# 📊 VotaVision

**VotaVision** es una plataforma de encuestas online diseñada para fomentar la participación ciudadana, recolectar opiniones y generar análisis con una experiencia moderna, segura y accesible. Está desarrollada con Angular y Spring Boot, y utiliza JWT para la autenticación.

---

## 🧩 Características principales

- 🔐 Registro e inicio de sesión con autenticación JWT
- 📋 Listado de encuestas activas con buscador y paginación
- 👤 Página de perfil editable con formulario similar al de registro
- 📱 Interfaz responsive con Bootstrap 5
- ⚙️ Backend modular con controladores REST en Spring Boot
- 🗄️ PostgreSQL como base de datos principal
- 🧠 Estado de sesión gestionado con señales (`signals`) en Angular
- 📦 Despliegue mediante Docker y Jenkins

---

## 🛠️ Tecnologías utilizadas

| Tipo        | Tecnología               |
|-------------|--------------------------|
| Frontend    | Angular, Bootstrap 5, RxJS, Signals, jwt-decode |
| Backend     | Spring Boot, Spring Security, JWT, Maven |
| Base de datos | PostgreSQL             |
| Herramientas | Docker, Jenkins, Visual Studio Code |

---

## 🚀 Instalación local

### 🔧 Requisitos previos

- Node.js v18+
- Angular CLI
- Java 17+
- PostgreSQL
- Maven
- Docker (opcional)

### 💻 Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/votavision.git
cd votavision
```

### 🧑‍💻 Frontend

```bash
cd frontend
npm install
ng serve
```

### ⚙️ Backend

```bash
cd backend
./mvnw spring-boot:run
```

Asegúrate de configurar el archivo `application.properties` con los datos correctos de tu PostgreSQL.

---

## 🐳 Despliegue con Docker y Jenkins

### 🐋 Docker

```bash
# Construir imágenes
docker build -t votavision-backend ./backend
docker build -t votavision-frontend ./frontend

# Levantar los servicios
docker-compose up
```

### ⚙️ Jenkins (Manual básico)

1. Crear un `Pipeline` en Jenkins.
2. Usar un `Jenkinsfile` con etapas para:
   - Construcción de backend y frontend
   - Pruebas
   - Despliegue Docker
3. Configurar webhook con GitHub o GitLab para CI/CD.

---

## 👤 Página de perfil

- Muestra un formulario con los datos del usuario (editable)
- Presenta una encuesta aleatoria en la parte superior
- El botón “Guardar cambios” está siempre visible
- Reutiliza el diseño del formulario de registro

---

## 🔒 Seguridad

- Autenticación basada en tokens JWT
- Protección de rutas en frontend y backend
- Decodificación de token para obtener información del usuario
- Sistema reactivo con señales (`signal`) para detectar cambios de estado de sesión

---

## 📷 Capturas de pantalla

> *(Agrega aquí imágenes o GIFs mostrando las funcionalidades de la app)*

---

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas!

1. Haz un fork del repositorio
2. Crea una nueva rama (`git checkout -b nueva-funcionalidad`)
3. Realiza tus cambios y haz commit (`git commit -m 'Agrega nueva funcionalidad'`)
4. Haz push a tu rama (`git push origin nueva-funcionalidad`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto está bajo la licencia [MIT](LICENSE).

---

## 📬 Contacto

Puedes ponerte en contacto con el equipo de desarrollo abriendo un issue en este repositorio o enviando un correo a `info.votavision@gmail.com`.

---
