package com.danielceinos.todolist.features.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.danielceinos.todolist.databinding.FragmentRecipesListBinding
import com.danielceinos.todolist.di.viewModel
import com.danielceinos.todolist.features.base.BaseFragment
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


        val adapter = RecipesAdapter({

        }, {
            viewModel.loadRecipes()
        })

        binding.recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recipesRecyclerView.adapter = adapter


        viewModel.getLiveData().observe {
            when (it) {
                is Result.Success -> adapter.submitList(it.value.recipes)
                is Result.Loading -> Toast.makeText(requireContext(), "loading", Toast.LENGTH_SHORT).show()
                is Result.Failure -> Toast.makeText(requireContext(), "failure", Toast.LENGTH_SHORT).show()
                is Result.Empty -> Toast.makeText(requireContext(), "empty", Toast.LENGTH_SHORT).show()
            }

        }

        viewModel.loadRecipes()
    }
}


