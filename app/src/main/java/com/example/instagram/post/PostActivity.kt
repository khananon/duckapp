package com.example.instagram.post

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.instagram.HomeActivity
import com.example.instagram.Models.Post
import com.example.instagram.R
import com.example.instagram.databinding.ActivityPostBinding
import com.example.instagram.utils.POST
import com.example.instagram.utils.POST_FOLDER
import com.example.instagram.utils.REEL
import com.example.instagram.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl: String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POST_FOLDER) { url ->
                if (url != null) {
                    binding.imageView1.setImageURI(uri)
                    imageUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val drawable = ContextCompat.getDrawable(this, R.drawable.baseline_arrow_back_24)
        binding.toolbar.navigationIcon = drawable

        binding.toolbar.setNavigationOnClickListener {
            // Handle the back button click here
            val intent = Intent(this@PostActivity, HomeActivity::class.java)
            startActivity(intent)
            finish() // This will close the current activity and navigate back to the previous one
        }

        binding.imageView1.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.buttonCancle.setOnClickListener {
            startActivity(Intent(this@PostActivity, HomeActivity::class.java))
            finish()
        }

        fun onBackPressed() {
            startActivity(Intent(this@PostActivity, HomeActivity::class.java))
            finish()
        }
        binding.buttonPost.setOnClickListener {
            val post = Post(imageUrl!!, binding.Caption.editableText.toString())
            Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post)
                    .addOnSuccessListener {
                        startActivity(Intent(this@PostActivity, HomeActivity::class.java))
                        finish()
                    }
            }
        }
    }
}
