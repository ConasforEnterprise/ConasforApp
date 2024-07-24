package com.example.conasforapp.lista_chequeo_cargue_descargue.Sincronizar_Listas_Cargue;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.conasforapp.R;

public class LoadingDialog {

    Activity activity;
    AlertDialog dialog;

    LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));

        dialog = builder.create();
        dialog.show();
    }

    void dimissDialog(){
        dialog.dismiss();
    }
}
