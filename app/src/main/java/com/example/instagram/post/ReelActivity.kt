package com.example.instagram.post

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instagram.HomeActivity
import com.example.instagram.Models.Reel
import com.example.instagram.databinding.ActivityReelBinding
import com.example.instagram.utils.REEL
import com.example.instagram.utils.REEL_FOLDER
import com.example.instagram.utils.uploadVideo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReelActivity : AppCompatActivity() {
    val binding by lazy {
       ActivityReelBinding.inflate(layoutInflater)
    }
    private lateinit var  VideoUrl:String
    lateinit var progressDialog:ProgressDialog
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadVideo(uri, REEL_FOLDER,progressDialog) { url ->
                if (url != null) {

                    VideoUrl = url
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        progressDialog=ProgressDialog(this)
        binding.SelectReel.setOnClickListener {
            launcher.launch("video/*")
        }

        binding.buttonCancle.setOnClickListener {
            startActivity(Intent(this@ReelActivity, HomeActivity::class.java))
            finish()
        }
        binding.buttonPost.setOnClickListener {
            val reel = Reel(VideoUrl!!, binding.Caption.editableText.toString())
            Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ REEL).document().set(reel)
                    .addOnSuccessListener {
                        startActivity(Intent(this@ReelActivity, HomeActivity::class.java))
                        finish()
                    }
            }
        }

        }
    }
