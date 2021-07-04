package com.androiddevs.shoppinglisttestingyt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.adapters.ShoppingItemAdapter
import com.androiddevs.shoppinglisttestingyt.databinding.FragmentShoppingBinding
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class ShoppingFragment @Inject constructor(
    val shoppingItemAdapter: ShoppingItemAdapter,
    var viewModel: ShoppingViewModel? = null
) : Fragment() {

    private var _binding: FragmentShoppingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subscribeToObservers()
        setupRecyclerView()

        binding.fabAddShoppingItem.setOnClickListener {
            val action = ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingFragment()
            findNavController().navigate(action)
        }

    }

    private val itemTouchCallback = object: ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = shoppingItemAdapter.shoppingItems[pos]
            viewModel?.deleteShoppingItem(item)
            Snackbar.make(
                requireView(),
                "Successfully Deleted Item!",
                Snackbar.LENGTH_LONG
            ).apply {
                setAction("Undo"){
                    viewModel?.insertShoppingItemIntoDb(item)
                }
            }.show()
        }

    }

    private fun subscribeToObservers(){
        viewModel?.shoppingItems?.observe(viewLifecycleOwner, {
            shoppingItemAdapter.shoppingItems = it
        })

        viewModel?.totalPrice?.observe(viewLifecycleOwner, {
            val price = it ?: 0f
            val priceText = "Total Price: $price€"
        })
    }

    private fun setupRecyclerView(){
        binding.rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}