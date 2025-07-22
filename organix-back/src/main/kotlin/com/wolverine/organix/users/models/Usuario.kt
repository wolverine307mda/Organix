package com.wolverine.organix.users.models

import com.wolverine.organix.utils.generators.GuidGenerator
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "usuarios")
data class Usuario(

    @Id
    @Column(nullable = false, unique = true, columnDefinition = "uuid")
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    var email: String = "",

    @Column(name = "username_field", nullable = false, unique = true)
    var usernameField: String = "",

    @Column(nullable = false)
    var firstName: String = "",

    @Column(nullable = false)
    var lastName: String = "",

    @Column(nullable = true)
    var birthDate: LocalDate? = null,

    // Dirección
    @Column(nullable = true)
    var addressLine: String? = null,

    @Column(nullable = true)
    var city: String? = null,

    @Column(nullable = true)
    var postalCode: String? = null,

    @Column(nullable = true)
    var country: String? = null,

    @Column(nullable = true)
    var phone: String? = null,

    // Avatar
    @Column(columnDefinition = "TEXT", nullable = true)
    var avatarUrl: String? = null,

    // Preferencias
    @Column(nullable = false)
    var language: String = "es",

    @Column(nullable = false)
    var timezone: String = "Europe/Madrid",

    // Seguridad y login
    @Column(nullable = false)
    @get:JvmName("getPasswordField")
    var password: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var rol: RolUsuario = RolUsuario.USUARIO,

    @Column(name = "last_login_at")
    var lastLoginAt: LocalDateTime? = null,

    // Integración externa
    @Column(nullable = false)
    var googleDriveLinked: Boolean = false,

    @Column(nullable = true)
    var googleDriveRefreshToken: String? = null,

    // Layout de dashboard
    @Column(name = "dashboard_layout_id", nullable = true)
    var dashboardLayoutId: UUID? = null,

    // Reset PIN
    @Column(name = "reset_pin")
    var resetPin: String? = null,

    @Column(name = "reset_pin_expiration")
    var resetPinExpiration: LocalDateTime? = null,

    // Control interno
    @Column(nullable = false)
    var isDeleted: Boolean = false,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null

) : UserDetails {

    override fun getUsername(): String = email
    override fun getPassword(): String = password
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = !isDeleted
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${rol.name}"))
    }

    fun getNombreUsuario(): String = usernameField

    fun generateResetPin(): String {
        this.resetPin = (100000..999999).random().toString()
        this.resetPinExpiration = LocalDateTime.now().plusMinutes(15)
        return this.resetPin!!
    }

    fun isResetPinValid(pin: String): Boolean {
        return this.resetPin == pin &&
                this.resetPinExpiration != null &&
                this.resetPinExpiration!!.isAfter(LocalDateTime.now())
    }

    fun clearResetPin() {
        this.resetPin = null
        this.resetPinExpiration = null
    }
}

enum class RolUsuario {
    USUARIO,
    ADMINISTRADOR,
    SUPER_ADMINISTRADOR
}
