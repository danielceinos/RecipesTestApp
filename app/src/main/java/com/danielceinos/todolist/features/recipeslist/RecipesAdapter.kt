package com.danielceinos.todolist.features.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.danielceinos.todolist.databinding.RecipeRowBinding
import com.danielceinos.todolist.features.recipeslist.RecipesListViewState.Recipe
import timber.log.Timber

class RecipesAdapter(
    private val onItemClickListener: (Recipe) -> Unit
) :
    ListAdapter<Recipe, RecipesAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        RecipeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run { ItemViewHolder(this) }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    inner class ItemViewHolder(private val binding: RecipeRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recipe, onItemClickListener: (Recipe) -> Unit) {
            with(item) {
                binding.recipeRowTitle.text = title
                binding.recipeRowFavCheckbox.isChecked = fav
            }

            binding.recipeRowFavCheckbox.setOnClickListener {
                onItemClickListener(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem == newItem
    }
}