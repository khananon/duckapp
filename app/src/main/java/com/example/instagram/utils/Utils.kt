package com.example.instagram.utils

import android.app.ProgressDialog
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImage(uri: Uri, folderName: String,callback:(String?)->Unit) {
    var imagrUrl: String? =null
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imagrUrl=it.toString()
                callback(imagrUrl)
            }
        }

}
fun uploadVideo(uri: Uri, folderName: String,progressDialog: ProgressDialog,callback:(String?)->Unit) {
    var imagrUrl: String? =null
  progressDialog.setTitle("Uploading . . .")
    progressDialog.show()
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imagrUrl=it.toString()
                callback(imagrUrl)
            }

        }.addOnProgressListener {
            val uploadValue :Long =it.bytesTransferred/it.totalByteCount
            progressDialog.setMessage("upload $uploadValue %")
        }

}