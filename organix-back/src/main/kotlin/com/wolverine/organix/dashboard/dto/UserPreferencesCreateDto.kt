package com.wolverine.organix.dashboard.dto

import java.util.*

data class UserPreferencesCreateDto(
    val userId: UUID,
    val dashboardTheme: String?,
    val sidebarCollapsed: Boolean?,
    val sidebarPosition: String?,
    val calendarView: String?,
    val calendarStartHour: Int?,
    val calendarEndHour: Int?,
    val calendarFirstDayOfWeek: Int?,
    val tasksDefaultPriority: String?,
    val tasksAutoArchive: Boolean?,
    val tasksShowCompleted: Boolean?,
    val tasksSortBy: String?,
    val tasksSortOrder: String?,
    val notificationsEnabled: Boolean?,
    val emailNotifications: Boolean?,
    val pushNotifications: Boolean?,
    val notificationSound: String?,
    val notificationSoundFile: String?,
    val notesDefaultFormat: String?,
    val notesAutoSave: Boolean?,
    val notesAutoSaveInterval: Int?,
    val filesDefaultView: String?,
    val filesShowHidden: Boolean?,
    val filesSortBy: String?,
    val filesSortOrder: String?,
    val ui: UiPreferencesDto?,
    val dataExportFormat: String?,
    val autoBackup: Boolean?,
    val autoBackupFrequency: String?,
    val pomodoroWorkDuration: Int?,
    val pomodoroShortBreak: Int?,
    val pomodoroLongBreak: Int?,
    val dailyGoalTasks: Int?,
    val weeklyGoalTasks: Int?,
    val customPreferences: Map<String, Any?>?
)

data class UiPreferencesDto(
    val density: String?,
    val animations: Boolean?,
    val fontSize: Int?,
    val highContrast: Boolean?
)

