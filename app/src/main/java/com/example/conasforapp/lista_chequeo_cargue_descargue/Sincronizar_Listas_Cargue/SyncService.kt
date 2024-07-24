package com.example.conasforapp.lista_chequeo_cargue_descargue.Sincronizar_Listas_Cargue

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SyncService : Service(){
    private lateinit var syncManager: ListasCargueSyncManager2
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {
        super.onCreate()
        syncManager = ListasCargueSyncManager2(applicationContext)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        syncManager.syncListWithFirestore()
        return START_STICKY
    }
}