# 🧠 MyLifeBoard — App de Organización Personal Todo-en-Uno

**MyLifeBoard** es una aplicación full stack para ayudarte a organizar tu vida de manera eficiente. Combina calendario, recordatorios, gestión de archivos, notas tipo Notion, hábitos y más. Pensada para usuarios que buscan productividad, claridad mental y control digital total.

---

## 🚀 Tecnologías Usadas

### 🧩 Frontend
- **Angular 17** — Framework principal
- **Angular Material** — Componentes UI modernos y accesibles
- **RxJS** — Gestión reactiva de datos
- **NgRx** *(futuro)* — Para manejar estado global si crece la app
- **PWA** *(futuro)* — Para modo offline y notificaciones

### 🔧 Backend
- **Kotlin** — Lenguaje del backend
- **Ktor** *(o Spring Boot)* — Framework HTTP (según decisión final)
- **Exposed** — ORM Kotlin para PostgreSQL
- **Kotlinx Serialization / Moshi** — Serialización JSON
- **Kotlin Mail API / JavaMail** — Envío de emails

### 🛢️ Base de Datos
- **PostgreSQL** — Base de datos relacional principal
- **Flyway** — Migraciones versionadas
- **Docker** *(dev)* — Contenedores para PostgreSQL y backend

### ☁️ Otros
- **Git / GitHub** — Control de versiones
- **Swagger** — Documentación de APIs
- **Markdown + Mermaid** — Para notas, diagramas y documentación

---

## 📦 Funcionalidades Principales

### 📅 Calendario y Agenda
- Crear eventos con título, descripción, fecha/hora
- Subir imágenes/documentos asociados a un evento
- Vista por día/semana/mes
- Repeticiones y recordatorios personalizables

### 📝 Bloc de Notas estilo Notion
- Editor Markdown con soporte para:
  - Checklists, tablas, imágenes
  - Diagramas (Mermaid, PlantUML)
- Organización por etiquetas/carpetas
- Exportación a `.md` o `.pdf`

### 🗂️ Gestor de Archivos
- Subida de archivos importantes (PDFs, imágenes, etc.)
- Clasificación por tipo, fecha y categoría
- Visualización integrada
- Seguridad y backup

### 🔔 Recordatorios y Notificaciones
- Alertas para eventos, tareas o notas
- Notificaciones por email y push
- Repeticiones automáticas y prioridad

### ✅ Gestor de Tareas
- Tareas con subtareas, prioridad, vencimiento
- Vistas tipo lista o Kanban (futuro)
- Relación opcional con eventos del calendario

### 📊 Módulo de Estadísticas y Hábitos
- Crear hábitos personalizados (ej: beber agua, meditar)
- Seguimiento diario/semanal con gráficos
- Análisis de progreso con charts
- Integración con el calendario o tareas
- Soporte para objetivos (ej: “Leer 12 libros este año”)

### 🧠 Panel Personal
- Vista rápida del día: eventos, tareas, notas destacadas
- Sugerencias inteligentes (eventos sin completar, hábitos pendientes)
- Resumen semanal automático

### 💬 Notas Rápidas
- “Sticky notes” tipo post-it en el panel
- Autoguardado
- Recordatorios

---

## 🛡️ Seguridad (MVP)
- Usuario único (versión inicial)
- Token JWT para autenticación de API
- Cifrado de archivos sensibles (futuro)
- Copias de seguridad locales/exportables

---

## 📈 Roadmap (próximas fases)
- [ ] Editor visual de tareas estilo Kanban
- [ ] App móvil con Ionic o Capacitor
- [ ] Modo colaborativo (compartir notas, tareas)
- [ ] Plugins o extensiones personales
- [ ] IA para sugerencias de organización

---

## 🧑‍💻 Autor
- Mario De Domingo Álvarez (@wolverine307mda)  
- ✉️ dedomingoalvaremario@gmail.com

---

## 📝 Licencia
Este proyecto está licenciado bajo MIT — eres libre de usarlo, modificarlo y mejorarlo. ¡Comparte y mejora!

