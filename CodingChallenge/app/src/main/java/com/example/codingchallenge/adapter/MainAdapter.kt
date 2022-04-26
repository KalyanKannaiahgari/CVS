package com.example.codingchallenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.codingchallenge.databinding.AdapterImageBinding
import com.example.codingchallenge.model.Items

class MainAdapter(callback: AdapterCallBack?) : RecyclerView.Adapter<MainViewHolder>() {

    var images = mutableListOf<Items>()
    var mAdapterCallback: AdapterCallBack? = callback


    fun setImageList(images: List<Items>) {
        this.images = images.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = AdapterImageBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val image = images[position]
        holder.binding.name.text = image.title
        Glide.with(holder.itemView.context).load(image.media?.m).into(holder.binding.imageview)
        holder.binding.cardView.setOnClickListener {
            mAdapterCallback?.itemOnClick(position)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}

class MainViewHolder(val binding: AdapterImageBinding) : RecyclerView.ViewHolder(binding.root) {

}
