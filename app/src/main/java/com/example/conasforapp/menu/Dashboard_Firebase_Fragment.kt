package com.example.conasforapp.menu

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.conasforapp.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Dashboard_Firebase_Fragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var percentageMeter: PieChart
    private lateinit var usageTextView: TextView
    private lateinit var chart: PieChart
    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    var maxStorage: Float = 1_000_000_000f // Por ejemplo, 1GB (1,000,000,000 bytes)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_dashboard__firebase_, container, false)

        chart = rootView.findViewById(R.id.chartFirebase)
        percentageMeter = rootView.findViewById(R.id.percentageMeter)
        usageTextView = rootView.findViewById(R.id.usageTextView)

        updateStorageUsage()

        return rootView
    }

    private fun setupChartFirebase(photoCamionSize: Long, firmasSize: Long, fotosPerfilSize: Long) {
        val totalSize = photoCamionSize + firmasSize + fotosPerfilSize

        // Convertir los tamaños a MB para las entradas del gráfico
        val photoCamionSizeMB = photoCamionSize / (1024 * 1024).toFloat()
        val firmasSizeMB = firmasSize / (1024 * 1024).toFloat()
        val fotosPerfilSizeMB = fotosPerfilSize / (1024 * 1024).toFloat()

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(photoCamionSizeMB, "Foto camión"))
        entries.add(PieEntry(firmasSizeMB, "Firmas"))
        entries.add(PieEntry(fotosPerfilSizeMB, "Fotos perfil"))

        val pieDataSet: PieDataSet = PieDataSet(entries, "Subjects")
        pieDataSet.valueTextSize = 24f

        val colors = mutableListOf<Int>()
        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }
        pieDataSet.colors = colors

        val pieData: PieData = PieData(pieDataSet)
        chart.data = pieData

        chart.description.isEnabled = false
        chart.animateY(1000)
        chart.invalidate()

        // Configura la leyenda (Legend)
        val legend = chart.legend
        legend.textSize = 12f
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.setDrawInside(false)
    }

    private fun calculateTotalSize(reference: StorageReference, onProgress: (Long) -> Unit, onComplete: (Long) -> Unit) {
        var totalSize = 0L

        fun listAllFiles(ref: StorageReference, onComplete: () -> Unit) {
            ref.listAll().addOnSuccessListener { result ->
                val pendingTasks = result.items.size + result.prefixes.size
                if (pendingTasks == 0) {
                    onComplete()
                    return@addOnSuccessListener
                }

                var completedTasks = 0

                val checkCompletion: () -> Unit = {
                    completedTasks++
                    onProgress(totalSize)
                    if (completedTasks == pendingTasks) {
                        onComplete()
                    }
                }

                for (item in result.items) {
                    item.metadata.addOnSuccessListener { metadata ->
                        totalSize += metadata.sizeBytes
                        onProgress(totalSize)
                        checkCompletion()
                    }.addOnFailureListener {
                        checkCompletion()
                    }
                }

                for (prefix in result.prefixes) {
                    listAllFiles(prefix, checkCompletion)
                }
            }.addOnFailureListener {
                onComplete()
            }
        }

        listAllFiles(reference) {
            onComplete(totalSize)
        }
    }

    private fun calculateFolderSizes(onProgress: (Long, Long, Long) -> Unit, onComplete: (Long, Long, Long) -> Unit) {
        val photoCamionRef = storage.reference.child("Fotos Camion Cargue Descargue/")
        val firmasRef = storage.reference.child("Firmas_Cargue_Descargue/")
        val fotosPerfilRef = storage.reference.child("Fotos Usuarios Registrados/")

        var photoCamionSize = 0L
        var firmasSize = 0L
        var fotosPerfilSize = 0L

        val totalFolders = 3
        var completedFolders = 0

        val checkCompletion: () -> Unit = {
            completedFolders++
            onProgress(photoCamionSize, firmasSize, fotosPerfilSize)
            if (completedFolders == totalFolders) {
                onComplete(photoCamionSize, firmasSize, fotosPerfilSize)
            }
        }

        calculateTotalSize(photoCamionRef, { currentSize ->
            photoCamionSize = currentSize
            onProgress(photoCamionSize, firmasSize, fotosPerfilSize)
        }) {
            checkCompletion()
        }

        calculateTotalSize(firmasRef, { currentSize ->
            firmasSize = currentSize
            onProgress(photoCamionSize, firmasSize, fotosPerfilSize)
        }) {
            checkCompletion()
        }

        calculateTotalSize(fotosPerfilRef, { currentSize ->
            fotosPerfilSize = currentSize
            onProgress(photoCamionSize, firmasSize, fotosPerfilSize)
        }) {
            checkCompletion()
        }
    }

    fun updateStorageUsage() {
        calculateFolderSizes({ photoCamionSize, firmasSize, fotosPerfilSize ->
            // Actualiza el gráfico con los tamaños parciales
            setupChartFirebase(photoCamionSize, firmasSize, fotosPerfilSize)

            val totalSize = photoCamionSize + firmasSize + fotosPerfilSize
            updatePercentageMeterProgress(totalSize)
        }) { photoCamionSize, firmasSize, fotosPerfilSize ->
            // Actualiza el gráfico con los tamaños completos
            setupChartFirebase(photoCamionSize, firmasSize, fotosPerfilSize)

            val totalSize = photoCamionSize + firmasSize + fotosPerfilSize
            updatePercentageMeterProgress(totalSize)
        }
    }

    private fun updatePercentageMeterProgress(currentValue: Long) {
        setupPercentageMeter(currentValue, maxStorage)
        val currentValueMB = currentValue / (1024 * 1024).toFloat() // Convertir bytes a MB
        val maxValueGB = maxStorage / (1024 * 1024 * 1024).toFloat() // Convertir maxStorage a GB
        usageTextView.text = String.format("%.2f MB", currentValueMB)
    }

    private fun setupPercentageMeter(currentValue: Long, maxValue: Float) {
        val currentValueMB = currentValue / (1024 * 1024).toFloat() // Convertir bytes a MB
        val maxValueMB = maxValue / (1024 * 1024).toFloat() // Convertir maxStorage a MB
        val completedPercentage = (currentValueMB / maxValueMB) * 100 // Calcular el porcentaje completado
        val remainingPercentage = 100 - completedPercentage

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(completedPercentage, "Completed"))
        entries.add(PieEntry(remainingPercentage, "Remaining"))

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.colors = listOf(
            ColorTemplate.rgb("#00FF00"),
            ColorTemplate.rgb("#D3D3D3")
        )
        pieDataSet.valueTextSize = 0f

        val pieData = PieData(pieDataSet)
        percentageMeter.data = pieData

        percentageMeter.legend.isEnabled = false
        percentageMeter.setDrawEntryLabels(false)
        percentageMeter.holeRadius = 80f
        percentageMeter.transparentCircleRadius = 0f
        percentageMeter.setDrawCenterText(true)
        percentageMeter.centerText = String.format("%.2f MB / %.2f GB", currentValueMB, maxValue / (1024 * 1024 * 1024)) // Texto central en MB y GB
        percentageMeter.description.isEnabled = false
        percentageMeter.isRotationEnabled = false
        percentageMeter.setTouchEnabled(false)
        percentageMeter.animateXY(1000, 1000)
        percentageMeter.invalidate()
    }
}