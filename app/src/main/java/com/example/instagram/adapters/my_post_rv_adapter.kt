package com.example.instagram.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.Models.Post
import com.example.instagram.databinding.MyPostRvDesignBinding
import com.squareup.picasso.Picasso

class my_post_rv_adapter(var context: Context, var postList: ArrayList<Post> ,): RecyclerView.Adapter<my_post_rv_adapter.ViewHolder>(){
    inner class ViewHolder (var binding: MyPostRvDesignBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      var binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(postList.get(position).postUrl).into(holder.binding.PostImage)
    }
}