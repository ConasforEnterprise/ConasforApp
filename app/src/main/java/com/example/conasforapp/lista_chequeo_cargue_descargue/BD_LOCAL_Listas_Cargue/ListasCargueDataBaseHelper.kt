package com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.conasforapp.modelos.ListasCargueModel

class ListasCargueDataBaseHelper (context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME,null,DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "LISTAS_CARGUE_MADERA.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "listas"
        private const val COLUMN_ID = "id"

        private const val COLUMN_HORA = "hora_entrada"
        private const val COLUMN_FECHA = "fecha"
        private const val COLUMN_TIPO_CARGUE = "tipo_cargue"
        private const val COLUMN_ZONA = "nombre_zona"
        private const val COLUMN_NUCLEO = "nombre_nucleo"
        private const val COLUMN_FINCA = "nombre_finca"

        private const val COLUMN_CONDUCTOR = "nombre_conductor"
        private const val COLUMN_CEDULA_CONDUCTOR = "cedula"
        private const val COLUMN_LUGAR_EXPEDICION_CC = "lugar_expedicion"
        private const val COLUMN_LIC_CONDUCCION = "lic_conduccion_res"
        private const val COLUMN_POLIZA_RCE = "poliza_rce_res"
        private const val EPS_RES = "eps_res"
        private const val ARL_RES = "arl_res"
        private const val AFP_RES = "afp_res"
        private const val EPS_CUAL = "cual_eps"
        private const val ARL_CUAL = "cual_arl"
        private const val AFP_CUAL = "cual_afp"

        private const val PLACA = "placa"
        private const val VEHICULO = "vehiculo"
        private const val TARJETA_PROPIEDAD = "tarjeta_propiedad"
        private const val SOAT_VIGENTE = "soat_vigente"
        private const val REVISION_TECNOMECANICA = "revision_tecnicomecanica"
        private const val LUCES_ALTAS = "luces_altas"
        private const val LUCES_BAJAS = "luces_bajas"
        private const val DIRECCIONALES = "direccionales"
        private const val SONIDO_REVERSA = "sonido_reversa"
        private const val REVERSA = "reversa"
        private const val STOP = "stop"
        private const val RETROVISORES = "retrovisores"
        private const val PLUMILLAS = "plumillas"
        private const val ESTADO_PANORAMICOS = "estado_panoramicos"
        private const val ESPEJOS = "espejos"
        private const val BOCINA = "bocina"
        private const val CINTURON = "cinturon"
        private const val FRENO = "freno"
        private const val LLANTAS = "llantas"
        private const val BOTIQUIN = "botiquin"
        private const val EXTINTOR_ABC = "extintor_abc"
        private const val BOTAS = "botas"
        private const val CHALECO = "chaleco"
        private const val CASCO = "casco"
        private const val CARROCERIA = "carroceria"
        private const val DOS_ESLINGAS_POR_BANCO = "dos_eslingas_banco"
        private const val DOS_CONOS_REFLECTIVOS = "dos_conos_reflectivos"
        private const val PARALES = "parales"
        private const val OBSERVACIONES_CAMION = "observaciones_camion"

        private const val HORA_SALIDA = "hora_salida"
        private const val FOTO_CAMION = "foto_camion"
        private const val MADERA_NO_SUPERA_MAMPARA = "madera_no_supera_mampara"
        private const val MADERA_NO_SUPERA_PARALES = "madera_no_supera_parales"
        private const val NO_HAY_MADERA_ATRAVIESE_MAMPARA = "no_madera_atraviesa_mampara"
        private const val PARALES_MISMA_ALTURA = "parales_misma_altura"
        private const val NINGUNA_UND_SOBREPASA_PARALES = "ninguna_und_sobrepasa_parales"
        private const val CADA_BANCO_ASEGURADO_ESLINGAS = "cada_banco_asegurado_eslingas"
        private const val CARROCERIA_Y_PARALES_BUEN_ESTADO = "carroceria_parales_buen_estado"
        private const val CONDUCTOR_SALIO_LUGAR_CON_CINTURON = "conductor_salio_lugar_cinturon"
        private const val PARALES_ABATIBLES_ASEGURADOS_ESTROBOS = "parales_abatibles_asegurados_estrobos"

        private const val NOMBRE_SUPERVISOR_FIRMA = "nombre_supervisor_firma"
        private const val FIRMA_SUPERVISOR = "firma_Supervisor"
        private const val NOMBRE_DESPACHADOR_FIRMA = "nombre_despachador_firma"
        private const val FIRMA_DESPACHADOR = "firma_despachador"
        private const val NOMBRE_CONDUCTOR_FIRMA = "nombre_conductor_firma"
        private const val FIRMA_CONDUCTOR = "firma_Conductor"
        private const val NOMBRE_OPERADOR_FIRMA = "nombre_operador_firma"
        private const val FIRMA_OPERADOR = "firma_operador"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME(" +
                "$COLUMN_ID INTEGER PRIMARY KEY," +

                "$COLUMN_HORA TEXT," +
                "$COLUMN_FECHA TEXT," +
                "$COLUMN_TIPO_CARGUE TEXT," +
                "$COLUMN_ZONA TEXT,"+
                "$COLUMN_NUCLEO TEXT,"+
                "$COLUMN_FINCA TEXT,"+

                "$COLUMN_CONDUCTOR TEXT,"+
                "$COLUMN_CEDULA_CONDUCTOR TEXT,"+
                "$COLUMN_LUGAR_EXPEDICION_CC TEXT,"+
                "$COLUMN_LIC_CONDUCCION TEXT,"+
                "$COLUMN_POLIZA_RCE TEXT,"+
                "$EPS_RES TEXT,"+
                "$ARL_RES TEXT,"+
                "$AFP_RES TEXT,"+
                "$EPS_CUAL TEXT,"+
                "$ARL_CUAL TEXT,"+
                "$AFP_CUAL TEXT,"+

                "$PLACA TEXT,"+
                "$VEHICULO TEXT,"+
                "$TARJETA_PROPIEDAD TEXT,"+
                "$SOAT_VIGENTE TEXT,"+
                "$REVISION_TECNOMECANICA TEXT,"+
                "$LUCES_ALTAS TEXT,"+
                "$LUCES_BAJAS TEXT,"+
                "$DIRECCIONALES TEXT,"+
                "$SONIDO_REVERSA TEXT,"+
                "$REVERSA  TEXT,"+
                "$STOP  TEXT,"+
                "$RETROVISORES TEXT,"+
                "$PLUMILLAS TEXT,"+
                "$ESTADO_PANORAMICOS TEXT,"+
                "$ESPEJOS TEXT,"+
                "$BOCINA TEXT,"+
                "$CINTURON TEXT,"+
                "$FRENO TEXT,"+
                "$LLANTAS TEXT,"+
                "$BOTIQUIN TEXT,"+
                "$EXTINTOR_ABC TEXT,"+
                "$BOTAS TEXT,"+
                "$CHALECO TEXT,"+
                "$CASCO TEXT,"+
                "$CARROCERIA TEXT,"+
                "$DOS_ESLINGAS_POR_BANCO TEXT,"+
                "$DOS_CONOS_REFLECTIVOS TEXT,"+
                "$PARALES TEXT,"+
                "$OBSERVACIONES_CAMION TEXT,"+

                "$HORA_SALIDA TEXT,"+
                "$FOTO_CAMION TEXT,"+
                "$MADERA_NO_SUPERA_MAMPARA TEXT,"+
                "$MADERA_NO_SUPERA_PARALES TEXT,"+
                "$NO_HAY_MADERA_ATRAVIESE_MAMPARA TEXT,"+
                "$PARALES_MISMA_ALTURA TEXT,"+
                "$NINGUNA_UND_SOBREPASA_PARALES TEXT,"+
                "$CADA_BANCO_ASEGURADO_ESLINGAS TEXT,"+
                "$CARROCERIA_Y_PARALES_BUEN_ESTADO TEXT,"+
                "$CONDUCTOR_SALIO_LUGAR_CON_CINTURON TEXT,"+
                "$PARALES_ABATIBLES_ASEGURADOS_ESTROBOS TEXT,"+

                "$NOMBRE_SUPERVISOR_FIRMA TEXT,"+
                "$FIRMA_SUPERVISOR TEXT,"+
                "$NOMBRE_DESPACHADOR_FIRMA TEXT,"+
                "$FIRMA_DESPACHADOR TEXT,"+
                "$NOMBRE_CONDUCTOR_FIRMA TEXT,"+
                "$FIRMA_CONDUCTOR TEXT,"+
                "$NOMBRE_OPERADOR_FIRMA TEXT,"+
                "$FIRMA_OPERADOR TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }
    fun insertList(
        item1: ListasCargueModel.InfoLugarCargue,
        item2: ListasCargueModel.InfoDelConductor,
        item3: ListasCargueModel.InfoDelVehiculo,
        item4: ListasCargueModel.EstadoDelCargue,
        firmas: ListasCargueModel
    ){
        val db = writableDatabase
        val values = ContentValues().apply {

            put(COLUMN_HORA, item1.horaEntrada)
            put(COLUMN_FECHA, item1.fecha)
            put(COLUMN_TIPO_CARGUE, item1.tipoCargue)
            put(COLUMN_ZONA, item1.nombreZona)
            put(COLUMN_NUCLEO,item1.nombreNucleo)
            put(COLUMN_FINCA, item1.nombreFinca)

            put(COLUMN_CONDUCTOR, item2.nombreConductor)
            put(COLUMN_CEDULA_CONDUCTOR, item2.cedula)
            put(COLUMN_LUGAR_EXPEDICION_CC, item2.lugarExpedicion)
            put(COLUMN_LIC_CONDUCCION, item2.licConduccionRes)
            put(COLUMN_POLIZA_RCE, item2.polizaRCERes)
            put(EPS_RES, item2.epsRes)
            put(ARL_RES, item2.arlRes)
            put(AFP_RES, item2.afpRes)
            put(EPS_CUAL, item2.cualEPS)
            put(ARL_CUAL, item2.cualARL)
            put(AFP_CUAL, item2.cualAFP)

            put(PLACA, item3.placa)
            put(VEHICULO, item3.vehiculo)
            put(TARJETA_PROPIEDAD, item3.tarjetaPropiedad)
            put(SOAT_VIGENTE, item3.soatVigente)
            put(REVISION_TECNOMECANICA, item3.revisionTecnicomecanica)
            put(LUCES_ALTAS, item3.lucesAltas)
            put(LUCES_BAJAS, item3.lucesBajas)
            put(DIRECCIONALES, item3.direccionales)
            put(SONIDO_REVERSA, item3.sonidoReversa)
            put(REVERSA, item3.reversa)
            put(STOP, item3.stop)
            put(RETROVISORES,item3.retrovisores)
            put(PLUMILLAS, item3.plumillas)
            put(ESTADO_PANORAMICOS,item3.estadoPanoramicos)
            put(ESPEJOS,item3.espejos)
            put(BOCINA,item3.bocina)
            put(CINTURON,item3.cinturon)
            put(FRENO,item3.freno)
            put(LLANTAS ,item3.llantas)
            put(BOTIQUIN,item3.botiquin)
            put(EXTINTOR_ABC,item3.extintorABC)
            put(BOTAS,item3.botas)
            put(CHALECO,item3.chaleco)
            put(CASCO,item3.casco)
            put(CARROCERIA,item3.carroceria)
            put(DOS_ESLINGAS_POR_BANCO,item3.dosEslingasBanco)
            put(DOS_CONOS_REFLECTIVOS,item3.dosConosReflectivos)
            put(PARALES,item3.parales)
            put(OBSERVACIONES_CAMION,item3.observacionesCamion)

            put(HORA_SALIDA,item4.horaSalida)
            put(FOTO_CAMION,item4.fotoCamion)
            put(MADERA_NO_SUPERA_MAMPARA,item4.maderaNoSuperaMampara)
            put(MADERA_NO_SUPERA_PARALES,item4.maderaNoSuperaParales)
            put(NO_HAY_MADERA_ATRAVIESE_MAMPARA,item4.noMaderaAtraviesaMampara)
            put(PARALES_MISMA_ALTURA,item4.paralesMismaAltura)
            put(NINGUNA_UND_SOBREPASA_PARALES,item4.ningunaUndSobrepasaParales)
            put(CADA_BANCO_ASEGURADO_ESLINGAS,item4.cadaBancoAseguradoEslingas)
            put(CARROCERIA_Y_PARALES_BUEN_ESTADO,item4.carroceriaParalesBuenEstado)
            put(CONDUCTOR_SALIO_LUGAR_CON_CINTURON,item4.conductorSalioLugarCinturon)
            put(PARALES_ABATIBLES_ASEGURADOS_ESTROBOS,item4.paralesAbatiblesAseguradosEstrobos)

            put(NOMBRE_SUPERVISOR_FIRMA, firmas.nombre_Supervisor)
            put(FIRMA_SUPERVISOR, firmas.firma_Supervisor)
            put(NOMBRE_DESPACHADOR_FIRMA, firmas.nombre_Despachador)
            put(FIRMA_DESPACHADOR, firmas.firma_Despachador)
            put(NOMBRE_CONDUCTOR_FIRMA, firmas.nombre_Conductor)
            put(FIRMA_CONDUCTOR, firmas.firma_Conductor)
            put(NOMBRE_OPERADOR_FIRMA, firmas.nombre_Operador)
            put(FIRMA_OPERADOR, firmas.firma_Operador)
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getListById(listaId: Int): ListasCargueModel {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(listaId.toString()))

        var listaCargue: ListasCargueModel? = null

        cursor.use { cursor ->
            if (cursor.moveToFirst()) {

                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val horaEntrada = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HORA))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA))
                val tipoCargue = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIPO_CARGUE))
                val zona = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ZONA))
                val nucleo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUCLEO))
                val finca = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FINCA))

                val conductor = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONDUCTOR))
                val cedula = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CEDULA_CONDUCTOR))
                val lugarExpedicion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LUGAR_EXPEDICION_CC))
                val licConduccion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LIC_CONDUCCION))
                val polizaRCE = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POLIZA_RCE))
                val epsRes = cursor.getString(cursor.getColumnIndexOrThrow(EPS_RES))
                val arlRes = cursor.getString(cursor.getColumnIndexOrThrow(ARL_RES))
                val afpRes = cursor.getString(cursor.getColumnIndexOrThrow(AFP_RES))
                val epsCual = cursor.getString(cursor.getColumnIndexOrThrow(EPS_CUAL))
                val arlCual = cursor.getString(cursor.getColumnIndexOrThrow(ARL_CUAL))
                val afpCual = cursor.getString(cursor.getColumnIndexOrThrow(AFP_CUAL))

                val placa = cursor.getString(cursor.getColumnIndexOrThrow(PLACA))
                val vehiculo = cursor.getString(cursor.getColumnIndexOrThrow(VEHICULO))
                val tarjetaPropiedad = cursor.getString(cursor.getColumnIndexOrThrow(TARJETA_PROPIEDAD))
                val soatVigente = cursor.getString(cursor.getColumnIndexOrThrow(SOAT_VIGENTE))
                val revisionTecnomecanica = cursor.getString(cursor.getColumnIndexOrThrow(REVISION_TECNOMECANICA))
                val lucesAltas = cursor.getString(cursor.getColumnIndexOrThrow(LUCES_ALTAS))
                val lucesBajas = cursor.getString(cursor.getColumnIndexOrThrow(LUCES_BAJAS))
                val direccionales = cursor.getString(cursor.getColumnIndexOrThrow(DIRECCIONALES))
                val sonidoReversa = cursor.getString(cursor.getColumnIndexOrThrow(SONIDO_REVERSA))
                val reversa = cursor.getString(cursor.getColumnIndexOrThrow(REVERSA))
                val stop = cursor.getString(cursor.getColumnIndexOrThrow(STOP))
                val retrovisores = cursor.getString(cursor.getColumnIndexOrThrow(RETROVISORES))
                val plumillas = cursor.getString(cursor.getColumnIndexOrThrow(PLUMILLAS))
                val estadoPanoramicos = cursor.getString(cursor.getColumnIndexOrThrow(ESTADO_PANORAMICOS))
                val espejos = cursor.getString(cursor.getColumnIndexOrThrow(ESPEJOS))
                val bocina = cursor.getString(cursor.getColumnIndexOrThrow(BOCINA))
                val cinturon = cursor.getString(cursor.getColumnIndexOrThrow(CINTURON))
                val freno = cursor.getString(cursor.getColumnIndexOrThrow(FRENO))
                val llantas = cursor.getString(cursor.getColumnIndexOrThrow(LLANTAS))
                val botiquin = cursor.getString(cursor.getColumnIndexOrThrow(BOTIQUIN))
                val extintorABC = cursor.getString(cursor.getColumnIndexOrThrow(EXTINTOR_ABC))
                val botas = cursor.getString(cursor.getColumnIndexOrThrow(BOTAS))
                val chaleco = cursor.getString(cursor.getColumnIndexOrThrow(CHALECO))
                val casco = cursor.getString(cursor.getColumnIndexOrThrow(CASCO))
                val carroceria = cursor.getString(cursor.getColumnIndexOrThrow(CARROCERIA))
                val dosEslingasBanco = cursor.getString(cursor.getColumnIndexOrThrow(DOS_ESLINGAS_POR_BANCO))
                val dosConosReflectivos = cursor.getString(cursor.getColumnIndexOrThrow(DOS_CONOS_REFLECTIVOS))
                val parales = cursor.getString(cursor.getColumnIndexOrThrow(PARALES))
                val observacionesCamion = cursor.getString(cursor.getColumnIndexOrThrow(OBSERVACIONES_CAMION))

                val horaSalida = cursor.getString(cursor.getColumnIndexOrThrow(HORA_SALIDA))
                val fotoCamion = cursor.getString(cursor.getColumnIndexOrThrow(FOTO_CAMION))
                val maderaNoSuperaMampara = cursor.getString(cursor.getColumnIndexOrThrow(MADERA_NO_SUPERA_MAMPARA))
                val maderaNoSuperaParales = cursor.getString(cursor.getColumnIndexOrThrow(MADERA_NO_SUPERA_PARALES))
                val noMaderaAtraviesaMampara = cursor.getString(cursor.getColumnIndexOrThrow(NO_HAY_MADERA_ATRAVIESE_MAMPARA))
                val paralesMismaAltura = cursor.getString(cursor.getColumnIndexOrThrow(PARALES_MISMA_ALTURA))
                val ningunaUndSobrepasaParales = cursor.getString(cursor.getColumnIndexOrThrow(NINGUNA_UND_SOBREPASA_PARALES))
                val cadaBancoAseguradoEslingas = cursor.getString(cursor.getColumnIndexOrThrow(CADA_BANCO_ASEGURADO_ESLINGAS))
                val carroceriaParalesBuenEstado = cursor.getString(cursor.getColumnIndexOrThrow(CARROCERIA_Y_PARALES_BUEN_ESTADO))
                val conductorSalioLugarCinturon = cursor.getString(cursor.getColumnIndexOrThrow(CONDUCTOR_SALIO_LUGAR_CON_CINTURON))
                val paralesAbatiblesAseguradosEstrobos = cursor.getString(cursor.getColumnIndexOrThrow(PARALES_ABATIBLES_ASEGURADOS_ESTROBOS))

                val nombreSupervisor = cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_SUPERVISOR_FIRMA))
                val nombreDespachador = cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_DESPACHADOR_FIRMA))
                val nombreConductor = cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_CONDUCTOR_FIRMA))
                val nombreOperador = cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_OPERADOR_FIRMA))
                val firmaSupervisor = cursor.getString(cursor.getColumnIndexOrThrow(FIRMA_SUPERVISOR))
                val firmaDespachador = cursor.getString(cursor.getColumnIndexOrThrow(FIRMA_DESPACHADOR))
                val firmaConductor = cursor.getString(cursor.getColumnIndexOrThrow(FIRMA_CONDUCTOR))
                val firmaOperador = cursor.getString(cursor.getColumnIndexOrThrow(FIRMA_OPERADOR))

                listaCargue = ListasCargueModel().apply {
                    this.id_lista_local = id
                    this.Item_1_Informacion_lugar_cargue.horaEntrada = horaEntrada
                    this.Item_1_Informacion_lugar_cargue.fecha = fecha
                    this.Item_1_Informacion_lugar_cargue.tipoCargue = tipoCargue
                    this.Item_1_Informacion_lugar_cargue.nombreZona = zona
                    this.Item_1_Informacion_lugar_cargue.nombreNucleo = nucleo
                    this.Item_1_Informacion_lugar_cargue.nombreFinca = finca

                    this.Item_2_Informacion_del_conductor.nombreConductor = conductor
                    this.Item_2_Informacion_del_conductor.cedula = cedula
                    this.Item_2_Informacion_del_conductor.lugarExpedicion = lugarExpedicion
                    this.Item_2_Informacion_del_conductor.licConduccionRes = licConduccion
                    this.Item_2_Informacion_del_conductor.polizaRCERes = polizaRCE
                    this.Item_2_Informacion_del_conductor.epsRes = epsRes
                    this.Item_2_Informacion_del_conductor.arlRes = arlRes
                    this.Item_2_Informacion_del_conductor.afpRes = afpRes
                    this.Item_2_Informacion_del_conductor.cualEPS = epsCual
                    this.Item_2_Informacion_del_conductor.cualARL = arlCual
                    this.Item_2_Informacion_del_conductor.cualAFP = afpCual

                    this.Item_3_Informacion_vehiculo.placa = placa
                    this.Item_3_Informacion_vehiculo.vehiculo = vehiculo
                    this.Item_3_Informacion_vehiculo.tarjetaPropiedad = tarjetaPropiedad
                    this.Item_3_Informacion_vehiculo.soatVigente = soatVigente
                    this.Item_3_Informacion_vehiculo.revisionTecnicomecanica = revisionTecnomecanica
                    this.Item_3_Informacion_vehiculo.lucesAltas = lucesAltas
                    this.Item_3_Informacion_vehiculo.lucesBajas = lucesBajas
                    this.Item_3_Informacion_vehiculo.direccionales = direccionales
                    this.Item_3_Informacion_vehiculo.sonidoReversa = sonidoReversa
                    this.Item_3_Informacion_vehiculo.reversa = reversa
                    this.Item_3_Informacion_vehiculo.stop = stop
                    this.Item_3_Informacion_vehiculo.retrovisores = retrovisores
                    this.Item_3_Informacion_vehiculo.plumillas = plumillas
                    this.Item_3_Informacion_vehiculo.estadoPanoramicos = estadoPanoramicos
                    this.Item_3_Informacion_vehiculo.espejos = espejos
                    this.Item_3_Informacion_vehiculo.bocina = bocina
                    this.Item_3_Informacion_vehiculo.cinturon = cinturon
                    this.Item_3_Informacion_vehiculo.freno = freno
                    this.Item_3_Informacion_vehiculo.llantas = llantas
                    this.Item_3_Informacion_vehiculo.botiquin = botiquin
                    this.Item_3_Informacion_vehiculo.extintorABC = extintorABC
                    this.Item_3_Informacion_vehiculo.botas = botas
                    this.Item_3_Informacion_vehiculo.chaleco = chaleco
                    this.Item_3_Informacion_vehiculo.casco = casco
                    this.Item_3_Informacion_vehiculo.carroceria = carroceria
                    this.Item_3_Informacion_vehiculo.dosEslingasBanco = dosEslingasBanco
                    this.Item_3_Informacion_vehiculo.dosConosReflectivos = dosConosReflectivos
                    this.Item_3_Informacion_vehiculo.parales = parales
                    this.Item_3_Informacion_vehiculo.observacionesCamion = observacionesCamion

                    this.Item_4_Estado_cargue.horaSalida = horaSalida
                    this.Item_4_Estado_cargue.fotoCamion = fotoCamion
                    this.Item_4_Estado_cargue.maderaNoSuperaMampara = maderaNoSuperaMampara
                    this.Item_4_Estado_cargue.maderaNoSuperaParales = maderaNoSuperaParales
                    this.Item_4_Estado_cargue.noMaderaAtraviesaMampara = noMaderaAtraviesaMampara
                    this.Item_4_Estado_cargue.paralesMismaAltura = paralesMismaAltura
                    this.Item_4_Estado_cargue.ningunaUndSobrepasaParales = ningunaUndSobrepasaParales
                    this.Item_4_Estado_cargue.cadaBancoAseguradoEslingas = cadaBancoAseguradoEslingas
                    this.Item_4_Estado_cargue.carroceriaParalesBuenEstado = carroceriaParalesBuenEstado
                    this.Item_4_Estado_cargue.conductorSalioLugarCinturon = conductorSalioLugarCinturon
                    this.Item_4_Estado_cargue.paralesAbatiblesAseguradosEstrobos = paralesAbatiblesAseguradosEstrobos

                    this.nombre_Supervisor = nombreSupervisor
                    this.firma_Supervisor = firmaSupervisor
                    this.nombre_Despachador = nombreDespachador
                    this.firma_Despachador = firmaDespachador
                    this.nombre_Conductor = nombreConductor
                    this.firma_Conductor = firmaConductor
                    this.nombre_Operador = nombreOperador
                    this.firma_Operador = firmaOperador
                }
            }
        }
        val finalLista = listaCargue ?: throw IllegalStateException("List not found")
        return finalLista
    }

    fun getCompleteList(): List<ListasCargueModel> {
        val list = mutableListOf<ListasCargueModel>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val listaCargue = ListasCargueModel().apply {
                id_lista_local = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))

                //ITEM 1
                val horaIndex = cursor.getColumnIndex(COLUMN_HORA)
                if (horaIndex != -1 && !cursor.isNull(horaIndex)) {
                    Item_1_Informacion_lugar_cargue.horaEntrada = cursor.getString(horaIndex)
                }
                val fechaIndex = cursor.getColumnIndex(COLUMN_FECHA)
                if (fechaIndex != -1 && !cursor.isNull(fechaIndex)) {
                    Item_1_Informacion_lugar_cargue.fecha = cursor.getString(fechaIndex)
                }
                val tipoCargueIndex = cursor.getColumnIndex(COLUMN_TIPO_CARGUE)
                if (tipoCargueIndex != -1 && !cursor.isNull(tipoCargueIndex)) {
                    Item_1_Informacion_lugar_cargue.tipoCargue = cursor.getString(tipoCargueIndex)
                }
                val zonaIndex = cursor.getColumnIndex(COLUMN_ZONA)
                if (zonaIndex != -1 && !cursor.isNull(zonaIndex)) {
                    Item_1_Informacion_lugar_cargue.nombreZona  = cursor.getString(zonaIndex)
                }

                val nucleoIndex = cursor.getColumnIndex(COLUMN_NUCLEO)
                if (nucleoIndex != -1 && !cursor.isNull(nucleoIndex)) {
                    Item_1_Informacion_lugar_cargue.nombreNucleo = cursor.getString(nucleoIndex)
                }
                val fincaIndex = cursor.getColumnIndex(COLUMN_FINCA)
                if (fincaIndex != -1 && !cursor.isNull(fincaIndex)) {
                    Item_1_Informacion_lugar_cargue.nombreFinca = cursor.getString(fincaIndex)
                }

                //ITEM 2

                val conductorIndex = cursor.getColumnIndex(COLUMN_CONDUCTOR)
                if (conductorIndex != -1 && !cursor.isNull(conductorIndex)) {
                    Item_2_Informacion_del_conductor.nombreConductor = cursor.getString(conductorIndex)
                }

                val cedulaIndex = cursor.getColumnIndex(COLUMN_CEDULA_CONDUCTOR)
                if (cedulaIndex != -1 && !cursor.isNull(cedulaIndex)) {
                    Item_2_Informacion_del_conductor.cedula = cursor.getString(cedulaIndex)
                }
                val lugarExpedicionIndex = cursor.getColumnIndex(COLUMN_LUGAR_EXPEDICION_CC)
                if (lugarExpedicionIndex != -1 && !cursor.isNull(lugarExpedicionIndex)) {
                    Item_2_Informacion_del_conductor.lugarExpedicion = cursor.getString(lugarExpedicionIndex)
                }
                val licConduccionIndex = cursor.getColumnIndex(COLUMN_LIC_CONDUCCION)
                if (licConduccionIndex != -1 && !cursor.isNull(licConduccionIndex)) {
                    Item_2_Informacion_del_conductor.licConduccionRes = cursor.getString(licConduccionIndex)
                }

                val polizaRCEIndex = cursor.getColumnIndex(COLUMN_POLIZA_RCE)
                if (polizaRCEIndex != -1 && !cursor.isNull(polizaRCEIndex)) {
                    Item_2_Informacion_del_conductor.polizaRCERes = cursor.getString(polizaRCEIndex)
                }

                val epsResIndex = cursor.getColumnIndex(EPS_RES)
                if (epsResIndex != -1 && !cursor.isNull(epsResIndex)) {
                    Item_2_Informacion_del_conductor.epsRes = cursor.getString(epsResIndex)
                }
                val arlResIndex = cursor.getColumnIndex(ARL_RES)
                if (arlResIndex != -1 && !cursor.isNull(arlResIndex)) {
                    Item_2_Informacion_del_conductor.arlRes = cursor.getString(arlResIndex)
                }

                val afpResIndex = cursor.getColumnIndex(AFP_RES)
                if (afpResIndex != -1 && !cursor.isNull(afpResIndex)) {
                    Item_2_Informacion_del_conductor.afpRes = cursor.getString(afpResIndex)
                }

                val epsCualIndex = cursor.getColumnIndex(EPS_CUAL)
                if (epsCualIndex != -1 && !cursor.isNull(epsCualIndex)) {
                    Item_2_Informacion_del_conductor.cualEPS = cursor.getString(epsCualIndex)
                }

                val arlCualIndex = cursor.getColumnIndex(ARL_CUAL)
                if (arlCualIndex != -1 && !cursor.isNull(arlCualIndex)) {
                    Item_2_Informacion_del_conductor.cualARL = cursor.getString(arlCualIndex)
                }

                val afpCualIndex = cursor.getColumnIndex(AFP_CUAL)
                if (afpCualIndex != -1 && !cursor.isNull(afpCualIndex)) {
                    Item_2_Informacion_del_conductor.cualAFP = cursor.getString(afpCualIndex)
                }

                //ITEM 3

                val placaIndex = cursor.getColumnIndex(PLACA)
                if (placaIndex != -1 && !cursor.isNull(placaIndex)) {
                    Item_3_Informacion_vehiculo.placa = cursor.getString(placaIndex)
                }

                val vehiculoIndex = cursor.getColumnIndex(VEHICULO)
                if (vehiculoIndex != -1 && !cursor.isNull(vehiculoIndex)) {
                    Item_3_Informacion_vehiculo.vehiculo = cursor.getString(vehiculoIndex)
                }

                val tarjetaPropiedadIndex = cursor.getColumnIndex(TARJETA_PROPIEDAD)
                if (tarjetaPropiedadIndex != -1 && !cursor.isNull(tarjetaPropiedadIndex)) {
                    Item_3_Informacion_vehiculo.tarjetaPropiedad = cursor.getString(tarjetaPropiedadIndex)
                }

                val soatVigenteIndex = cursor.getColumnIndex(SOAT_VIGENTE)
                if (soatVigenteIndex != -1 && !cursor.isNull(soatVigenteIndex)) {
                    Item_3_Informacion_vehiculo.soatVigente = cursor.getString(soatVigenteIndex)
                }

                val revisionTecnomecanicaIndex = cursor.getColumnIndex(REVISION_TECNOMECANICA)
                if (revisionTecnomecanicaIndex != -1 && !cursor.isNull(revisionTecnomecanicaIndex)) {
                    Item_3_Informacion_vehiculo.revisionTecnicomecanica = cursor.getString(revisionTecnomecanicaIndex)
                }

                val lucesAltasIndex = cursor.getColumnIndex(LUCES_ALTAS)
                if (lucesAltasIndex != -1 && !cursor.isNull(lucesAltasIndex)) {
                    Item_3_Informacion_vehiculo.lucesAltas = cursor.getString(lucesAltasIndex)
                }

                val lucesBajasIndex = cursor.getColumnIndex(LUCES_BAJAS)
                if (lucesBajasIndex != -1 && !cursor.isNull(lucesBajasIndex)) {
                    Item_3_Informacion_vehiculo.lucesBajas = cursor.getString(lucesBajasIndex)
                }

                val direccionalesIndex = cursor.getColumnIndex(DIRECCIONALES)
                if (direccionalesIndex  != -1 && !cursor.isNull(direccionalesIndex)) {
                    Item_3_Informacion_vehiculo.direccionales = cursor.getString(direccionalesIndex)
                }

                val sonidoReversaIndex = cursor.getColumnIndex(SONIDO_REVERSA)
                if (sonidoReversaIndex  != -1 && !cursor.isNull(sonidoReversaIndex)) {
                    Item_3_Informacion_vehiculo.sonidoReversa = cursor.getString(sonidoReversaIndex)
                }

                val reversaIndex = cursor.getColumnIndex(REVERSA)
                if (reversaIndex != -1 && !cursor.isNull(reversaIndex)) {
                    Item_3_Informacion_vehiculo.reversa = cursor.getString(reversaIndex)
                }

                val stopIndex = cursor.getColumnIndex(STOP)
                if (stopIndex != -1 && !cursor.isNull(stopIndex)) {
                    Item_3_Informacion_vehiculo.stop = cursor.getString(stopIndex)
                }

                val retrovisoresIndex = cursor.getColumnIndex(RETROVISORES)
                if (retrovisoresIndex != -1 && !cursor.isNull(retrovisoresIndex)) {
                    Item_3_Informacion_vehiculo.retrovisores = cursor.getString(retrovisoresIndex)
                }

                val plumillasIndex = cursor.getColumnIndex(PLUMILLAS)
                if (plumillasIndex != -1 && !cursor.isNull(plumillasIndex)) {
                    Item_3_Informacion_vehiculo.plumillas = cursor.getString(plumillasIndex)
                }

                val estadoPanoramicosIndex = cursor.getColumnIndex(ESTADO_PANORAMICOS)
                if (estadoPanoramicosIndex != -1 && !cursor.isNull(estadoPanoramicosIndex)) {
                    Item_3_Informacion_vehiculo.estadoPanoramicos = cursor.getString(estadoPanoramicosIndex)
                }

                val espejosIndex = cursor.getColumnIndex(ESPEJOS)
                if (espejosIndex != -1 && !cursor.isNull(espejosIndex)) {
                    Item_3_Informacion_vehiculo.espejos = cursor.getString(espejosIndex)
                }

                val bocinaIndex = cursor.getColumnIndex(BOCINA)
                if (bocinaIndex != -1 && !cursor.isNull(bocinaIndex)) {
                    Item_3_Informacion_vehiculo.bocina = cursor.getString(bocinaIndex)
                }

                val cinturonIndex = cursor.getColumnIndex(CINTURON)
                if (cinturonIndex != -1 && !cursor.isNull(cinturonIndex)) {
                    Item_3_Informacion_vehiculo.cinturon = cursor.getString(cinturonIndex)
                }

                val frenoIndex = cursor.getColumnIndex(FRENO)
                if (frenoIndex != -1 && !cursor.isNull(frenoIndex)) {
                    Item_3_Informacion_vehiculo.freno = cursor.getString(frenoIndex)
                }

                val llantasIndex = cursor.getColumnIndex(LLANTAS)
                if (llantasIndex != -1 && !cursor.isNull(llantasIndex)) {
                    Item_3_Informacion_vehiculo.llantas = cursor.getString(llantasIndex)
                }

                val botiquinIndex = cursor.getColumnIndex(BOTIQUIN)
                if (botiquinIndex != -1 && !cursor.isNull(botiquinIndex)) {
                    Item_3_Informacion_vehiculo.botiquin = cursor.getString(botiquinIndex)
                }

                val extintorABCIndex = cursor.getColumnIndex(EXTINTOR_ABC)
                if (extintorABCIndex != -1 && !cursor.isNull(extintorABCIndex)) {
                    Item_3_Informacion_vehiculo.extintorABC = cursor.getString(extintorABCIndex)
                }

                val botasIndex = cursor.getColumnIndex(BOTAS)
                if (botasIndex != -1 && !cursor.isNull(botasIndex)) {
                    Item_3_Informacion_vehiculo.botas = cursor.getString(botasIndex)
                }

                val chalecoIndex = cursor.getColumnIndex(CHALECO)
                if (chalecoIndex != -1 && !cursor.isNull(chalecoIndex)) {
                    Item_3_Informacion_vehiculo.chaleco = cursor.getString(chalecoIndex)
                }

                val cascoIndex = cursor.getColumnIndex(CASCO)
                if (cascoIndex != -1 && !cursor.isNull(cascoIndex)) {
                    Item_3_Informacion_vehiculo.casco = cursor.getString(cascoIndex)
                }

                val carroceriaIndex = cursor.getColumnIndex(CARROCERIA)
                if (carroceriaIndex != -1 && !cursor.isNull(carroceriaIndex)) {
                    Item_3_Informacion_vehiculo.carroceria = cursor.getString(carroceriaIndex)
                }

                val dosEslingasBancoIndex = cursor.getColumnIndex(DOS_ESLINGAS_POR_BANCO)
                if (dosEslingasBancoIndex != -1 && !cursor.isNull(dosEslingasBancoIndex)) {
                    Item_3_Informacion_vehiculo.dosEslingasBanco = cursor.getString(dosEslingasBancoIndex)
                }

                val dosConosReflectivosIndex = cursor.getColumnIndex(DOS_CONOS_REFLECTIVOS)
                if (dosConosReflectivosIndex != -1 && !cursor.isNull(dosConosReflectivosIndex)) {
                    Item_3_Informacion_vehiculo.dosConosReflectivos = cursor.getString(dosConosReflectivosIndex)
                }

                val paralesIndex = cursor.getColumnIndex(PARALES)
                if (paralesIndex != -1 && !cursor.isNull(paralesIndex)) {
                    Item_3_Informacion_vehiculo.parales = cursor.getString(paralesIndex)
                }

                val observacionesIndex = cursor.getColumnIndex(OBSERVACIONES_CAMION)
                if (observacionesIndex != -1 && !cursor.isNull(observacionesIndex)) {
                    Item_3_Informacion_vehiculo.observacionesCamion = cursor.getString(observacionesIndex)
                }

                //ITEM 4

                val horaSalidaIndex = cursor.getColumnIndex(HORA_SALIDA)
                if (horaSalidaIndex != -1 && !cursor.isNull(horaSalidaIndex)) {
                    Item_4_Estado_cargue.horaSalida = cursor.getString(horaSalidaIndex)
                }

                val fotoCamionIndex = cursor.getColumnIndex(FOTO_CAMION)
                if (fotoCamionIndex != -1 && !cursor.isNull(fotoCamionIndex)) {
                    Item_4_Estado_cargue.fotoCamion = cursor.getString(fotoCamionIndex)
                }

                val maderaNoSuperaMamparaIndex = cursor.getColumnIndex(MADERA_NO_SUPERA_MAMPARA)
                if (maderaNoSuperaMamparaIndex != -1 && !cursor.isNull(maderaNoSuperaMamparaIndex)) {
                    Item_4_Estado_cargue.maderaNoSuperaMampara = cursor.getString(maderaNoSuperaMamparaIndex)
                }

                val maderaNoSuperaParalesIndex = cursor.getColumnIndex(MADERA_NO_SUPERA_PARALES)
                if (maderaNoSuperaParalesIndex != -1 && !cursor.isNull(maderaNoSuperaParalesIndex)) {
                    Item_4_Estado_cargue.maderaNoSuperaParales = cursor.getString(maderaNoSuperaParalesIndex)
                }

                val noMaderaAtraviesaMamparaIndex = cursor.getColumnIndex(NO_HAY_MADERA_ATRAVIESE_MAMPARA)
                if (noMaderaAtraviesaMamparaIndex != -1 && !cursor.isNull(noMaderaAtraviesaMamparaIndex)) {
                    Item_4_Estado_cargue.noMaderaAtraviesaMampara = cursor.getString(noMaderaAtraviesaMamparaIndex)
                }

                val paralesMismaAlturaIndex = cursor.getColumnIndex(PARALES_MISMA_ALTURA)
                if (paralesMismaAlturaIndex != -1 && !cursor.isNull(paralesMismaAlturaIndex)) {
                    Item_4_Estado_cargue.paralesMismaAltura = cursor.getString(paralesMismaAlturaIndex)
                }

                val ningunaUndSobrepasaParalesIndex = cursor.getColumnIndex(NINGUNA_UND_SOBREPASA_PARALES)
                if (ningunaUndSobrepasaParalesIndex != -1 && !cursor.isNull(ningunaUndSobrepasaParalesIndex)) {
                    Item_4_Estado_cargue.ningunaUndSobrepasaParales = cursor.getString(ningunaUndSobrepasaParalesIndex)
                }
                val cadaBancoAseguradoEslingasIndex = cursor.getColumnIndex(CADA_BANCO_ASEGURADO_ESLINGAS)
                if (cadaBancoAseguradoEslingasIndex != -1 && !cursor.isNull(cadaBancoAseguradoEslingasIndex)) {
                    Item_4_Estado_cargue.cadaBancoAseguradoEslingas = cursor.getString(cadaBancoAseguradoEslingasIndex)
                }

                val carroceriaParalesBuenEstadoIndex = cursor.getColumnIndex(CARROCERIA_Y_PARALES_BUEN_ESTADO)
                if (carroceriaParalesBuenEstadoIndex != -1 && !cursor.isNull(carroceriaParalesBuenEstadoIndex)) {
                    Item_4_Estado_cargue.carroceriaParalesBuenEstado = cursor.getString(carroceriaParalesBuenEstadoIndex)
                }

                val conductorSalioLugarCinturonIndex = cursor.getColumnIndex(CONDUCTOR_SALIO_LUGAR_CON_CINTURON)
                if (conductorSalioLugarCinturonIndex != -1 && !cursor.isNull(conductorSalioLugarCinturonIndex)) {
                    Item_4_Estado_cargue.conductorSalioLugarCinturon = cursor.getString(conductorSalioLugarCinturonIndex)
                }

                val paralesAbatiblesAseguradosEstrobosIndex = cursor.getColumnIndex(PARALES_ABATIBLES_ASEGURADOS_ESTROBOS)
                if (paralesAbatiblesAseguradosEstrobosIndex != -1 && !cursor.isNull(paralesAbatiblesAseguradosEstrobosIndex)) {
                    Item_4_Estado_cargue.paralesAbatiblesAseguradosEstrobos = cursor.getString(paralesAbatiblesAseguradosEstrobosIndex)
                }

                //FIRMAS

                val nombreSupervisorIndex = cursor.getColumnIndex(NOMBRE_SUPERVISOR_FIRMA)
                if (nombreSupervisorIndex != -1 && !cursor.isNull(nombreSupervisorIndex)) {
                    nombre_Supervisor = cursor.getString(nombreSupervisorIndex)
                }

                val firmaSupervisorIndex = cursor.getColumnIndex(FIRMA_SUPERVISOR)
                if (firmaSupervisorIndex != -1 && !cursor.isNull(firmaSupervisorIndex)) {
                    firma_Supervisor = cursor.getString(firmaSupervisorIndex)
                }

                val nombreDespachadorIndex = cursor.getColumnIndex(NOMBRE_DESPACHADOR_FIRMA)
                if (nombreDespachadorIndex != -1 && !cursor.isNull(nombreDespachadorIndex)) {
                    nombre_Despachador = cursor.getString(nombreDespachadorIndex)
                }

                val firmaDespachadorIndex = cursor.getColumnIndex(FIRMA_DESPACHADOR)
                if (firmaDespachadorIndex != -1 && !cursor.isNull(firmaDespachadorIndex)) {
                    firma_Despachador = cursor.getString(firmaDespachadorIndex)
                }

                val nombreConductorIndex = cursor.getColumnIndex(NOMBRE_CONDUCTOR_FIRMA)
                if (nombreConductorIndex != -1 && !cursor.isNull(nombreConductorIndex)) {
                    nombre_Conductor = cursor.getString(nombreConductorIndex)
                }

                val firmaConductorIndex = cursor.getColumnIndex(FIRMA_CONDUCTOR)
                if (firmaConductorIndex != -1 && !cursor.isNull(firmaConductorIndex)) {
                    firma_Conductor = cursor.getString(firmaConductorIndex)
                }

                val nombreOperadorIndex = cursor.getColumnIndex(NOMBRE_OPERADOR_FIRMA)
                if (nombreOperadorIndex != -1 && !cursor.isNull(nombreOperadorIndex)) {
                    nombre_Operador = cursor.getString(nombreOperadorIndex)
                }

                val firmaOperadorIndex = cursor.getColumnIndex(FIRMA_OPERADOR)
                if (firmaOperadorIndex != -1 && !cursor.isNull(firmaOperadorIndex)) {
                    firma_Operador = cursor.getString(firmaOperadorIndex)
                }

            }
            list.add(listaCargue)
        }
        cursor.close()
        db.close()
        return list
    }

    fun updateList(lista: ListasCargueModel) {
        val db = writableDatabase
        val values = ContentValues().apply {

            put(COLUMN_HORA, lista.Item_1_Informacion_lugar_cargue.horaEntrada)
            put(COLUMN_FECHA, lista.Item_1_Informacion_lugar_cargue.fecha)
            put(COLUMN_TIPO_CARGUE, lista.Item_1_Informacion_lugar_cargue.tipoCargue)
            put(COLUMN_ZONA, lista.Item_1_Informacion_lugar_cargue.nombreZona)
            put(COLUMN_NUCLEO,lista.Item_1_Informacion_lugar_cargue.nombreNucleo)
            put(COLUMN_FINCA, lista.Item_1_Informacion_lugar_cargue.nombreFinca)

            put(COLUMN_CONDUCTOR, lista.Item_2_Informacion_del_conductor.nombreConductor)
            put(COLUMN_CEDULA_CONDUCTOR, lista.Item_2_Informacion_del_conductor.cedula)
            put(COLUMN_LUGAR_EXPEDICION_CC, lista.Item_2_Informacion_del_conductor.lugarExpedicion)
            put(COLUMN_LIC_CONDUCCION, lista.Item_2_Informacion_del_conductor.licConduccionRes)
            put(COLUMN_POLIZA_RCE, lista.Item_2_Informacion_del_conductor.polizaRCERes)
            put(EPS_RES, lista.Item_2_Informacion_del_conductor.epsRes)
            put(ARL_RES, lista.Item_2_Informacion_del_conductor.arlRes)
            put(AFP_RES, lista.Item_2_Informacion_del_conductor.afpRes)
            put(EPS_CUAL, lista.Item_2_Informacion_del_conductor.cualEPS)
            put(ARL_CUAL, lista.Item_2_Informacion_del_conductor.cualARL)
            put(AFP_CUAL, lista.Item_2_Informacion_del_conductor.cualAFP)

            put(PLACA, lista.Item_3_Informacion_vehiculo.placa)
            put(VEHICULO, lista.Item_3_Informacion_vehiculo.vehiculo)
            put(TARJETA_PROPIEDAD, lista.Item_3_Informacion_vehiculo.tarjetaPropiedad)
            put(SOAT_VIGENTE, lista.Item_3_Informacion_vehiculo.soatVigente)
            put(REVISION_TECNOMECANICA, lista.Item_3_Informacion_vehiculo.revisionTecnicomecanica)
            put(LUCES_ALTAS, lista.Item_3_Informacion_vehiculo.lucesAltas)
            put(LUCES_BAJAS, lista.Item_3_Informacion_vehiculo.lucesBajas)
            put(DIRECCIONALES, lista.Item_3_Informacion_vehiculo.direccionales)
            put(SONIDO_REVERSA, lista.Item_3_Informacion_vehiculo.sonidoReversa)
            put(REVERSA, lista.Item_3_Informacion_vehiculo.reversa)
            put(STOP, lista.Item_3_Informacion_vehiculo.stop)
            put(RETROVISORES, lista.Item_3_Informacion_vehiculo.retrovisores)
            put(PLUMILLAS, lista.Item_3_Informacion_vehiculo.plumillas)
            put(ESTADO_PANORAMICOS, lista.Item_3_Informacion_vehiculo.estadoPanoramicos)
            put(ESPEJOS, lista.Item_3_Informacion_vehiculo.espejos)
            put(BOCINA, lista.Item_3_Informacion_vehiculo.bocina)
            put(CINTURON, lista.Item_3_Informacion_vehiculo.cinturon)
            put(FRENO, lista.Item_3_Informacion_vehiculo.freno)
            put(LLANTAS, lista.Item_3_Informacion_vehiculo.llantas)
            put(BOTIQUIN, lista.Item_3_Informacion_vehiculo.botiquin)
            put(EXTINTOR_ABC, lista.Item_3_Informacion_vehiculo.extintorABC)
            put(BOTAS, lista.Item_3_Informacion_vehiculo.botas)
            put(CHALECO, lista.Item_3_Informacion_vehiculo.chaleco)
            put(CASCO, lista.Item_3_Informacion_vehiculo.casco)
            put(CARROCERIA, lista.Item_3_Informacion_vehiculo.carroceria)
            put(DOS_ESLINGAS_POR_BANCO, lista.Item_3_Informacion_vehiculo.dosEslingasBanco)
            put(DOS_CONOS_REFLECTIVOS, lista.Item_3_Informacion_vehiculo.dosConosReflectivos)
            put(PARALES, lista.Item_3_Informacion_vehiculo.parales)
            put(OBSERVACIONES_CAMION, lista.Item_3_Informacion_vehiculo.observacionesCamion)

            put(HORA_SALIDA, lista.Item_4_Estado_cargue.horaSalida)
            put(FOTO_CAMION, lista.Item_4_Estado_cargue.fotoCamion)
            put(MADERA_NO_SUPERA_MAMPARA, lista.Item_4_Estado_cargue.maderaNoSuperaMampara)
            put(MADERA_NO_SUPERA_PARALES, lista.Item_4_Estado_cargue.maderaNoSuperaParales)
            put(NO_HAY_MADERA_ATRAVIESE_MAMPARA, lista.Item_4_Estado_cargue.noMaderaAtraviesaMampara)
            put(PARALES_MISMA_ALTURA, lista.Item_4_Estado_cargue.paralesMismaAltura)
            put(NINGUNA_UND_SOBREPASA_PARALES, lista.Item_4_Estado_cargue.ningunaUndSobrepasaParales)
            put(CADA_BANCO_ASEGURADO_ESLINGAS, lista.Item_4_Estado_cargue.cadaBancoAseguradoEslingas)
            put(CARROCERIA_Y_PARALES_BUEN_ESTADO, lista.Item_4_Estado_cargue.carroceriaParalesBuenEstado)
            put(CONDUCTOR_SALIO_LUGAR_CON_CINTURON, lista.Item_4_Estado_cargue.conductorSalioLugarCinturon)
            put(PARALES_ABATIBLES_ASEGURADOS_ESTROBOS, lista.Item_4_Estado_cargue.paralesAbatiblesAseguradosEstrobos)

            put(NOMBRE_SUPERVISOR_FIRMA, lista.nombre_Supervisor)
            put(FIRMA_SUPERVISOR, lista.firma_Supervisor)
            put(NOMBRE_DESPACHADOR_FIRMA, lista.nombre_Despachador)
            put(FIRMA_DESPACHADOR, lista.firma_Despachador)
            put(NOMBRE_CONDUCTOR_FIRMA, lista.nombre_Conductor)
            put(FIRMA_CONDUCTOR, lista.firma_Conductor)
            put(NOMBRE_OPERADOR_FIRMA, lista.nombre_Operador)
            put(FIRMA_OPERADOR, lista.firma_Operador)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(lista.id_lista_local.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun deleteListById(noteId: Int) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(noteId.toString()))
        db.close()
    }
}