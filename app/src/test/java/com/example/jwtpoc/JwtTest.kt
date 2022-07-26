package com.example.jwtpoc

import com.auth0.android.jwt.JWT
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.Duration
import kotlin.system.measureTimeMillis

@RunWith(RobolectricTestRunner::class)
class JwtTest {

    @Test
    fun validJwt() {
        // Given
        // 2023 July 26
        val token = """
            eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE2OTAzNTA0MTN9.BzVXnrzCILC6lbNn91j4A1GePykWuiCt06-DiOZaZ1w
        """.trimIndent()

        // When
        val jwt = JWT(token)

        // Then
        assertNotNull(jwt.expiresAt)
        val range = jwt.expiresAt!!.toInstant().toEpochMilli() - System.currentTimeMillis()
        assertTrue(range > ONE_WEEK)
    }

    @Test
    fun expiredJwt() {
        // Given
        // 2021 July 26
        val token = """
            eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE2MjcyNzg0MTN9.b76RGu6WcoUSN3hNgxzdv-k2pBsznHD9fwZGsN4_ZJM
        """.trimIndent()

        // When
        val jwt = JWT(token)

        // Then
        assertNotNull(jwt.expiresAt)
        val range = jwt.expiresAt!!.toInstant().toEpochMilli() - System.currentTimeMillis()
        assertFalse(range > ONE_WEEK)
    }

    @Test
    fun measureTimeForEachCheck() {
        fun parse() = run {
            val token = """
                eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE2MjcyNzg0MTN9.b76RGu6WcoUSN3hNgxzdv-k2pBsznHD9fwZGsN4_ZJM
            """.trimIndent()
            JWT(token)
        }

        val averageMillis: Double = List(100) {
            measureTimeMillis {
                parse()
            }
        }.average()

        assertTrue(averageMillis < 1)
    }

    companion object {
        val ONE_WEEK = Duration.ofDays(7).toMillis()
    }
}
