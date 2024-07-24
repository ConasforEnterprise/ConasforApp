package com.example.conasforapp.modelos

class ListasCargueModel {

    // Sobrescribimos equals y hashCode basados en id_lista
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as ListasCargueModel

        return id_lista == other.id_lista
    }
    override fun hashCode(): Int {
        return id_lista.hashCode()
    }

    var usuario :UsuariosModel = UsuariosModel()
    var nombre : String = ""
    var cargo : String = ""
    var fotoUsuario : String = ""
    var estadoLista : String = ""

    var firma_Supervisor: String? = null
    var nombre_Supervisor: String? = null
    var firma_Despachador: String? = null
    var nombre_Despachador: String? = null
    var firma_Conductor: String? = null
    var nombre_Conductor: String? = null
    var firma_Operador: String? = null
    var nombre_Operador: String? = null

    var id_lista : String = ""
    var lista_numero : String = ""
    var id_lista_local : Int = 0

    //var Item_0_DatosUsuario : Usuario = Usuario()
    var Item_1_Informacion_lugar_cargue : InfoLugarCargue = InfoLugarCargue()
    var Item_2_Informacion_del_conductor : InfoDelConductor = InfoDelConductor()
    var Item_3_Informacion_vehiculo : InfoDelVehiculo = InfoDelVehiculo()
    var Item_4_Estado_cargue : EstadoDelCargue = EstadoDelCargue()
    //var Item_5_Firmas_cargue_descargue : FirmasCargueDescargue = FirmasCargueDescargue()

    //6 Variables
    class InfoLugarCargue{
        var horaEntrada: String? = null
        var fecha: String? = null
        var tipoCargue : String = ""
        var nombreZona : String = ""
        var nombreNucleo : String = ""
        var nombreFinca : String = ""
    }

    //12 Variables
    class InfoDelConductor{
        var nombreConductor : String = ""
        var cedula : String = ""
        var lugarExpedicion : String = ""
        var licConduccionRes: String = ""
        var polizaRCERes : String = ""
        var epsRes : String = ""
        var arlRes : String = ""
        var afpRes : String = ""
        var cualEPS : String = ""
        var cualARL : String = ""
        var cualAFP : String = ""
    }

    class InfoDelVehiculo{
        var placa : String = ""
        var vehiculo : String = ""
        var tarjetaPropiedad : String = ""
        var soatVigente : String = ""
        var revisionTecnicomecanica : String = ""
        var lucesAltas : String = ""
        var lucesBajas : String = ""
        var direccionales : String = ""
        var sonidoReversa : String = ""
        var reversa : String = ""
        var stop : String = ""
        var retrovisores : String = ""
        var plumillas : String = ""
        var estadoPanoramicos : String = ""
        var espejos : String = ""
        var bocina : String = ""
        var cinturon : String = ""
        var freno : String = ""
        var llantas : String = ""
        var botiquin : String = ""
        var extintorABC : String = ""
        var botas : String = ""
        var chaleco : String = ""
        var casco : String = ""
        var carroceria : String = ""
        var dosEslingasBanco : String = ""
        var dosConosReflectivos : String = ""
        var parales : String = ""
        var observacionesCamion : String = ""
    }

    class EstadoDelCargue{
        var horaSalida: String? = null
        var fotoCamion: String? = null
        var maderaNoSuperaMampara : String = ""
        var maderaNoSuperaParales :String = ""
        var noMaderaAtraviesaMampara : String = ""
        var paralesMismaAltura : String = ""
        var ningunaUndSobrepasaParales : String = ""
        var cadaBancoAseguradoEslingas : String = ""
        var carroceriaParalesBuenEstado : String = ""
        var conductorSalioLugarCinturon : String = ""
        var paralesAbatiblesAseguradosEstrobos : String = ""
    }

    /*
    class FirmasCargueDescargue{
        var firma_Supervisor : String = ""
        var nombre_Supervisor : String = ""

        var firma_Despachador : String = ""
        var nombre_Despachador : String = ""

        var firma_Conductor : String = ""
        var nombre_Conductor : String = ""

        var firma_Operador : String = ""
        var nombre_Operador : String = ""
    }

     */
    class FirmasCargueDescargueOP{

        //val nombre : String = ""
        val datos : ByteArray
            //
            get() {
                TODO()
            }

        //var firmaSupervisor : String = ""
        //var firmaDespachador : String = ""
        //var firmaConductor : String = ""
        //var firmaOperador : String = ""
    }

    override fun toString(): String {
        return "ListaModel(" +
                "Item_1_Informacion_lugar_cargue=$Item_1_Informacion_lugar_cargue, " +
                "Item_2_Informacion_del_conductor=$Item_2_Informacion_del_conductor, " +
                "Item_3_Informacion_vehiculo=$Item_3_Informacion_vehiculo, " +
                "Item_4_Estado_cargue=$Item_4_Estado_cargue)"
    }
}