package com.example.codingchallenge.repository

class MainRepository constructor(private val retrofitService: RetrofitService) {

    fun getAllImages() = retrofitService.getAllImages()
}