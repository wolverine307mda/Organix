package com.wolverine.organix.utils.email

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val emailSender: JavaMailSender
) {
    fun sendSimpleEmail(to: String, subject: String, htmlContent: String) {
        val message: MimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(htmlContent, true)
        emailSender.send(message)
    }
    fun sendResetPinEmail(to: String, pin: String) {
        val subject = "Reset Your PIN"
        val htmlContent = """
            <html>
                <body>
                    <h1>Reset Your PIN</h1>
                    <p>Your reset PIN is: <strong>$pin</strong></p>
                    <p>Please use this PIN to reset your account.</p>
                </body>
            </html>
        """.trimIndent()
        sendSimpleEmail(to, subject, htmlContent)
    }
}