package com.danielceinos.todolist.features.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.danielceinos.todolist.databinding.RecipeRowBinding
import com.danielceinos.todolist.features.recipeslist.RecipesListViewState.Recipe

class RecipesAdapter(
    private val onItemClickListener: (Recipe) -> Unit,
    private val onItemFavClickListener: (Recipe) -> Unit
) :
    ListAdapter<Recipe, RecipesAdapter.ItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        RecipeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run { ItemViewHolder(this) }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: RecipeRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recipe) {
            with(item) {
                binding.recipeRowTitle.text = title
                binding.recipeRowFavCheckbox.isChecked = fav
                binding.recipeRowCard.setOnClickListener {
                    onItemClickListener(this)
                }
            }

            //TODO: use check listener
            binding.recipeRowFavCheckbox.setOnClickListener {
                onItemFavClickListener(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem == newItem
    }
}