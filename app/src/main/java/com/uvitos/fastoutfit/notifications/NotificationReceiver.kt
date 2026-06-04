package com.uvitos.fastoutfit.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.uvitos.fastoutfit.MainActivity
import com.uvitos.fastoutfit.R

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val messages = listOf(
            "¿Ya pensaste en tu outfit de hoy? ✨ ¡FastOutfit te ayuda!",
            "Un buen outfit empieza con una buena mañana 👕 ¡Ábrela!",
            "Hoy es un gran día para lucir increíble 🌟",
            "Tu armario te está esperando 👗 ¡Crea tu look del día!",
            "La moda es arte y tú eres el lienzo 🎨 ¡Inspírate hoy!",
        )
        val message = messages.random()

        createNotificationChannel(context)

        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("FastOutfit 👔")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Outfit del día",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Recordatorio diario para crear tu outfit"
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "outfit_daily"
        const val NOTIFICATION_ID = 1001
    }
}