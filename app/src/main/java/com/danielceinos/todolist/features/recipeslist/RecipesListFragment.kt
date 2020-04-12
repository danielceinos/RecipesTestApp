package com.danielceinos.todolist.features.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import com.danielceinos.todolist.databinding.FragmentRecipesListBinding
import com.danielceinos.todolist.di.viewModel
import com.danielceinos.todolist.features.base.BaseFragment
import com.danielceinos.todolist.features.recipeslist.FooterAdapter.FooterState
import com.hoopcarpool.fluxy.Result


/**
 * A simple [Fragment] subclass.
 */
class RecipesListFragment : BaseFragment() {

    private val viewModel: RecipesListViewModel by viewModel()

    private lateinit var binding: FragmentRecipesListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentRecipesListBinding.inflate(inflater).also { binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recipesAdapter = RecipesAdapter({
            findNavController().navigate(RecipesListFragmentDirections.actionListFragmentToDetailFragment(it.id))
        }, {
            viewModel.markFavorite(it.id)
        })

        val footerAdapter = FooterAdapter {
            viewModel.loadRecipes()
        }

        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recipesRecyclerView.adapter = MergeAdapter(recipesAdapter, footerAdapter)

        viewModel.getLiveData().observe {
            when (it) {
                is Result.Success -> {
                    recipesAdapter.submitList(it.value.recipes)
                    footerAdapter.state = if (it.value.cached) FooterState.ERROR else FooterState.LOADED
                }
                is Result.Loading -> footerAdapter.state = FooterState.LOADING
                is Result.Failure -> footerAdapter.state = FooterState.ERROR
                is Result.Empty -> {
                }
            }
        }
    }
}
