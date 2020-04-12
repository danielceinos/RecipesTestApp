package com.danielceinos.todolist.features.recipeslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danielceinos.todolist.databinding.RecipeLoaderRowBinding
import com.danielceinos.todolist.features.recipeslist.LoadRecipesAdapter.ItemViewHolder

class LoadRecipesAdapter(val listener: () -> Unit) : RecyclerView.Adapter<ItemViewHolder>() {

    var loading: Boolean = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var canLoadMore: Boolean = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        RecipeLoaderRowBinding.inflate(LayoutInflater.from(parent.context), parent, false).run { ItemViewHolder(this) }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(loading)
        listener()
    }

    inner class ItemViewHolder(private val binding: RecipeLoaderRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loading: Boolean) {
            if (loading) binding.root.visibility = View.VISIBLE else binding.root.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int = if (canLoadMore) 1 else 0
}
