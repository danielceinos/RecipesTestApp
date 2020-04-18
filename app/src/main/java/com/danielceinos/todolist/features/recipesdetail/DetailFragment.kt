package com.danielceinos.todolist.features.recipesdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.danielceinos.todolist.databinding.FragmentDetailBinding
import com.danielceinos.todolist.databinding.IngredientChipBinding
import com.danielceinos.todolist.di.viewModel
import com.danielceinos.todolist.features.base.BaseFragment
import com.google.android.material.chip.Chip
import com.hoopcarpool.fluxy.Result


class DetailFragment : BaseFragment() {

    private val args: DetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: RecipeDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setup(args)
        setToolbarTitle(args.recipeTitle)

        viewModel.getLiveData().observe {
            when (it) {
                is Result.Success -> renderSuccessState(it.value)
                is Result.Loading -> {
                }
                is Result.Failure -> {
                }
                is Result.Empty -> {
                }
            }
        }

        binding.recipeDetailFavoriteButton.setOnClickListener {
            viewModel.markFavoriteRecipe()
        }
    }

    private fun renderSuccessState(viewData: RecipeDetailViewModel.RecipeDetailViewState) {
        Glide.with(this).load(viewData.imageUrl).into(binding.recipeDetailImage)
        setToolbarTitle(viewData.title)
        binding.recipeDetailTitle.text = viewData.title
        binding.recipeDetailFavoriteButton.setImageDrawable(
            if (viewData.favorite) {
                resources.getDrawable(android.R.drawable.btn_star_big_on)
            } else {
                resources.getDrawable(android.R.drawable.btn_star_big_off)
            }
        )

        binding.ingredientsChipGroup.removeAllViews()
        viewData.ingredients.forEach {
            val chip = IngredientChipBinding.inflate(layoutInflater).root as Chip
            chip.text = it
            binding.ingredientsChipGroup.addView(chip)
        }
    }
}
