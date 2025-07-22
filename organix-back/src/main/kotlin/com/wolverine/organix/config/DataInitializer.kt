package com.wolverine.organix.config

import com.wolverine.organix.users.models.RolUsuario
import com.wolverine.organix.users.models.Usuario
import com.wolverine.organix.users.repositories.UsuarioRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class DataInitializer(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        createDefaultUsers()
    }

    private fun createDefaultUsers() {
        // Crear usuario administrador de prueba
        if (!usuarioRepository.existsByEmail("wolverine.mda.307@gmail.com")) {
            val adminUser = Usuario()
            adminUser.email = "wolverine.mda.307@gmail.com"
            adminUser.usernameField = "admin"
            adminUser.firstName = "Mario"
            adminUser.lastName = "de Domingo"
            adminUser.birthDate = LocalDate.of(1990, 1, 1)
            adminUser.addressLine = "Calle Principal 123"
            adminUser.city = "Madrid"
            adminUser.postalCode = "28001"
            adminUser.country = "España"
            adminUser.phone = "+34 600 123 456"
            adminUser.password = passwordEncoder.encode("30072004")
            adminUser.rol = RolUsuario.ADMINISTRADOR
            adminUser.language = "es"
            adminUser.timezone = "Europe/Madrid"
            adminUser.googleDriveLinked = false
            adminUser.isDeleted = false
            
            usuarioRepository.save(adminUser)
            println("✅ Usuario administrador creado: wolverine.mda.307@gmail.com / 30072004")
        }

        // Crear usuario de prueba adicional
        if (!usuarioRepository.existsByEmail("test@organix.com")) {
            val testUser = Usuario()
            testUser.email = "test@organix.com"
            testUser.usernameField = "test"
            testUser.firstName = "Test"
            testUser.lastName = "User"
            testUser.birthDate = LocalDate.of(1992, 8, 22)
            testUser.addressLine = "Calle Test 789"
            testUser.city = "Valencia"
            testUser.postalCode = "46001"
            testUser.country = "España"
            testUser.phone = "+34 600 987 654"
            testUser.password = passwordEncoder.encode("test123")
            testUser.rol = RolUsuario.USUARIO
            testUser.language = "es"
            testUser.timezone = "Europe/Madrid"
            testUser.googleDriveLinked = false
            testUser.isDeleted = false
            
            usuarioRepository.save(testUser)
            println("✅ Usuario de prueba creado: test@organix.com / test123")
        }

        println("🔧 Inicialización de datos completada")
    }
}
