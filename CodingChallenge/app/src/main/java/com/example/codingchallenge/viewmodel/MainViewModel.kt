package com.example.codingchallenge.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.codingchallenge.model.ImageData
import com.example.codingchallenge.model.Items
import com.example.codingchallenge.repository.MainRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel constructor(private val repository: MainRepository) : ViewModel() {

    val imageList = MutableLiveData<List<Items>>()
    val errorMessage = MutableLiveData<String>()

    fun getAllImages() {
        val response = repository.getAllImages()
        response.enqueue(object : Callback<ImageData> {
            override fun onResponse(call: Call<ImageData>, response: Response<ImageData>) {
                imageList.postValue(response.body()?.items)
            }

            override fun onFailure(call: Call<ImageData>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }
}