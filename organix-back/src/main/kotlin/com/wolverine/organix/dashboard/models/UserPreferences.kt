package com.wolverine.organix.dashboard.models

import com.wolverine.organix.utils.generators.GuidGenerator
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "user_preferences")
data class UserPreferences(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.fromString(GuidGenerator().generarId()),

    @Column(name = "user_id", nullable = false, unique = true, columnDefinition = "uuid")
    val userId: UUID,

    // Configuración de Dashboard
    @Column(name = "default_dashboard_layout_id", columnDefinition = "uuid")
    val defaultDashboardLayoutId: UUID? = null,

    @Column(name = "dashboard_theme", nullable = false)
    val dashboardTheme: String = "light",

    @Column(name = "sidebar_collapsed", nullable = false)
    val sidebarCollapsed: Boolean = false,

    @Column(name = "sidebar_position", nullable = false)
    val sidebarPosition: String = "left", // left, right

    // Configuración de Calendario
    @Column(name = "calendar_view", nullable = false)
    val calendarView: String = "month", // day, week, month

    @Column(name = "calendar_start_hour", nullable = false)
    val calendarStartHour: Int = 8,

    @Column(name = "calendar_end_hour", nullable = false)
    val calendarEndHour: Int = 20,

    @Column(name = "calendar_first_day_of_week", nullable = false)
    val calendarFirstDayOfWeek: Int = 1, // 0 = Sunday, 1 = Monday

    // Configuración de Tareas
    @Column(name = "tasks_default_priority", nullable = false)
    @Enumerated(EnumType.STRING)
    val tasksDefaultPriority: com.wolverine.organix.tasks.model.TaskPriority = com.wolverine.organix.tasks.model.TaskPriority.MEDIUM,

    @Column(name = "tasks_auto_archive", nullable = false)
    val tasksAutoArchive: Boolean = true,

    @Column(name = "tasks_show_completed", nullable = false)
    val tasksShowCompleted: Boolean = true,

    @Column(name = "tasks_sort_by", nullable = false)
    val tasksSortBy: String = "dueDate", // dueDate, priority, createdAt, title

    @Column(name = "tasks_sort_order", nullable = false)
    val tasksSortOrder: String = "asc", // asc, desc

    // Configuración de Notificaciones
    @Column(name = "notifications_enabled", nullable = false)
    val notificationsEnabled: Boolean = true,

    @Column(name = "email_notifications", nullable = false)
    val emailNotifications: Boolean = true,

    @Column(name = "push_notifications", nullable = false)
    val pushNotifications: Boolean = true,

    @Column(name = "notification_sound", nullable = false)
    val notificationSound: Boolean = true,

    @Column(name = "notification_sound_file")
    val notificationSoundFile: String? = null,

    // Configuración de Notas
    @Column(name = "notes_default_format", nullable = false)
    val notesDefaultFormat: String = "markdown", // markdown, html, text

    @Column(name = "notes_auto_save", nullable = false)
    val notesAutoSave: Boolean = true,

    @Column(name = "notes_auto_save_interval", nullable = false)
    val notesAutoSaveInterval: Int = 30, // segundos

    // Configuración de Archivos
    @Column(name = "files_default_view", nullable = false)
    val filesDefaultView: String = "grid", // grid, list

    @Column(name = "files_show_hidden", nullable = false)
    val filesShowHidden: Boolean = false,

    @Column(name = "files_sort_by", nullable = false)
    val filesSortBy: String = "name", // name, size, date, type

    @Column(name = "files_sort_order", nullable = false)
    val filesSortOrder: String = "asc", // asc, desc

    // Configuración de Interfaz
    @Column(name = "ui_density", nullable = false)
    val uiDensity: String = "comfortable", // compact, comfortable, spacious

    @Column(name = "ui_animations", nullable = false)
    val uiAnimations: Boolean = true,

    @Column(name = "ui_font_size", nullable = false)
    val uiFontSize: String = "medium", // small, medium, large

    @Column(name = "ui_high_contrast", nullable = false)
    val uiHighContrast: Boolean = false,

    // Configuración de Privacidad
    @Column(name = "data_export_format", nullable = false)
    val dataExportFormat: String = "json", // json, csv, xml

    @Column(name = "auto_backup", nullable = false)
    val autoBackup: Boolean = true,

    @Column(name = "auto_backup_frequency", nullable = false)
    val autoBackupFrequency: String = "weekly", // daily, weekly, monthly

    // Configuración de Productividad
    @Column(name = "pomodoro_work_duration", nullable = false)
    val pomodoroWorkDuration: Int = 25, // minutos

    @Column(name = "pomodoro_short_break", nullable = false)
    val pomodoroShortBreak: Int = 5, // minutos

    @Column(name = "pomodoro_long_break", nullable = false)
    val pomodoroLongBreak: Int = 15, // minutos

    @Column(name = "daily_goal_tasks", nullable = false)
    val dailyGoalTasks: Int = 5,

    @Column(name = "weekly_goal_tasks", nullable = false)
    val weeklyGoalTasks: Int = 30,

    // Configuración JSON para preferencias adicionales
    @Column(name = "custom_preferences", columnDefinition = "text")
    val customPreferences: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime? = null
)
