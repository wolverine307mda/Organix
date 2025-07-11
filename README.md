# 🧠 MyLifeBoard

**MyLifeBoard** es una aplicación web todo-en-uno de organización personal. Diseñada para ayudarte a gestionar tareas, eventos, notas, archivos y hábitos desde un único panel intuitivo y personalizable.

Desarrollada en tus tiempos libres como proyecto personal, busca ser **modular**, **extensible**, usable tanto online como offline, y enfocada en **mejorar tu productividad diaria y semanal**.

---

## 📦 Características Principales

### 🗓️ Calendario y Agenda
- Vista por día, semana y mes
- Eventos con:
  - Repetición (diaria, semanal, mensual…)
  - Recordatorios
  - Adjuntos
  - Notas o descripción en HTML/Markdown

### 📝 Bloc de Notas estilo Notion
- Edición con soporte Markdown
- Imágenes, listas, código, tablas y diagramas
- Organización por carpetas o etiquetas
- Creación de documentos enriquecidos (guardados como HTML)

### 🗂️ Gestor de Archivos Personales
- Subida y clasificación de archivos importantes
- Visualización previa (PDF, imágenes, etc.)

### ✅ Gestor de Tareas
- Tareas con subtareas
- Prioridades y etiquetas
- Estado (pendiente, en progreso, completado)
- Fechas límite y repetición

### 🔔 Recordatorios y Notificaciones
- Push Notifications + Emails
- Configuración de prioridad, frecuencia, etc.

### 🧠 Panel Personal
- Dashboard personalizable
- Resumen diario/semanal de tareas, eventos, hábitos y notas
- Widgets personalizables (añadir, mover, eliminar)

### 🖼️ Recuerdos Fotográficos
- Asociación automática de fotos con fechas pasadas (según metadatos EXIF)
- Visualización de recuerdos por día en el calendario

### 💬 Notas Rápidas / Post-its
- Mini notas visibles directamente en el dashboard

---

## ⚙️ Tecnologías

### 🧩 Frontend
- **Angular 20** (sin `NgModules`, standalone components)
- **Angular Material** + **TailwindCSS**
- **Hydratación no destructiva** + **SSG (Static Site Generation)**
- Enrutado optimizado, lazy loading por vistas

### 🚀 Backend
- **Kotlin** con **Spring Boot**
- API REST + WebSocket (futuro)
- Autenticación con JWT
- Cifrado básico de datos sensibles

### 🗃️ Base de Datos
- **PostgreSQL**
- ORM: **Exposed (Kotlin)**

### ☁️ Infraestructura
- **Docker** (contenedorización local y para producción)
- Almacenamiento de archivos: local
- Certificados SSL: Let's Encrypt
- Sugerido para VPS con backup manual

---

## 🧪 Modelos Mínimos

### Usuario
- `id`, `email`, `nombre`, `contraseñaHash`, `fechaRegistro`, `preferencias`

### Nota
- `id`, `titulo`, `contenidoMarkdown`, `fechaCreacion`, `ultimaEdicion`, `etiquetas`, `carpetaId`, `imagenes`

### Documento
- `id`, `titulo`, `contenidoHTML`, `imagenes`, `fechaCreacion`

### Evento
- `id`, `titulo`, `descripcion`, `inicio`, `fin`, `repeticion`, `recordatorios`, `adjuntos`

### Tarea
- `id`, `titulo`, `descripcion`, `fechaLimite`, `prioridad`, `estado`, `subtareas[]`, `etiquetas[]`

### Archivo
- `id`, `nombre`, `ruta`, `tipoMime`, `fechaSubida`, `usuarioId`

### Recuerdo (foto con metadatos)
- `id`, `imagenPath`, `fechaOriginal`, `ubicacion`, `eventoRelacionado`

---

## 🧱 Estado del Proyecto

> ⚠️ Proyecto personal en desarrollo activo.  
> Desarrollado en tiempos libres con el objetivo de tener una herramienta útil, extensible y potente para la organización diaria.

---

## 📌 Objetivos Futuros

- Sistema de hábitos y estadísticas
- Integración con calendario externo (Google Calendar)
- Copias de seguridad exportables
- Sincronización multi-dispositivo
- Modo offline con IndexedDB
- IA para sugerencias y resúmenes

---

## 🧑‍💻 Autor

- **Mario Domingo Álvarez**
- GitHub: [wolverine307mda](https://github.com/wolverine307mda)
- Email: dedomingoalvaremario@gmail.com
