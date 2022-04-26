package com.example.codingchallenge.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.codingchallenge.R
import com.example.codingchallenge.databinding.DetailActivityBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: DetailActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent != null) {
            val imageUrl = intent.getStringExtra("imageUrl")
            val title = intent.getStringExtra("title")
            val publishedOn = intent.getStringExtra("publishedOn")
            val author = intent.getStringExtra("author")
            Glide.with(this).load(imageUrl.toString()).into(binding.imageview)
            binding.title.text = title
            binding.publishedOn.text = publishedOn.toString()
            binding.author.text = author.toString()

        }
    }
}