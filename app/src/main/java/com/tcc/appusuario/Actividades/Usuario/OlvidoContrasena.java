package com.tcc.appusuario.Actividades.Usuario;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.tcc.appusuario.R;


public class OlvidoContrasena extends AppCompatActivity {

    private EditText edtEmailActualizar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvido_contrasena);



    }


}