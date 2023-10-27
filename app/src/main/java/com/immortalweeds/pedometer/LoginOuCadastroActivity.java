package com.immortalweeds.pedometer;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.immortalweeds.pedometer.FormularioActivity;
import com.immortalweeds.pedometer.databinding.ActivityLoginOuCadastroBinding;
import com.immortalweeds.pedometer.databinding.CarregandoLayoutBinding;

public class LoginOuCadastroActivity extends AppCompatActivity {

    private AlertDialog dialog_carregando;
    private Button btnCad;

    private Button btnLogin;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ActivityLoginOuCadastroBinding vb ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityLoginOuCadastroBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());
        btnCad = findViewById(R.id.btn_cad);
        btnLogin = findViewById(R.id.login_btn);
        configurarAlert();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // verifica se o user já ta logado
        if ( user != null){
            Toast.makeText(this, "Bem-vindo de volta!", Toast.LENGTH_SHORT).show();
            startActivity(
                    new Intent(getApplicationContext(), CountStep.class)
            );
        }else{
            Toast.makeText(
                    this, "Faça login ou se cadastre!", Toast.LENGTH_SHORT
            ).show();
        }


        vb.checkSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vb.checkSenha.isChecked()) {
                    vb.edtDigitePassword.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    vb.edtDigitePassword.setInputType(
                            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    );
                }
            }
        });


        btnLogin.setOnClickListener(vLogin ->{
            dialog_carregando.show();
            String email, senha;
            email = vb.edtDigiteEmail.getText().toString().trim();
            senha = vb.edtDigitePassword.getText().toString().trim();

            if ( !email.isEmpty()  && !senha.isEmpty()){
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
                    if ( task.isSuccessful() ){
                        dialog_carregando.dismiss();
                        startActivity(
                                new Intent(getApplicationContext(), CountStep.class)
                        );
                    }else{
                        dialog_carregando.dismiss();
                        Toast.makeText(LoginOuCadastroActivity.this, "Credenciais erradas.", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                dialog_carregando.dismiss();
            }


        });


        btnCad.setOnClickListener( viewCad -> {
            startActivity(
                    new Intent(getApplicationContext(), FormularioActivity.class)
            );
        });
    }

    public void configurarAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginOuCadastroActivity.this);
        builder.setView(CarregandoLayoutBinding.inflate(getLayoutInflater()).getRoot());
        builder.setCancelable(false);
        dialog_carregando = builder.create();
    }
}