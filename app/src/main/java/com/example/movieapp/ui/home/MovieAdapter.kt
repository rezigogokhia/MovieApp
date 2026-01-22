package com.example.movieapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movieapp.data.model.Movie
import com.example.movieapp.databinding.ItemMovieBinding
import com.example.movieapp.utils.Constants
import kotlinx.coroutines.*
import com.example.movieapp.data.local.AppDatabase
import com.example.movieapp.data.local.FavoriteMovieEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MovieAdapter(
    private val movies: List<Movie>
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            binding.textTitle.text = movie.title
            binding.imagePoster.load(
                Constants.IMAGE_BASE_URL + movie.poster_path
            )

            val movieId = movie.title.hashCode()
            val dao = AppDatabase
                .getDatabase(binding.root.context)
                .favoriteMovieDao()

            // ⭐ initial state
            CoroutineScope(Dispatchers.Main).launch {
                val isFav = dao.isFavorite(movieId)
                updateIcon(isFav)
            }

            // ⭐ toggle
            binding.imageFavorite.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val isFav = dao.isFavorite(movieId)

                    if (isFav) {
                        dao.deleteById(movieId)
                        updateIcon(false)
                    } else {
                        dao.insert(
                            FavoriteMovieEntity(
                                id = movieId,
                                title = movie.title,
                                posterPath = movie.poster_path ?: ""
                            )
                        )
                        updateIcon(true)
                    }
                }
            }

            // Details navigation (რჩება)
            binding.root.setOnClickListener {
                val action =
                    HomeFragmentDirections
                        .actionHomeFragmentToDetailsFragment(
                            title = movie.title,
                            posterPath = movie.poster_path ?: ""
                        )
                it.findNavController().navigate(action)
            }
        }

        private fun updateIcon(isFavorite: Boolean) {
            binding.imageFavorite.setImageResource(
                if (isFavorite)
                    android.R.drawable.btn_star_big_on
                else
                    android.R.drawable.btn_star_big_off
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size
}




