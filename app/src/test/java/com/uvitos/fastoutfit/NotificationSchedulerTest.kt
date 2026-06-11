package com.uvitos.fastoutfit

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.uvitos.fastoutfit.notifications.NotificationReceiver
import com.uvitos.fastoutfit.notifications.NotificationScheduler
import io.mockk.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class NotificationSchedulerTest {

    private lateinit var mockContext: Context
    private lateinit var mockAlarmManager: AlarmManager
    private lateinit var mockPendingIntent: PendingIntent

    @Before
    fun setup() {
        mockContext      = mockk(relaxed = true)
        mockAlarmManager = mockk(relaxed = true)
        mockPendingIntent = mockk(relaxed = true)

        every { mockContext.packageName } returns "com.uvitos.fastoutfit"
        every { mockContext.getSystemService(Context.ALARM_SERVICE) } returns mockAlarmManager

        mockkStatic(PendingIntent::class)
        every {
            PendingIntent.getBroadcast(any(), any(), any(), any())
        } returns mockPendingIntent
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    // ── schedule() ─────────────────────────────────────────────────────────────

    @Test
    fun `schedule - llama a setRepeating con intervalo de un dia`() {
        NotificationScheduler.schedule(mockContext, hour = 8, minute = 0)

        verify(exactly = 1) {
            mockAlarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                any(),
                AlarmManager.INTERVAL_DAY,
                mockPendingIntent,
            )
        }
    }

    @Test
    fun `schedule - usa REQUEST_CODE 2001`() {
        NotificationScheduler.schedule(mockContext, hour = 8, minute = 0)

        verify {
            PendingIntent.getBroadcast(
                mockContext,
                2001,
                any(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }
    }

    @Test
    fun `schedule - programa en el futuro`() {
        val capturedTime = slot<Long>()

        every {
            mockAlarmManager.setRepeating(any(), capture(capturedTime), any(), any())
        } just Runs

        NotificationScheduler.schedule(mockContext, hour = 23, minute = 59)

        assertTrue(
            "El trigger debe ser mayor que ahora",
            capturedTime.captured > System.currentTimeMillis()
        )
    }

    @Test
    fun `schedule - si la hora ya paso hoy programa para manana`() {
        val capturedTime = slot<Long>()

        every {
            mockAlarmManager.setRepeating(any(), capture(capturedTime), any(), any())
        } just Runs

        NotificationScheduler.schedule(mockContext, hour = 0, minute = 0)

        assertTrue(
            "Debería programarse para el futuro",
            capturedTime.captured > System.currentTimeMillis()
        )
    }

    @Test
    fun `schedule - intent apunta a NotificationReceiver`() {

        val intentSlot = slot<Intent>()

        mockkConstructor(Intent::class)


        every {
            PendingIntent.getBroadcast(any(), any(), capture(intentSlot), any())
        } returns mockPendingIntent

        NotificationScheduler.schedule(mockContext, hour = 8, minute = 0)


        assertNotNull(intentSlot.captured)

        unmockkAll()
    }

    // ── scheduleTest() ─────────────────────────────────────────────────────────

    @Test
    fun `scheduleTest - llama a set no a setRepeating`() {
        NotificationScheduler.scheduleTest(mockContext)

        verify(exactly = 1)  { mockAlarmManager.set(any(), any(), any()) }
        verify(exactly = 0)  { mockAlarmManager.setRepeating(any(), any(), any(), any()) }
    }

    @Test
    fun `scheduleTest - dispara en aproximadamente 3 segundos`() {
        val capturedTime = slot<Long>()

        every {
            mockAlarmManager.set(any(), capture(capturedTime), any())
        } just Runs

        val before = System.currentTimeMillis()
        NotificationScheduler.scheduleTest(mockContext)
        val after = System.currentTimeMillis()

        assertTrue("Trigger >= ahora + 2s", capturedTime.captured >= before + 2000L)
        assertTrue("Trigger <= ahora + 4s", capturedTime.captured <= after  + 4000L)
    }

    @Test
    fun `scheduleTest - usa REQUEST_CODE 2002`() {
        NotificationScheduler.scheduleTest(mockContext)

        verify {
            PendingIntent.getBroadcast(
                mockContext,
                2002,
                any(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }
    }

    // ── cancel() ───────────────────────────────────────────────────────────────

    @Test
    fun `cancel - llama a alarmManager cancel`() {
        NotificationScheduler.cancel(mockContext)

        verify(exactly = 1) { mockAlarmManager.cancel(mockPendingIntent) }
    }

    @Test
    fun `cancel - usa REQUEST_CODE 2001`() {
        NotificationScheduler.cancel(mockContext)

        verify {
            PendingIntent.getBroadcast(
                mockContext,
                2001,
                any(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }
    }

    @Test
    fun `cancel - no llama a setRepeating ni a set`() {
        NotificationScheduler.cancel(mockContext)

        verify(exactly = 0) { mockAlarmManager.set(any(), any(), any()) }
        verify(exactly = 0) { mockAlarmManager.setRepeating(any(), any(), any(), any()) }
    }
}