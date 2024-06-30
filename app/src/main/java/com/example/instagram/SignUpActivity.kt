package com.example.instagram

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.instagram.Models.User
import com.example.instagram.databinding.ActivitySignupBinding
import com.example.instagram.post.PostActivity
import com.example.instagram.utils.USER_NODE
import com.example.instagram.utils.USER_PROFILE_FOLDER
import com.example.instagram.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class SignUpActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    lateinit var user:User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let {
 uploadImage(uri, USER_PROFILE_FOLDER){
    if(it==null){

    }else{
        user.image=it
        binding.dp.setImageURI(uri)

    }
}
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val text = "<font color=#ff000000>Already have an Account</font> <font color=#1E88E5>login?</font>"
        binding.login.setText(Html.fromHtml(text))
        user = User()
        if(intent.hasExtra("MODE")){
            if(intent.getIntExtra("MODE",-1)==1){
                binding.main.background = ColorDrawable(Color.WHITE)
                binding.signUpBtn.text="Update Profile"
                binding.Logout.visibility=View.VISIBLE
                binding.login.visibility=View.GONE
                binding.Logout.setOnClickListener {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, loginActivity::class.java))
                    finish()

                }


                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener {
                        user=it.toObject<User>()!!

                        if(!user.image.isNullOrEmpty()){
                            Picasso.get().load(user.image).into(binding.dp)



                        }
                        binding.textFieldName.editText?.setText(user.name)
                        binding.textFieldEmail.editText?.setText(user.email)
                        binding.textFieldP.editText?.setText(user.password)
                    }
            }
        }

        binding.signUpBtn.setOnClickListener{
            if(intent.hasExtra("MODE")){
                if(intent.getIntExtra("MODE",-1)==1) {
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid)
                        .set(user)
                        .addOnSuccessListener {

                            Toast.makeText(
                                this@SignUpActivity,
                                "Profile Update successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(
                                Intent(
                                    this@SignUpActivity,
                                    HomeActivity::class.java
                                )
                            )
                            finish()
                        }
                }
                }
            else {


                if (binding.textFieldName.editText?.text.toString().equals("") or
                    binding.textFieldEmail.editText?.text.toString().equals("") or
                    binding.textFieldP.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Please fill the all Info ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.textFieldEmail.editText?.text.toString(),
                        binding.textFieldP.editText?.text.toString()
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = User(
                                image = user.image,
                                name = binding.textFieldName.editText?.text.toString(),
                                email = binding.textFieldEmail.editText?.text.toString(),
                                password = binding.textFieldP.editText?.text.toString()

                            )
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid)
                                .set(user)
                                .addOnSuccessListener {

                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "Signup successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(
                                        Intent(
                                            this@SignUpActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                                .addOnFailureListener {

                                    Toast.makeText(
                                        this@SignUpActivity,
                                        "Failed to save user data",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {

                            Toast.makeText(
                                this@SignUpActivity,
                                task.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }

            }
        }
        binding.addimage.setOnClickListener{
            launcher.launch("image/*")
        }
       binding.login.setOnClickListener {
           startActivity(Intent(this@SignUpActivity,loginActivity::class.java))
           finish()
       }

    }

    override fun onBackPressed() {
        // Check for changes and handle back button press
        val isNameChanged = binding.textFieldName.editText?.text.toString().isNotEmpty()
        val isEmailChanged = binding.textFieldEmail.editText?.text.toString().isNotEmpty()
        val isPasswordChanged = binding.textFieldP.editText?.text.toString().isNotEmpty()
        val isImageChanged = user.image != null

        if (isNameChanged || isEmailChanged || isPasswordChanged || isImageChanged) {
            // If changes are made, show a confirmation dialog
            AlertDialog.Builder(this)
                .setMessage("Discard changes and go back?")
                .setPositiveButton("Yes") { _, _ ->
                    super.onBackPressed()
                }
                .setNegativeButton("No", null)
                .show()
        } else {
            // If no changes are made, simply go back to the previous activity
            super.onBackPressed()
        }
    }
}
