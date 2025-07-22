package com.wolverine.organix.dashboard.repositories

import com.wolverine.organix.dashboard.models.DashboardWidget
import com.wolverine.organix.dashboard.models.WidgetType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DashboardWidgetRepository : JpaRepository<DashboardWidget, UUID> {
    
    fun findByDashboardLayoutIdOrderByOrderIndexAsc(layoutId: UUID): List<DashboardWidget>
    
    fun findByDashboardLayoutIdAndIsVisibleTrueOrderByOrderIndexAsc(layoutId: UUID): List<DashboardWidget>
    
    @Query("SELECT dw FROM DashboardWidget dw WHERE dw.dashboardLayout.id = :layoutId AND dw.type = :type")
    fun findByDashboardLayoutIdAndType(@Param("layoutId") layoutId: UUID, @Param("type") type: WidgetType): List<DashboardWidget>
    
    fun findByDashboardLayoutIdAndId(layoutId: UUID, id: UUID): DashboardWidget?
    
    @Query("SELECT MAX(dw.orderIndex) FROM DashboardWidget dw WHERE dw.dashboardLayout.id = :layoutId")
    fun findMaxOrderIndexByLayoutId(@Param("layoutId") layoutId: UUID): Int?

    @Query("SELECT MAX(w.orderIndex) FROM DashboardWidget w WHERE w.dashboardLayout.id = :dashboardLayoutId AND w.isDeleted = false")
    fun findMaxDisplayOrderByDashboardLayoutId(@Param("dashboardLayoutId") dashboardLayoutId: UUID): Int?

    @Query("SELECT COUNT(w) FROM DashboardWidget w WHERE w.dashboardLayout.id = :dashboardLayoutId AND w.isDeleted = false")
    fun countByDashboardLayoutIdAndIsDeletedFalse(@Param("dashboardLayoutId") dashboardLayoutId: UUID): Long

    @Query("SELECT w FROM DashboardWidget w WHERE w.dashboardLayout.usuario.id = :userId AND w.type = :widgetType AND w.isDeleted = false")
    fun findByUserIdAndWidgetTypeAndIsDeletedFalse(@Param("userId") userId: UUID, @Param("widgetType") widgetType: WidgetType): List<DashboardWidget>

    @Query("SELECT w FROM DashboardWidget w WHERE w.dashboardLayout.usuario.id = :userId AND w.autoRefresh = true AND w.isDeleted = false")
    fun findAutoRefreshWidgetsByUserId(@Param("userId") userId: UUID): List<DashboardWidget>

    fun deleteByDashboardLayoutIdAndIsDeletedFalse(dashboardLayoutId: UUID): Int

    // Métodos adicionales necesarios para el servicio
    fun findByDashboardLayoutId(dashboardLayoutId: UUID): List<DashboardWidget>
    
    fun findByIdAndDashboardLayoutId(widgetId: UUID, dashboardLayoutId: UUID): DashboardWidget?
    
    fun findByDashboardLayoutIdAndWidgetType(dashboardLayoutId: UUID, widgetType: WidgetType): List<DashboardWidget>
}
