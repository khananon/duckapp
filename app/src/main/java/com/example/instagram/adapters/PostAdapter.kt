package com.example.instagram.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.Models.Post
import com.example.instagram.Models.User
import com.example.instagram.R
import com.example.instagram.databinding.PostBinding
import com.example.instagram.utils.USER_NODE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class PostAdapter(var context : Context,  var postList: List<Post>): RecyclerView.Adapter<PostAdapter.MyHolder>(){
    inner class MyHolder( var binding : PostBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding=PostBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyHolder(binding )
    }

    override fun getItemCount(): Int {
       return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Firebase.firestore.collection(USER_NODE).document(postList.get(position).uid).get()
            .addOnSuccessListener {
                var user = it.toObject<User>()
                Glide.with(context).load(user!!.image).placeholder(R.drawable.user)
                holder.binding.name.text=user.name
            }
        Glide.with(context).load(postList.get(position).postUrl).placeholder(R.drawable.loading)
        holder.binding.time.text=postList.get(position).time
        holder.binding.caption.text=postList.get(position).caption
    }
}