package com.androiddevs.shoppinglisttestingyt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.adapters.ImageAdapter
import com.androiddevs.shoppinglisttestingyt.databinding.FragmentImagePickBinding
import com.androiddevs.shoppinglisttestingyt.other.Constants.GRID_SPAN_COUNT
import javax.inject.Inject

class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter
): Fragment() {

    private var _binding: FragmentImagePickBinding? = null

    private val binding get() = _binding!!

    lateinit var viewModel: ShoppingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagePickBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)

        setupRecyclerView()

        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setCurrentImageUrl(it)
        }

    }

    private fun setupRecyclerView(){
        binding.rvImages.adapter = imageAdapter
        binding.rvImages.layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}