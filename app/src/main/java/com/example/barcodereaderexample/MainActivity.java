package com.example.barcodereaderexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(view -> mGetContent.launch(new Intent(this,ScannerActivity.class)));

    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intent = result.getData();
                    if(intent!=null) {
                        String codigo = intent.getStringExtra(ScannerActivity.CODE);
                        Toast.makeText(MainActivity.this, codigo, Toast.LENGTH_LONG).show();
                    }
                }
    });
}