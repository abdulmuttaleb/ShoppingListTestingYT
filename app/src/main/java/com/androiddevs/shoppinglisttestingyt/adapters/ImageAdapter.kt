package com.androiddevs.shoppinglisttestingyt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.shoppinglisttestingyt.databinding.ItemImageBinding
import com.bumptech.glide.RequestManager
import javax.inject.Inject

class ImageAdapter @Inject constructor(
    private val glide: RequestManager
): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){

    private val diffCallBack = object: DiffUtil.ItemCallback<String>(){
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    var images: List<String>
    get() = differ.currentList
    set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit){
        onItemClickListener = listener
    }

    inner class ImageViewHolder(private val itemImageBinding: ItemImageBinding): RecyclerView.ViewHolder(itemImageBinding.root){
        fun bind(imageUrl: String) {
            glide.load(imageUrl).into(itemImageBinding.ivShoppingImage)
            itemImageBinding.ivShoppingImage.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(imageUrl)
                }
            }
        }

    }
}