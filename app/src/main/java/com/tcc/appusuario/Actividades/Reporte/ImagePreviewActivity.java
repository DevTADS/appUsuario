package com.tcc.appusuario.Actividades.Reporte;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.tcc.appusuario.R;

public class ImagePreviewActivity extends AppCompatActivity {

    private int imageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        ImageView imageViewPreview = findViewById(R.id.imageViewPreview);
        ImageView imageViewDelete = findViewById(R.id.imageViewDelete);

        Uri imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());
        imageViewPreview.setImageBitmap(bitmap);

        imageIndex = getIntent().getIntExtra("imageIndex", -1);

        // Eliminar la foto al hacer clic en la opción de eliminar
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que quieres eliminar esta foto?")
                .setCancelable(false)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteImage();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteImage() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("imageIndex", imageIndex);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
