package com.bullion.bulliontestcase.helper

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object PasswordEncryption {

    fun encryptPassword(password: String): String {
        try {
            // Create MessageDigest instance for SHA-256
            val md = MessageDigest.getInstance("SHA-256")
            // Add password bytes to digest
            md.update(password.toByteArray())
            // Get the hashed bytes
            val hashedBytes = md.digest()

            // Convert byte array to hexadecimal string
            val sb = StringBuilder()
            for (b in hashedBytes) {
                sb.append(String.format("%02x", b))
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            // Handle NoSuchAlgorithmException
            return ""
        }
    }
}

