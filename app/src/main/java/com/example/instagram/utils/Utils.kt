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
fun uploadVideo(
    uri: Uri,
    folderName: String,
    progressDialog: ProgressDialog,
    callback: (String?) -> Unit
) {
    progressDialog.setTitle("Uploading . . .")
    progressDialog.show()

    val storageReference = FirebaseStorage.getInstance().getReference(folderName)
        .child(UUID.randomUUID().toString())

    storageReference.putFile(uri)
        .addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                callback(uri.toString())
                progressDialog.dismiss()
            }.addOnFailureListener { exception ->
                callback(null)
                progressDialog.dismiss()
                // Log or handle the exception
            }
        }
        .addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
            progressDialog.setMessage("Uploaded ${progress.toInt()}%")
        }
        .addOnFailureListener { exception ->
            callback(null)
            progressDialog.dismiss()
            // Log or handle the exception
        }
}
