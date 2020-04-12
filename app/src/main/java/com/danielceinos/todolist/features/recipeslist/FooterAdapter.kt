package com.danielceinos.todolist.features.recipeslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danielceinos.todolist.databinding.RecipeErrorRowBinding
import com.danielceinos.todolist.databinding.RecipeLoaderRowBinding
import com.danielceinos.todolist.features.recipeslist.FooterAdapter.ItemViewHolder
import com.danielceinos.todolist.features.recipeslist.FooterAdapter.ItemViewHolder.ErrorItemViewHolder
import com.danielceinos.todolist.features.recipeslist.FooterAdapter.ItemViewHolder.LoaderItemViewHolder

class FooterAdapter(val listener: () -> Unit) : RecyclerView.Adapter<ItemViewHolder>() {

    var state: FooterState = FooterState.LOADED
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        when (state) {
            FooterState.LOADING ->
                RecipeLoaderRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .run { LoaderItemViewHolder(this) }
            FooterState.ERROR ->
                RecipeErrorRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .run { ErrorItemViewHolder(this) }
            FooterState.LOADED ->
                RecipeLoaderRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                    .run { LoaderItemViewHolder(this) }
        }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        when (holder) {
            is LoaderItemViewHolder -> {
                holder.bind(state == FooterState.LOADING)
                listener()
            }
            is ErrorItemViewHolder -> {
                holder.bind(listener)
            }
        }

    }

    override fun getItemViewType(position: Int): Int = state.ordinal

    override fun getItemCount(): Int = 1

    sealed class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {


        class LoaderItemViewHolder(private val binding: RecipeLoaderRowBinding) : ItemViewHolder(binding.root) {
            fun bind(loading: Boolean) {
                if (loading) binding.root.visibility = View.VISIBLE else binding.root.visibility = View.INVISIBLE
            }
        }

        class ErrorItemViewHolder(private val binding: RecipeErrorRowBinding) : ItemViewHolder(binding.root) {
            fun bind(listener: () -> Unit) {
                binding.recipeErrorRowRetry.setOnClickListener { listener() }
            }
        }
    }


    enum class FooterState {
        LOADING, ERROR, LOADED
    }
}
