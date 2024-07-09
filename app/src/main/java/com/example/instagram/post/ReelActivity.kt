package com.example.instagram.post

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.instagram.HomeActivity
import com.example.instagram.Models.Reel
import com.example.instagram.Models.User
import com.example.instagram.R
import com.example.instagram.databinding.ActivityReelBinding
import com.example.instagram.utils.REEL
import com.example.instagram.utils.REEL_FOLDER
import com.example.instagram.utils.USER_NODE
import com.example.instagram.utils.uploadVideo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
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
                    binding.SelectReel.setText("Selected")
                    binding.SelectReel.setTextColor(Color.parseColor("#000000"))
                    binding.SelectReel.setBackgroundColor(ContextCompat.getColor(this, R.color.Yellow))
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
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    var user: User = it.toObject<User>()!!
                    val reel: Reel =
                        Reel(VideoUrl!!, binding.Caption.editableText.toString(), user.image!!)
                    Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL)
                            .document().set(reel)
                            .addOnSuccessListener {
                                startActivity(Intent(this@ReelActivity, HomeActivity::class.java))
                                finish()
                            }
                    }
                }

        }}}

