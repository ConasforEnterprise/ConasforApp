package com.example.conasforapp.lista_chequeo_cargue_descargue.Sincronizar_Listas_Cargue

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.widget.Toast
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper
import com.example.conasforapp.modelos.ListasCargueModel
import com.example.conasforapp.modelos.UsuariosModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Calendar
import java.util.Locale

class  ListasCargueSyncManager2(private val context: Context){

    private val firestore = FirebaseFirestore.getInstance()
    private val listCollection = firestore.collection("Listas chequeo cargue descargue")
    private val auth = FirebaseAuth.getInstance()
    private val currentUser: FirebaseUser? = auth.getCurrentUser()
    private var uid: String? = null

    var pathFirmasCargueDescargue = "Firmas_Cargue_Descargue/"
    private var nombreUser: String? = null
    private var cargo: String? = null
    val inflater = LayoutInflater.from(context)

    var loadingDialog: LoadingDialog;
    init {
        loadingDialog = LoadingDialog(context as Activity?)
    }

    private var listasParaSincronizar = 0
    private var listasSincronizadas = 0

    private val dbHelper = ListasCargueDataBaseHelper(context)

    fun syncListWithFirestore() {
        if (isInternetAvailable()) {
            val listasLocales = dbHelper.getCompleteList()
            listasParaSincronizar = listasLocales.size
            listasSincronizadas = 0

            if (listasParaSincronizar == 0) {
                return
            }

            loadingDialog.startLoadingDialog()

            currentUser?.let { user ->
                uid = user.uid
                firestore.collection("Usuarios").document(uid!!).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null && document.exists()) {
                                val usuario = document.toObject(UsuariosModel::class.java)
                                usuario?.let {
                                    nombreUser = it.nombre
                                    cargo = it.cargo
                                    val fotoPerfil = it.fotoUsuario

                                    // Procesar las listas una por una
                                    procesarListasSecuencialmente(listasLocales, fotoPerfil)
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun procesarListasSecuencialmente(listas: List<ListasCargueModel>, fotoPerfil: String?) {
        if (listas.isEmpty()) {
            loadingDialog.dimissDialog()
            Toast.makeText(context, "Todas las listas han sido sincronizadas", Toast.LENGTH_SHORT).show()
            return
        }

        val lista = listas.first()
        procesarLista(lista, fotoPerfil) { success ->
            if (success) {
                listasSincronizadas++
                dbHelper.deleteListById(lista.id_lista_local)
            }
            // Procesar la siguiente lista
            procesarListasSecuencialmente(listas.drop(1), fotoPerfil)
        }
    }

    private fun procesarLista(lista: ListasCargueModel, fotoPerfil: String?, callback: (Boolean) -> Unit) {
        val Item1 = hashMapOf(
            "horaEntrada" to lista.Item_1_Informacion_lugar_cargue.horaEntrada,
            "fecha" to lista.Item_1_Informacion_lugar_cargue.fecha,
            "tipoCargue" to lista.Item_1_Informacion_lugar_cargue.tipoCargue,
            "nombreZona" to lista.Item_1_Informacion_lugar_cargue.nombreZona,
            "nombreNucleo" to lista.Item_1_Informacion_lugar_cargue.nombreNucleo,
            "nombreFinca" to lista.Item_1_Informacion_lugar_cargue.nombreFinca
        )

        val Item2 = hashMapOf(
            "nombreConductor" to lista.Item_2_Informacion_del_conductor.nombreConductor,
            "cedula" to lista.Item_2_Informacion_del_conductor.cedula,
            "lugarExpedicion" to lista.Item_2_Informacion_del_conductor.lugarExpedicion,
            "licConduccionRes" to lista.Item_2_Informacion_del_conductor.licConduccionRes,
            "polizaRCERes" to lista.Item_2_Informacion_del_conductor.polizaRCERes,
            "epsRes" to lista.Item_2_Informacion_del_conductor.epsRes,
            "arlRes" to lista.Item_2_Informacion_del_conductor.arlRes,
            "afpRes" to lista.Item_2_Informacion_del_conductor.afpRes,
            "cualEPS" to lista.Item_2_Informacion_del_conductor.cualEPS,
            "cualARL" to lista.Item_2_Informacion_del_conductor.cualARL,
            "cualAFP" to lista.Item_2_Informacion_del_conductor.cualAFP
        )

        val Item3 = hashMapOf(
            "placa" to lista.Item_3_Informacion_vehiculo.placa,
            "vehiculo" to lista.Item_3_Informacion_vehiculo.vehiculo,
            "tarjetaPropiedad" to lista.Item_3_Informacion_vehiculo.tarjetaPropiedad,
            "soatVigente" to lista.Item_3_Informacion_vehiculo.soatVigente,
            "revisionTecnicomecanica" to lista.Item_3_Informacion_vehiculo.revisionTecnicomecanica,
            "lucesAltas" to lista.Item_3_Informacion_vehiculo.lucesAltas,
            "lucesBajas" to lista.Item_3_Informacion_vehiculo.lucesBajas,
            "direccionales" to lista.Item_3_Informacion_vehiculo.direccionales,
            "sonidoReversa" to lista.Item_3_Informacion_vehiculo.sonidoReversa,
            "reversa" to lista.Item_3_Informacion_vehiculo.reversa,
            "stop" to lista.Item_3_Informacion_vehiculo.stop,
            "retrovisores" to lista.Item_3_Informacion_vehiculo.retrovisores,
            "plumillas" to lista.Item_3_Informacion_vehiculo.plumillas,
            "estadoPanoramicos" to lista.Item_3_Informacion_vehiculo.estadoPanoramicos,
            "espejos" to lista.Item_3_Informacion_vehiculo.espejos,
            "bocina" to lista.Item_3_Informacion_vehiculo.bocina,
            "cinturon" to lista.Item_3_Informacion_vehiculo.cinturon,
            "freno" to lista.Item_3_Informacion_vehiculo.freno,
            "llantas" to lista.Item_3_Informacion_vehiculo.llantas,
            "botiquin" to lista.Item_3_Informacion_vehiculo.botiquin,
            "extintorABC" to lista.Item_3_Informacion_vehiculo.extintorABC,
            "botas" to lista.Item_3_Informacion_vehiculo.botas,
            "chaleco" to lista.Item_3_Informacion_vehiculo.chaleco,
            "casco" to lista.Item_3_Informacion_vehiculo.casco,
            "carroceria" to lista.Item_3_Informacion_vehiculo.carroceria,
            "dosEslingasBanco" to lista.Item_3_Informacion_vehiculo.dosEslingasBanco,
            "dosConosReflectivos" to lista.Item_3_Informacion_vehiculo.dosConosReflectivos,
            "parales" to lista.Item_3_Informacion_vehiculo.parales,
            "observacionesCamion" to lista.Item_3_Informacion_vehiculo.observacionesCamion
        )

        val Item4 = hashMapOf(
            //"fotoCamion" to lista.fotoStatic,
            "horaSalida" to lista.Item_4_Estado_cargue.horaSalida,
            "maderaNoSuperaMampara" to lista.Item_4_Estado_cargue.maderaNoSuperaMampara,
            "maderaNoSuperaParales" to lista.Item_4_Estado_cargue.maderaNoSuperaParales,
            "noMaderaAtraviesaMampara" to lista.Item_4_Estado_cargue.noMaderaAtraviesaMampara,
            "paralesMismaAltura" to lista.Item_4_Estado_cargue.paralesMismaAltura,
            "ningunaUndSobrepasaParales" to lista.Item_4_Estado_cargue.ningunaUndSobrepasaParales,
            "cadaBancoAseguradoEslingas" to lista.Item_4_Estado_cargue.cadaBancoAseguradoEslingas,
            "carroceriaParalesBuenEstado" to lista.Item_4_Estado_cargue.carroceriaParalesBuenEstado,
            "conductorSalioLugarCinturon" to lista.Item_4_Estado_cargue.conductorSalioLugarCinturon,
            "paralesAbatiblesAseguradosEstrobos" to lista.Item_4_Estado_cargue.paralesAbatiblesAseguradosEstrobos
        )

        val listData = hashMapOf(
            "lista_numero" to lista.lista_numero,
            "id_usuario" to uid,
            "nombre" to nombreUser,
            "cargo" to cargo,
            "fotoUsuario" to fotoPerfil,
            "Item_1_Informacion_lugar_cargue" to Item1,
            "Item_2_Informacion_del_conductor" to Item2,
            "Item_3_Informacion_vehiculo" to Item3,
            "Item_4_Estado_cargue" to Item4,
            "nombre_Supervisor" to lista.nombre_Supervisor,
            "nombre_Despachador" to lista.nombre_Despachador,
            "nombre_Conductor" to lista.nombre_Conductor,
            "nombre_Operador" to lista.nombre_Operador,
            "timestamp" to FieldValue.serverTimestamp()
        )


        listCollection.add(listData)
            .addOnSuccessListener { documentReference ->
                val idListaCreada = documentReference.id

                val updateTasks = mutableListOf<Task<*>>()

                // Agregar tareas de carga de imágenes y firmas
                agregarTareasDeCargas(updateTasks, lista, idListaCreada)

                // Esperar a que todas las tareas de carga se completen
                Tasks.whenAllComplete(updateTasks)
                    .addOnCompleteListener {
                        callback(true)
                    }
                    .addOnFailureListener {
                        callback(false)
                    }
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    private fun agregarTareasDeCargas(updateTasks: MutableList<Task<*>>, lista: ListasCargueModel, idListaCreada: String) {
        lista.Item_4_Estado_cargue.fotoCamion?.let { fotoCamion ->
            val uploadTask = uploadImageToStorage(fotoCamion,idListaCreada)
            uploadTask?.let { task ->
                updateTasks.add(task.continueWithTask { task ->
                    val imageUrl = task.result
                    listCollection.document(idListaCreada)
                        .update("Item_4_Estado_cargue.fotoCamion", imageUrl)
                })
            }
        }

        // Tarea para la firma del supervisor
        lista.firma_Supervisor?.let { firmaSupervisor ->
            val uploadTask = enviarFirma1(firmaSupervisor,idListaCreada)
            uploadTask?.let { task ->
                updateTasks.add(task.continueWithTask { task ->
                    val firma1Url = task.result
                    listCollection.document(idListaCreada)
                        .update("firma_Supervisor", firma1Url)
                })
            }
        }

        // Tarea para la firma del despachador
        lista.firma_Despachador?.let { firmaDespachador ->
            val uploadTask = enviarFirma2(firmaDespachador,idListaCreada)
            uploadTask?.let { task ->
                updateTasks.add(task.continueWithTask { task ->
                    val firma2Url = task.result
                    listCollection.document(idListaCreada)
                        .update("firma_Despachador", firma2Url)
                })
            }
        }

        // Tarea para la firma del conductor
        lista.firma_Conductor?.let { firmaConductor ->
            val uploadTask = enviarFirma3(firmaConductor,idListaCreada)
            uploadTask?.let { task ->
                updateTasks.add(task.continueWithTask { task ->
                    val firma3Url = task.result
                    listCollection.document(idListaCreada)
                        .update("firma_Conductor", firma3Url)
                })
            }
        }

        // Tarea para la firma del operador
        lista.firma_Operador?.let { firmaOperador ->
            val uploadTask = enviarFirma4(firmaOperador,idListaCreada)
            uploadTask?.let { task ->
                updateTasks.add(task.continueWithTask { task ->
                    val firma4Url = task.result
                    listCollection.document(idListaCreada)
                        .update("firma_Operador", firma4Url)
                })
            }
        }
    }

    fun uploadImageToStorage(base64Image: String, idLista: String): Task<Uri> {
        val cleanedBase64 = base64Image.replace("\\s".toRegex(), "")
        val calHoy = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calHoy.time)

        val pathFotoCamion = "foto_camión_${nombreUser}_${cargo}_${formattedDate}_ID_Usuario_${uid}_ID_LISTA_${idLista}.jpg"

        val decodedBytes: ByteArray = Base64.getDecoder().decode(cleanedBase64)

        val storageRef = FirebaseStorage.getInstance().reference
        val imagenRef = storageRef.child("Fotos Camion Cargue Descargue/$formattedDate/$pathFotoCamion")

        return imagenRef.putBytes(decodedBytes).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            imagenRef.downloadUrl
        }
    }

    private fun enviarFirma(signatureBase64: String?, path: String, fileName: String): Task<Uri> {
        val data: ByteArray = android.util.Base64.decode(signatureBase64, android.util.Base64.DEFAULT) // Convertir el string Base64 en un array de bytes
        val storageRef = FirebaseStorage.getInstance().reference // Referencia al Firebase Storage
        val firmaRef = storageRef.child("$path/$fileName") // Referencia al archivo en el Firebase Storage

        val uploadTask = firmaRef.putBytes(data)

        return uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            firmaRef.downloadUrl
        }
    }

    fun enviarFirma1(signatureBase64: String, idLista: String): Task<Uri> {
        val calHoy = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calHoy.time)
        val fileName = "firma_supervisor_${formattedDate}_ID_USUARIO_${uid}_ID_LISTA_${idLista}.jpg"
        return enviarFirma(signatureBase64, pathFirmasCargueDescargue + formattedDate, fileName)
    }

    fun enviarFirma2(signatureBase64: String, idLista: String): Task<Uri> {
        val calHoy = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calHoy.time)
        val fileName = "firma_despachador_${formattedDate}_ID_USUARIO_${uid}_ID_LISTA_${idLista}.jpg"
        return enviarFirma(signatureBase64, pathFirmasCargueDescargue + formattedDate, fileName)
    }

    fun enviarFirma3(signatureBase64: String, idLista: String): Task<Uri> {
        val calHoy = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calHoy.time)
        val fileName = "firma_conductor_${formattedDate}_ID_USUARIO_${uid}_ID_LISTA_${idLista}.jpg"
        return enviarFirma(signatureBase64, pathFirmasCargueDescargue + formattedDate, fileName)
    }

    fun enviarFirma4(signatureBase64: String, idLista: String): Task<Uri> {
        val calHoy = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calHoy.time)
        val fileName = "firma_operador_${formattedDate}_ID_USUARIO_${uid}_ID_LISTA_${idLista}.jpg"
        return enviarFirma(signatureBase64, pathFirmasCargueDescargue + formattedDate, fileName)
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            networkInfo.isConnected
        }
    }
}