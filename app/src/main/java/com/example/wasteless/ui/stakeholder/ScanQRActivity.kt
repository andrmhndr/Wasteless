package com.example.wasteless.ui.stakeholder

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.*
import com.example.wasteless.R
import com.example.wasteless.viewmodel.ScanQRViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ScanQRActivity : AppCompatActivity() {
    private lateinit var scanQRViewModel: ScanQRViewModel
    private lateinit var mAuth: FirebaseAuth

    private lateinit var scanner: CodeScanner
    private lateinit var scannerView: CodeScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qractivity)

        scanQRViewModel = ViewModelProvider(this).get(ScanQRViewModel::class.java)

        mAuth = Firebase.auth

        scannerView = findViewById(R.id.scanner_view)
        scanner = CodeScanner(this, scannerView)

        scanner.camera = CodeScanner.CAMERA_BACK
        scanner.formats = CodeScanner.ALL_FORMATS

        scanner.autoFocusMode = AutoFocusMode.SAFE
        scanner.scanMode = ScanMode.SINGLE
        scanner.isAutoFocusEnabled = true
        scanner.isFlashEnabled = false

        scanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                GlobalScope.launch(Dispatchers.IO){
                    scanQRViewModel.process( it.text)
                }
                finish()
            }
        }
        scanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            scanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        requestForCamera()
        scanner.startPreview()
    }

    override fun onPause() {
        scanner.releaseResources()
        super.onPause()
    }

    private fun requestForCamera() {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(object: PermissionListener{
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                scanner.startPreview()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(applicationContext, R.string.cam_permission, Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }
        }).check()
    }
}
