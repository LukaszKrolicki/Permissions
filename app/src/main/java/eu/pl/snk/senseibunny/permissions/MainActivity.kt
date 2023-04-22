package eu.pl.snk.senseibunny.permissions

import android.app.Instrumentation.ActivityResult
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.os.Message
import android.service.quicksettings.Tile
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private val cameraResultLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){
        isGranted->
        if(isGranted){
            Toast.makeText(this,"permission granted for camera", Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this,"Permission denied for camera", Toast.LENGTH_LONG).show()
        }
    }

    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
            permissions->
        permissions.entries.forEach{
            val permissionName = it.key
            val isGranted=it.value

            if(isGranted){
                if(permissionName==Manifest.permission.ACCESS_FINE_LOCATION){
                    Toast.makeText(this, "Permissions granted for location",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this, "Permissions granted for Camera",Toast.LENGTH_LONG).show()
                }
            }
            else{
                if(permissionName==Manifest.permission.ACCESS_FINE_LOCATION){
                    Toast.makeText(this, "Permissions denied for location",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this, "Permissions denied for Camera",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //Show dialog for displaying why the app needs permission
    //Only shown if user has denied permission request previously
    private fun showRationaleDialog(tile: String, message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Cancel"){dialog,_->dialog.dismiss()}
        builder.create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCamera : Button=findViewById(R.id.per_btn)

        btnCamera.setOnClickListener{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) //Checking version
            {
                showRationaleDialog("App requires camera access", "Camera cannot because Camera access is denied")
            }
            else{
                cameraAndLocationResultLauncher.launch(arrayOf(Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION))
            }
        }

    }
}