package com.danielceinos.todolist.features.recipesdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.danielceinos.todolist.databinding.FragmentDetailBinding
import com.danielceinos.todolist.di.viewModel
import com.danielceinos.todolist.features.base.BaseFragment
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

        viewModel.getLiveData().observe {
            when (it) {
                is Result.Success -> {
                    Glide.with(this).load(it.value.imageUrl).into(binding.recipeDetailImage)
                    binding.recipeDetailTitle.text = it.value.title
                    binding.recipeDetailFavoriteButton.setImageDrawable(
                        if (it.value.favorite) {
                            resources.getDrawable(android.R.drawable.btn_star_big_on)
                        } else {
                            resources.getDrawable(android.R.drawable.btn_star_big_off)
                        }
                    )
                }
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
}
