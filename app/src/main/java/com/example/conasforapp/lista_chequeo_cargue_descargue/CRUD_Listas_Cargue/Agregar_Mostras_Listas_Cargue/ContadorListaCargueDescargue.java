package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue;
public class ContadorListaCargueDescargue {
    private static int contador_lista = 0;

    public static int incrementoContador(){
        contador_lista++;
        return contador_lista;
    }

    public static int getContador() {
        return contador_lista;
    }

    public static void decrementoContador(){
        contador_lista--;
    }

    public static void contadorCero(){
        contador_lista = 0;
    }

    public static int obtenerContador(){
        return contador_lista;
    }

    public static void guardarContador() {
        contadorCero();
    }
}
