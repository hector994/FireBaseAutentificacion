package com.example.firebaseautentificacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView tvBienvenida;
    Button btnSalir;
    private FirebaseAuth objFirebase;
    private FirebaseAuth.AuthStateListener objFirebaseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvBienvenida = findViewById(R.id.textView);
        btnSalir = findViewById(R.id.button);
        objFirebase = FirebaseAuth.getInstance();
        //Listener
        objFirebaseListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser objUsuario = firebaseAuth.getCurrentUser();
                if (objUsuario != null) {
                    tvBienvenida.setText("Bienvenido/a: " + objUsuario.getEmail());
                }
            }
        };
        btnSalir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                salir();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        objFirebase.addAuthStateListener(objFirebaseListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (objFirebaseListener != null) {
            objFirebase.removeAuthStateListener(objFirebaseListener);
        }
    }
    private void salir() {
        objFirebase.signOut();
        Intent objLogin = new Intent(MainActivity.this, Login.class);
        startActivity(objLogin);
        this.finish();
    }
}