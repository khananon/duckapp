package com.example.instagram.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.instagram.Models.Post
import com.example.instagram.adapters.my_post_rv_adapter
import com.example.instagram.databinding.FragmentMypostBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class MypostFragment : Fragment() {
    private lateinit var  binding: FragmentMypostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMypostBinding.inflate(inflater, container, false)
        var postList=ArrayList<Post>() //hold the object of type post(type means constructor type)
        var adapter=my_post_rv_adapter(requireContext(),postList) //set the post list
        binding.RV.layoutManager=StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        binding.RV.adapter=adapter
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            var tempList= arrayListOf<Post>()
            for (i in it.documents){
                var post:Post=i.toObject<Post>()!!
                tempList.add(post)
            }
            postList.addAll(tempList) //change the postlist with templist
            adapter.notifyDataSetChanged()// notify Adpapter to reflect change
        }
        return  binding.root
    }

    companion object {

                }
            }
