package com.example.recibirsms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String mensaje = "";
    EditText mensa ;

    public MainActivity(String strMessage) {
        this.mensaje = strMessage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button boton = (Button) findViewById(R.id.btnEnviar);
        boton.setOnClickListener(this);

        mensa = (EditText) findViewById(R.id.textview);

    }


    @Override
    public void onClick(View v) {
        Context context = this;

        String regex = "//d{8} [0-9]";

        boolean validacion = Pattern.matches(regex, mensaje);

        if(validacion==true){

            Toast.makeText(context, "Codigo Correcto Gracias", Toast.LENGTH_LONG).show();
            mensa.setText(mensaje); //lo pongo aqui porque he intendado ponerlo en el onreceiver que tiene mas sentido pero no funciona porque no entra en el


        }else{

            Toast.makeText(context, "Codigo Incorrecto", Toast.LENGTH_LONG).show();

        }




    }
}