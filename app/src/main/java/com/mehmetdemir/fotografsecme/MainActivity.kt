package com.mehmetdemir.fotografsecme

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var secilengorsel:Uri
    lateinit var bitmap:Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fotograf.setOnClickListener {
            if(ContextCompat.checkSelfPermission(
                    this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1,)
            }
            else{
                var galeri_gecis=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeri_gecis,2)
            }
        }
    }
    //izin alındı mı kontrolu
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==1){
            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    //izin alındığında yapılacaklar
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==2 && resultCode==Activity.RESULT_OK && data!=null ){
            secilengorsel= data.data!!
            if(secilengorsel!=null){
                if(Build.VERSION.SDK_INT >=28){
                    val source=ImageDecoder.createSource(this.contentResolver,secilengorsel)
                    bitmap=ImageDecoder.decodeBitmap(source)
                    fotograf.setImageBitmap(bitmap)
                }
                else{
                    bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,secilengorsel)
                    fotograf.setImageBitmap(bitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}