package com.login.howtosenddata;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Dialog dialog = new Dialog(getApplicationContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("amigos");
        DatabaseReference myRef2 = myRef.child("person");

        final EditText full_name=findViewById(R.id.your_full_name);
        final EditText mail=findViewById(R.id.your_email_address);
        final EditText pass=findViewById(R.id.create_new_password);
        Button SingUp=findViewById(R.id.sing_up);
        mAuth = FirebaseAuth.getInstance();
        SingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=full_name.getText().toString();
                String email=mail.getText().toString();
                String password=pass.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password);
                final Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("full_name",name);
                startActivity(intent);
                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //Do something after 100ms
//                        dialog.setContentView(R.layout.activity_login);
//                        dialog.setTitle("Title...");
//
//                    }
//                }, 3000);

            }
        });
        myRef2.addValueEventListener(new ValueEventListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null)

                    return;
                Log.e("TAG", "value :  " + dataSnapshot.child("date").getValue().toString());

                //Toast.makeText(getApplicationContext(), "there was a persone in front your door at : " + dataSnapshot.child("date").getValue().toString(), 5000).show();
                AlertDialog.Builder builder1 = new AlertDialog.Builder(login.this);
                builder1.setMessage("there was a persone in front of your door at : " + dataSnapshot.child("date").getValue().toString());
                builder1.setCancelable(false);
                builder1.setNeutralButton("Open Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), Streaming.class);
                        startActivity(intent);
                    }
                });
                builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });





                AlertDialog alert11 = builder1.create();
                alert11.show();
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notify=new Notification.Builder
                        (getApplicationContext()).setContentTitle("VISITOR").setContentText("there is some one at your door").
                        setContentTitle("VISITOR").setSmallIcon(R.mipmap.ic_launcher).setSound(alarmSound).build();

                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                notif.notify(0, notify);

                //JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                //Log.e("JSOOOON", "jsonObject :  " + jsonObject.toString());
                //String date = jsonObject.getString("date");
                //    Log.e("Dateee", "DATEE :  " +date);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
