package com.example.movieapp.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.data.local.AppDatabase
import com.example.movieapp.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoritesAdapter { movieId ->
            deleteMovie(movieId)
        }


        binding.recyclerViewFavorites.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = adapter

        observeFavorites()
    }

    private fun observeFavorites() {
        lifecycleScope.launch {
            AppDatabase
                .getDatabase(requireContext())
                .favoriteMovieDao()
                .getAllFavorites()
                .collect { favorites ->
                    adapter.submitList(favorites)

                    binding.textEmpty.visibility =
                        if (favorites.isEmpty()) View.VISIBLE else View.GONE
                }
        }
    }

    private fun deleteMovie(movieId: Int) {
        lifecycleScope.launch {
            AppDatabase
                .getDatabase(requireContext())
                .favoriteMovieDao()
                .deleteById(movieId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
