package com.example.barcodereaderexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.zxing.BarcodeFormat;

import java.util.Arrays;

public class ScannerActivity extends AppCompatActivity {
    private static final String TAG = "ScannerActivity";
    public static final String CODE = "CODE";
    private static final int RC_PERMISSION = 10;
    private CodeScanner mCodeScanner;
    private boolean mPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this,scannerView);

        //Formato: Code 128
        mCodeScanner.setFormats(Arrays.asList(BarcodeFormat.CODE_128));
        //Obtiene resultado
        mCodeScanner.setDecodeCallback(result -> {
            runOnUiThread(() -> {
                String code = result.getText();
                Intent intent = new Intent();
                intent.putExtra(CODE, code);
                setResult(RESULT_OK,intent);
                finish();
            });
        });

        mCodeScanner.setErrorCallback(error -> {
            String errMsg = error.getMessage();
            Log.d(TAG,errMsg);
            setResult(RESULT_CANCELED);
            finish();
        });
        //scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
        //Solicita permisos si no los tiene
        checkPermission();
    }


    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[] {Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = true;
                mCodeScanner.startPreview();
            } else {
                mPermissionGranted = false;
            }
        }
    }

    //Control de ciclo de vida
    @Override
    protected void onResume() {
        super.onResume();
        if(mPermissionGranted)
            mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}