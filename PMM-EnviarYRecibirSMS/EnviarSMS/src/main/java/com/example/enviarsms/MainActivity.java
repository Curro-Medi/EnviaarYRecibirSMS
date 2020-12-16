package com.example.enviarsms;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.PendingIntent;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private EditText puerto;
    SmsManager sms = SmsManager.getDefault();
    String texto = "";
    NotificationCompat.Builder noti ;
    private static final int idNoti=51625;
    String valor = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button boton = (Button) findViewById(R.id.btnEnviar);
        boton.setOnClickListener(this);

        noti = new NotificationCompat.Builder(this);
        noti.setAutoCancel(true);

        puerto = (EditText) findViewById(R.id.textview);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v){

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new
                ActivityResultCallback<Boolean>(){
                    @Override
                    public void onActivityResult(Boolean isGranted){
                        if (isGranted) {
                            Toast toad = Toast.makeText(MainActivity.this, "Permisos concedidos"  , Toast.LENGTH_SHORT);
                            toad.show();
                        } else {

                            Toast toad = Toast.makeText(MainActivity.this, "No "  , Toast.LENGTH_SHORT);
                            toad.show();
                        }
                    }
                });

        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        Network currentNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(currentNetwork);

        if(caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
            Toast toad = Toast.makeText(MainActivity.this, "Muy buena, tienes internet"  , Toast.LENGTH_SHORT);
            toad.show();
            if (ContextCompat.checkSelfPermission(
                    MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {


            } else {
                requestPermissionLauncher.launch(
                        Manifest.permission.SEND_SMS);
            }
        }else{
            Toast toad = Toast.makeText(MainActivity.this, "No hay conexion a internet "  , Toast.LENGTH_SHORT);
            toad.show();
        }

        valor = "";
        for(int i = 0; i<8; i++) {
            int valorEntero = (int) Math.floor(Math.random()*(10));

            System.out.println(valorEntero);

            valor += ""+valorEntero;
        }
        texto = "El numero secreto es --> " + valor;
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(puerto.getText().toString(), null, texto,null, null);

        notificar();

    }

    public void notificar(){
        try{

            noti.setSmallIcon(R.drawable.payaso);
            noti.setTicker("Illoo la notificacion");
            noti.setWhen(System.currentTimeMillis());
            noti.setContentTitle("¿Quieres lanzar el intent?");
            noti.setContentText("Pulsa para lanzar");

            Intent IntentNoti = new Intent(Intent.ACTION_SENDTO);

            IntentNoti.setData(Uri.parse("smsto:" + puerto.getText().toString()));
            IntentNoti.putExtra("sms_body", texto);

            PendingIntent irintent = PendingIntent.getActivity(this, 1, IntentNoti, PendingIntent.FLAG_UPDATE_CURRENT);

            noti.setContentIntent(irintent);

            NotificationManager notificacion = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificacion.notify(idNoti, noti.build());



        }catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

    }

}
