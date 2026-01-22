package com.example.movieapp.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.movieapp.data.local.AppDatabase
import com.example.movieapp.data.local.FavoriteMovieEntity
import com.example.movieapp.databinding.FragmentDetailsBinding
import com.example.movieapp.utils.Constants
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = DetailsFragmentArgs.fromBundle(requireArguments())

        binding.textTitle.text = args.title
        binding.imagePoster.load(
            Constants.IMAGE_BASE_URL + args.posterPath
        )

        binding.buttonFavorite.setOnClickListener {
            val movie = FavoriteMovieEntity(
                id = args.title.hashCode(),
                title = args.title,
                posterPath = args.posterPath
            )

            lifecycleScope.launch {
                AppDatabase
                    .getDatabase(requireContext())
                    .favoriteMovieDao()
                    .insert(movie)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

