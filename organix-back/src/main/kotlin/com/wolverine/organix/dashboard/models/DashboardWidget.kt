package com.wolverine.organix.dashboard.models

import com.wolverine.organix.utils.generators.GuidGenerator
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "dashboard_widgets")
data class DashboardWidget(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.fromString(GuidGenerator().generarId()),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dashboard_layout_id", nullable = false)
    val dashboardLayout: DashboardLayout,

    @Column(name = "widget_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val type: WidgetType,

    @Column(nullable = false)
    var title: String,

    @Column(columnDefinition = "text")
    var description: String? = null,

    // Posición en el grid
    @Column(name = "grid_x", nullable = false)
    var x: Int,

    @Column(name = "grid_y", nullable = false)
    var y: Int,

    @Column(name = "grid_width", nullable = false)
    var width: Int,

    @Column(name = "grid_height", nullable = false)
    var height: Int,

    // Orden de visualización
    @Column(name = "display_order", nullable = false)
    var orderIndex: Int = 0,

    // Configuración específica del widget (JSON)
    @Column(name = "widget_config", columnDefinition = "text")
    var configuration: String? = null,

    // Estilo personalizado
    @Column(name = "custom_style", columnDefinition = "text")
    val customStyle: String? = null,

    @Column(name = "background_color")
    val backgroundColor: String? = null,

    @Column(name = "text_color")
    val textColor: String? = null,

    @Column(name = "border_color")
    val borderColor: String? = null,

    @Column(name = "border_width")
    val borderWidth: Int = 1,

    @Column(name = "border_radius")
    val borderRadius: Int = 8,

    // Opciones de comportamiento
    @Column(name = "is_resizable", nullable = false)
    val isResizable: Boolean = true,

    @Column(name = "is_draggable", nullable = false)
    val isDraggable: Boolean = true,

    @Column(name = "is_removable", nullable = false)
    val isRemovable: Boolean = true,

    @Column(name = "is_visible", nullable = false)
    var isVisible: Boolean = true,

    @Column(name = "is_minimized", nullable = false)
    var isMinimized: Boolean = false,

    @Column(name = "auto_refresh", nullable = false)
    val autoRefresh: Boolean = false,

    @Column(name = "refresh_interval")
    val refreshInterval: Int? = null, // en segundos

    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean = false,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime? = null
)

enum class WidgetType {
    WELCOME,
    TASKS_SUMMARY,          // Resumen de tareas pendientes
    CALENDAR_EVENTS,        // Próximos eventos
    QUICK_NOTES,           // Notas rápidas/post-its
    WEATHER,               // Clima
    CLOCK,                 // Reloj
    HABITS_TRACKER,        // Seguimiento de hábitos
    RECENT_DOCUMENTS,      // Documentos recientes
    PHOTO_MEMORIES,        // Recuerdos fotográficos
    QUICK_STATS,           // Estadísticas rápidas
    REMINDERS,             // Recordatorios
    RECENT_ACTIVITIES,     // Actividades recientes
    CUSTOM_HTML,           // Widget personalizado HTML
    NOTES_PREVIEW,         // Vista previa de notas
    FILE_MANAGER,          // Gestor de archivos
    GOOGLE_CALENDAR,       // Integración Google Calendar
    MOTIVATIONAL_QUOTE,    // Cita motivacional
    PROGRESS_TRACKER,      // Seguimiento de progreso
    NOTIFICATIONS,         // Notificaciones
    SEARCH_WIDGET,         // Widget de búsqueda
    SHORTCUT_MENU,
    POMODORO_TIMER,
    WEATHER_WIDGET,
    RSS_FEED,
    SHORTCUTS,
    TASKS,
    NOTES,
    RECENT_ACTIVITY,
    CALENDAR
}
