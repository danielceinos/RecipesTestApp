package com.danielceinos.todolist.features.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danielceinos.todolist.databinding.RecipeErrorRowBinding

class ErrorRecipesAdapter(private val listener: () -> Unit) : RecyclerView.Adapter<ErrorRecipesAdapter.ItemViewHolder>() {

    var error: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        RecipeErrorRowBinding.inflate(LayoutInflater.from(parent.context), parent, false).run { ItemViewHolder(this) }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listener)
    }

    inner class ItemViewHolder(private val binding: RecipeErrorRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: () -> Unit) {
            binding.recipeErrorRowRetry.setOnClickListener { listener() }
        }
    }

    override fun getItemCount(): Int = if (error) 1 else 0
}
