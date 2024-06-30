package com.example.instagram.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.instagram.R
import com.example.instagram.databinding.FragmentAddBinding
import com.example.instagram.databinding.FragmentProfileBinding
import com.example.instagram.post.PostActivity
import com.example.instagram.post.ReelActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddFragment : BottomSheetDialogFragment() {
    private lateinit var  binding: FragmentAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=FragmentAddBinding.inflate(inflater, container, false)
        binding.Post.setOnClickListener {
            startActivity(Intent(requireContext(),PostActivity::class.java))
            activity?.finish()
        }
        binding.Reel.setOnClickListener {
            startActivity(Intent(requireContext(), ReelActivity::class.java))
        }
        return binding.root
    }

    companion object {

    }
}