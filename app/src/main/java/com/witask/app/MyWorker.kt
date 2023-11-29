package com.witask.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.R
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(val context: Context, val workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val channel = NotificationChannel("pushes","name", NotificationManager.IMPORTANCE_MIN)
        val n = NotificationCompat.Builder(applicationContext,"pushes")
            .setContentText(workerParams.inputData.getString("message")).setContentTitle(workerParams.inputData.getString("title")).setSmallIcon(
                com.witask.app.R.mipmap.icon_round)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSilent(false)
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        manager.notify(workerParams.inputData.getInt("id",0),n.build())
        return  Result.success()
    }
}