package de.proneucon.mybroadcastreciverdemo;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //finish(); // der Reciver läuft dann eigenständig wenn setContentView() ausgeblendet/gelöscht wird

        //registrieren des BroadcastReveiver für OREO-PROBLEM
        receiver = new MyReceiver();         //neuen Receiver erstellen
        IntentFilter filter = new IntentFilter();       //Neuer IntentFilter erstellen
        filter.addAction("android.intent.action.AIRPLANE_MODE");  //Action aus der ManifestDatei
        registerReceiver(receiver , filter);            //registrieren des Receivers (mit receiver und filter)
        /*nach programmatischer Anmeldung kann jetzt auch der Receiver unter OREO verwendet werden*/


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //OREO-PROBLEM:
        unregisterReceiver(receiver);   //nur solange die App läuft werden die Notifications asgeführt
    }
}
