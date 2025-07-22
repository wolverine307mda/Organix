package com.wolverine.organix.dashboard.models

import com.wolverine.organix.utils.generators.GuidGenerator
import com.wolverine.organix.users.models.Usuario
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "dashboard_layouts")
data class DashboardLayout(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.fromString(GuidGenerator().generarId()),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val usuario: Usuario,

    @Column(nullable = false)
    var name: String,

    @Column(columnDefinition = "text")
    var description: String? = null,

    @Column(name = "is_default", nullable = false)
    var isDefault: Boolean = false,

    @Column(name = "is_active", nullable = false)
    val isActive: Boolean = true,

    @Column(name = "grid_columns", nullable = false)
    var gridColumns: Int = 12,

    @Column(name = "grid_rows", nullable = false)
    var gridRows: Int = 8,

    @Column(name = "background_color")
    var backgroundColor: String? = null,

    @Column(name = "background_image")
    var backgroundImage: String? = null,

    @Column(name = "theme", nullable = false)
    var theme: String = "light",

    @OneToMany(mappedBy = "dashboardLayout", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val widgets: List<DashboardWidget> = mutableListOf(),

    @Column(name = "is_deleted", nullable = false)
    val isDeleted: Boolean = false,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime? = null
)