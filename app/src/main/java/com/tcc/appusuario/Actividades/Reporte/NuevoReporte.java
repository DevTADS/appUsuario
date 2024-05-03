package com.tcc.appusuario.Actividades.Reporte;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tcc.appusuario.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NuevoReporte extends AppCompatActivity {

    private static final  int IMAGE_PREVIEW_REQUEST = 101;
    private EditText descripcionEditText;
    private Spinner prioridadSpinner;
    private Spinner divisionSpinner;
    private Button selectImageButton, enviarReporteButton;
    private ImageView imageViewPreview1, imageViewPreview2, imageViewPreview3;
    private static final int PICK_IMAGE = 1;
    private List<ImageView> imageViews = new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<File> imageFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nuevo_reporte);

        divisionSpinner = findViewById(R.id.spinner_division);
        descripcionEditText = findViewById(R.id.et_descripcion);
        prioridadSpinner = findViewById(R.id.spinner_prioridad);
        selectImageButton = findViewById(R.id.btn_select_image);
        enviarReporteButton = findViewById(R.id.btn_enviar_reporte);
        imageViewPreview1 = findViewById(R.id.imageViewPreview1);
        imageViewPreview2 = findViewById(R.id.imageViewPreview2);
        imageViewPreview3 = findViewById(R.id.imageViewPreview3);

        imageViews.add(imageViewPreview1);
        imageViews.add(imageViewPreview2);
        imageViews.add(imageViewPreview3);

        // Configuración del Spinner División
        String[] divisiones = {"Division 1", "Division 2", "Division 3", "Division 4"}; // Divisiones de ejemplo, cámbialas según tus necesidades
        ArrayAdapter<String> divisionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, divisiones);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(divisionAdapter);

        // Configuración del Spinner Prioridad
        String[] prioridades = {"Alta", "Media", "Baja"}; // Prioridades de ejemplo, cámbialas según tus necesidades
        ArrayAdapter<String> prioridadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, prioridades);
        prioridadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioridadSpinner.setAdapter(prioridadAdapter);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        enviarReporteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarReporte();
            }
        });

        // Configurar onClickListeners para las imágenes previas
        imageViewPreview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePreview(0);
            }
        });

        imageViewPreview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePreview(1);
            }
        });

        imageViewPreview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePreview(2);
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                // Aquí puedes mostrar la imagen seleccionada si lo necesitas
                for (int i = 0; i < imageViews.size(); i++) {
                    if (imageViews.get(i).getDrawable() == null) {
                        imageViews.get(i).setImageBitmap(bitmap);
                        bitmaps.add(bitmap); // Agregar el bitmap a la lista de bitmaps

                        // Guardar la imagen en un archivo temporal
                        File file = createImageFile(bitmap);
                        imageFiles.add(file);

                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == IMAGE_PREVIEW_REQUEST && data != null) {
            int imageIndex = data.getIntExtra("imageIndex", -1);
            if (imageIndex != -1) {
                // Eliminar la imagen y actualizar la interfaz de usuario
                imageFiles.remove(imageIndex);
                bitmaps.remove(imageIndex);
                imageViews.get(imageIndex).setImageDrawable(null);
            }
        }
    }


    private File createImageFile(Bitmap bitmap) {
        // Guardar la imagen en un archivo temporal
        try {
            File file = File.createTempFile("image", ".jpg", getCacheDir());
            return saveBitmap(bitmap, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File saveBitmap(Bitmap bitmap, File file) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitmapData = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showImagePreview(int index) {
        if (imageFiles.size() > index) {
            Intent intent = new Intent(this, ImagePreviewActivity.class);
            intent.putExtra("imageUri", Uri.fromFile(imageFiles.get(index)).toString());
            intent.putExtra("imageIndex", index);
            startActivityForResult(intent, IMAGE_PREVIEW_REQUEST);
        }
    }

    private void enviarReporte() {
        final String descripcion = descripcionEditText.getText().toString().trim();
        final String prioridad = prioridadSpinner.getSelectedItem().toString().trim();
        final String division = divisionSpinner.getSelectedItem().toString().trim();
        final String id_usuario = "1"; // Aquí debes obtener el ID del usuario activo de alguna manera
        final String hora = "00:00:00"; // Aquí debes obtener la hora actual
        final String fecha = "2024-05-07"; // Aquí debes obtener la fecha actual
        final String geolocalizacion = "latitud,longitud"; // Aquí debes obtener la geolocalización del dispositivo (latitud y longitud)
        final String status = "Pendiente"; // Por defecto, el status es "Pendiente", podrías cambiarlo según tus necesidades

        // Verificar si la descripción está vacía
        if (descripcion.isEmpty()) {
            descripcionEditText.setError("Este campo no puede estar vacío");
            return;
        }

        // Verificar si hay imágenes seleccionadas
        if (bitmaps.isEmpty()) {
            Toast.makeText(this, "Error: No se han seleccionado imágenes", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar ProgressDialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando reporte...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Realizar solicitud HTTP para enviar el reporte
        String url = "https://qybdatye.lucusvirtual.es/tcc/reporte/insertar.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Ocultar ProgressDialog
                        progressDialog.dismiss();

                        // Mostrar la respuesta del servidor para depuración
                        Toast.makeText(NuevoReporte.this, response, Toast.LENGTH_SHORT).show();

                        if (response.equalsIgnoreCase("Reporte enviado")) {
                            Toast.makeText(NuevoReporte.this, "Reporte enviado correctamente", Toast.LENGTH_SHORT).show();
                            // Puedes redirigir a otra actividad aquí si es necesario
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Ocultar ProgressDialog
                        progressDialog.dismiss();

                        Toast.makeText(NuevoReporte.this, "Error al enviar reporte: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("descripcion", descripcion);
                params.put("prioridad", prioridad);
                params.put("division", division);
                params.put("id_usuario", id_usuario);
                params.put("hora", hora);
                params.put("fecha", fecha);
                params.put("geolocalizacion", geolocalizacion);
                params.put("status", status);

                // Agregar las imágenes al final de los parámetros de la solicitud HTTP
                for (int i = 0; i < bitmaps.size(); i++) {
                    params.put("imagen_" + i, fileToBase64(imageFiles.get(i)));
                }

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    // Método para convertir un archivo a una cadena Base64
    private String fileToBase64(File file) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
