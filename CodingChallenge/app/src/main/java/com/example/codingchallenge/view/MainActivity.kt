package com.example.codingchallenge.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.codingchallenge.R
import com.example.codingchallenge.adapter.AdapterCallBack
import com.example.codingchallenge.adapter.MainAdapter
import com.example.codingchallenge.databinding.ActivityMainBinding
import com.example.codingchallenge.model.Items
import com.example.codingchallenge.repository.MainRepository
import com.example.codingchallenge.repository.MyViewModelFactory
import com.example.codingchallenge.repository.RetrofitService
import com.example.codingchallenge.viewmodel.MainViewModel
import java.util.*


class MainActivity : AppCompatActivity(), AdapterCallBack {
    private lateinit var binding: ActivityMainBinding
    private var imageList: List<Items>? = null
    private var filteredList: List<Items>? = ArrayList()
    private lateinit var viewModel: MainViewModel
    private val adapter = MainAdapter(this)
    private val retrofitService = RetrofitService.getInstance()
    private var dropDownList = ArrayList<String>()
    var headerText: TextView? = null
    var head: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, MyViewModelFactory(MainRepository(retrofitService))).get(
                MainViewModel::class.java
            )

        //Set Adapter for recyclerview
        binding.recyclerview.adapter = adapter

        setupHeader()
        setupDropDown()

        // Search button click listener
        handleSearchButtonClicks()

        //get imageList livedata from viewModel by observer
        liveDataObserve()

    }

    private fun handleSearchButtonClicks() {
        binding.searchButton.setOnClickListener {
            if (binding.searchView.text.toString().isNotEmpty()) {
                setupDropDown()
                initApiAndUpdateUi()
            } else {
                showAlert(getString(R.string.please_enter_some_text))
            }
        }
        binding.searchView.setOnEditorActionListener(OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.searchView.text.toString().isNotEmpty()) {
                    setupDropDown()
                    initApiAndUpdateUi()
                } else {
                    showAlert(getString(R.string.please_enter_some_text))
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun liveDataObserve() {
        viewModel.imageList.observe(this) {
            imageList = it
            //filter the result from search term
            filter(binding.searchView.text.toString())
            binding.progressBar.visibility = View.GONE
        }

        //get error message livedata from viewModel by observer
        viewModel.errorMessage.observe(this) {
            binding.progressBar.visibility = View.GONE
            showAlert(getString(R.string.something_went_wrong))

        }
    }

    private fun setupHeader() {
        head = layoutInflater.inflate(R.layout.header_layout, null) as View
        headerText = head?.findViewById(R.id.titleLabel)
        headerText?.text = getString(R.string.no_recent_searches)
        binding.dropDownListView.addHeaderView(head)
    }

    private fun setupDropDown() {
        if (dropDownList.isEmpty()) {
            if (binding.searchView.text.toString().isNotEmpty()) {
                dropDownList.add(binding.searchView.text.toString())
            }
        } else {
            if (!dropDownList.contains(binding.searchView.text.toString()) && dropDownList.size <= 4) {
                dropDownList.add(binding.searchView.text.toString())
            } else if (!dropDownList.contains(binding.searchView.text.toString()) && dropDownList.size >= 4) {
                dropDownList.removeAt(0)
                dropDownList.add(binding.searchView.text.toString())
            }
        }
        //Set DropDownList
        val mArrayAdapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                dropDownList
            )
        binding.dropDownListView.adapter = mArrayAdapter
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.searchView.text?.isEmpty() == true) {
                    binding.dropDownListView.visibility = View.VISIBLE
                    binding.recyclerview.visibility = View.GONE
                    if (dropDownList.size > 0) {
                        binding.dropDownListView.removeHeaderView(head)
                        headerText?.text = getString(R.string.recent_searches)
                        binding.dropDownListView.addHeaderView(head)
                    }
                }
                mArrayAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.dropDownListView.setOnItemClickListener { _, _, position, _ ->
            if (position != 0) {
                val element = dropDownList[position - 1] // The item that was clicked
                binding.searchView.setText(element)
                binding.dropDownListView.visibility = View.GONE
            }
        }
    }

    private fun initApiAndUpdateUi() {
        binding.dropDownListView.visibility = View.GONE
        filteredList = emptyList()
        filteredList?.let { adapter.setImageList(it) }
        binding.progressBar.visibility = View.VISIBLE
        hideKeyboard()
        viewModel.getAllImages()
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage(message)
        builder.setPositiveButton("Ok") { _, _ ->

        }
        builder.show()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        filteredList = if (text.isEmpty()) {
            emptyList()
        } else {
            //looping through existing elements
            imageList?.filter {
                it.tags?.toLowerCase(
                    Locale.ROOT
                )?.contains(text.toLowerCase(Locale.ROOT)) == true
            }
        }
        //calling a method of the adapter class and passing the filtered list
        if (filteredList?.isEmpty() == true) {
            showAlert(getString(R.string.no_result))
        }
        binding.dropDownListView.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
        filteredList?.let { adapter.setImageList(it) }

    }

    override fun itemOnClick(position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("imageUrl", filteredList?.get(position)?.media?.m)
        intent.putExtra("title", filteredList?.get(position)?.title)
        intent.putExtra("publishedOn", filteredList?.get(position)?.published)
        intent.putExtra("author", filteredList?.get(position)?.author)
        startActivity(intent)
    }
}