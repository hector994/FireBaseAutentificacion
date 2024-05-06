package com.example.firebaseautentificacion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button btnIniciarSesion;
    EditText txtCorreo, txtPassword;
    private FirebaseAuth objFirebase;
    private FirebaseAuth.AuthStateListener objFirebaseListener;
    private ProgressDialog objDialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnIniciarSesion = findViewById(R.id.buttonLogin);
        txtCorreo = findViewById(R.id.editTextEmail);
        txtPassword = findViewById(R.id.editTextPassword);
        objFirebase = FirebaseAuth.getInstance();
        objFirebaseListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser objUsuario = firebaseAuth.getCurrentUser();
                if (objUsuario != null) {
                    cargarPrincipal();
                }
            }
        };
        objDialogo = new ProgressDialog(this);
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

            public void onCLick(View view) {
                iniciarSesion();
            }
        });
    }

    private void cargarPrincipal() {
        Intent objVentana = new Intent(Login.this, MainActivity.class);
        startActivity(objVentana);
        this.finish();
    }

    private void iniciarSesion() {
        objDialogo.setMessage("Iniciandosesion..");
        objDialogo.show();
        objFirebase.signInWithEmailAndPassword(txtCorreo.getText().toString(),
                txtPassword.getText().toString()).addOnCompleteListener(
                Login.this, new OnCompleteListener<AuthResult>(){
            public void onComplete (Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    objDialogo.dismiss();
                    cargarPrincipal();
                } else {
                    objDialogo.dismiss();
                    Toast.makeText(Login.this, "Usuario o Password incorrecto", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        objFirebase.addAuthStateListener(objFirebaseListener);
    }
    protected void onStop() {
        super.onStop();
        if (objFirebaseListener != null) {
            objFirebase.removeAuthStateListener(objFirebaseListener);
        }
    }
}