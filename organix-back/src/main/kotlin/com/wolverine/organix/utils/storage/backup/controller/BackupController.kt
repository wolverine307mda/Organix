package com.wolverine.organix.utils.storage.backup.controller

import jakarta.annotation.security.PermitAll
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import com.wolverine.organix.utils.storage.backup.service.BackupService

@RestController
@PermitAll
@RequestMapping("/api/backups")
@PreAuthorize("hasRole('ADMINISTRADOR')")
class BackupController(
    private val backupService: BackupService
) {

    @GetMapping("/export")
    fun exportBackup(): ResponseEntity<FileSystemResource> {
        val file = backupService.exportBackup()
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${file.name}")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(FileSystemResource(file))
    }

    @GetMapping
    fun listBackups(): List<String> =
        backupService.listBackups().map { it.name }

    @GetMapping("/download/{fileName}")
    fun downloadBackup(@PathVariable fileName: String): ResponseEntity<FileSystemResource> {
        val file = backupService.getBackupFile(fileName)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${file.name}")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(FileSystemResource(file))
    }

    @PostMapping("/import")
    fun importBackupByFileName(@RequestParam("filename") filename: String): ResponseEntity<String> {
        val file = backupService.getBackupFile(filename)
        val result = backupService.importBackup(file)
        return ResponseEntity.ok(result)
    }
}
