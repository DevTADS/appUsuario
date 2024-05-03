package com.tcc.appusuario.Actividades.Usuario;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tcc.appusuario.R;

import java.util.HashMap;
import java.util.Map;

public class RegistroUsuario extends AppCompatActivity {

    EditText txtName;
    EditText txtEmail;
    EditText pass;
    Spinner spinnerPais;
    Spinner spinnerCiudad;
    EditText txtCedula;
    EditText txtTelefono;
    EditText txtDireccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.usuario_activity_registro);

        txtName = findViewById(R.id.ednombre);
        txtEmail = findViewById(R.id.etemail);
        pass = findViewById(R.id.etcontraseña);
        spinnerPais = findViewById(R.id.spinnerPais);
        spinnerCiudad = findViewById(R.id.spinnerCiudad);
        txtCedula = findViewById(R.id.txtCedula);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtDireccion = findViewById(R.id.txtDireccion);
        Button btnRegresar = findViewById(R.id.btn_regresar);

        // Configuración del Spinner de Países
        ArrayAdapter<CharSequence> adapterPaises = ArrayAdapter.createFromResource(this, R.array.paises, android.R.layout.simple_spinner_item);
        adapterPaises.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPais.setAdapter(adapterPaises);

        spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccionarPais = parent.getItemAtPosition(position).toString();
                if (seleccionarPais.equals("Uruguay")) {
                    DepartamentosUruguay();
                } else if (seleccionarPais.equals("Brasil")) {
                    EstadosBrasil();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btn_insert = findViewById(R.id.btn_registrar);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroUsuario.this, LoginUsuario.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registrarUsuario() {
        final String nombre = txtName.getText().toString().trim();
        final String email = txtEmail.getText().toString().trim();
        final String password = pass.getText().toString().trim();
        final String pais = spinnerPais.getSelectedItem().toString().trim();
        final String ciudad = spinnerCiudad.getSelectedItem().toString().trim();
        final String cedula = txtCedula.getText().toString().trim();
        final String telefono = txtTelefono.getText().toString().trim();
        final String direccion = txtDireccion.getText().toString().trim();

        if (nombre.isEmpty()) {
            txtName.setError("Este campo no puede estar vacío");
            return;
        }
        if (email.isEmpty()) {
            txtEmail.setError("Este campo no puede estar vacío");
            return;
        }
        if (password.isEmpty()) {
            pass.setError("Este campo no puede estar vacío");
            return;
        }
        if (cedula.isEmpty()) {
            txtCedula.setError("Este campo no puede estar vacío");
            return;
        }
        if (telefono.isEmpty()) {
            txtTelefono.setError("Este campo no puede estar vacío");
            return;
        }
        if (direccion.isEmpty()) {
            txtDireccion.setError("Este campo no puede estar vacío");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.show();

        // Realizar solicitud HTTP para registrar el usuario
        String url = "https://qybdatye.lucusvirtual.es/tcc/usuario/insertar.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss(); // Ocultar el ProgressDialog
                        // Mostrar la respuesta del servidor para depuración
                        Toast.makeText(RegistroUsuario.this, response, Toast.LENGTH_SHORT).show();

                        if (response.equalsIgnoreCase("Datos insertados")) {
                            Toast.makeText(RegistroUsuario.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();

                            // Volver a la pantalla de inicio de sesión
                            Intent intent = new Intent(RegistroUsuario.this, LoginUsuario.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss(); // Ocultar el ProgressDialog
                        Toast.makeText(RegistroUsuario.this, "Error al registrar el usuario: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("email", email);
                params.put("password", password);
                params.put("pais", pais);
                params.put("ciudad", ciudad);
                params.put("cedula", cedula);
                params.put("telefono", telefono);
                params.put("direccion", direccion);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void DepartamentosUruguay() {
        ArrayAdapter<CharSequence> adapterDepartamentos = ArrayAdapter.createFromResource(this, R.array.departamentos, android.R.layout.simple_spinner_item);
        adapterDepartamentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCiudad.setAdapter(adapterDepartamentos);
    }

    private void EstadosBrasil() {
        ArrayAdapter<CharSequence> adapterEstados = ArrayAdapter.createFromResource(this, R.array.rio_grande_do_sul, android.R.layout.simple_spinner_item);
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCiudad.setAdapter(adapterEstados);
    }
}
