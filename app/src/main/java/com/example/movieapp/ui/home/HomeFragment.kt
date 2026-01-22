package com.example.movieapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.data.remote.RetrofitInstance
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.utils.Constants
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var adapter: MovieAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.buttonFavorites.setOnClickListener {
            val action =
                HomeFragmentDirections
                    .actionHomeFragmentToFavoritesFragment()
            findNavController().navigate(action)
        }


        loadMovies()
    }

    private fun loadMovies() {

        // 🔄 loading start
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val response =
                RetrofitInstance.api.getPopularMovies(Constants.API_KEY)

            adapter = MovieAdapter(response.results)
            binding.recyclerView.adapter = adapter

            // ✅ loading end
            binding.progressBar.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

