package com.example.watch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.watch.models.LocalDataSource
import com.example.watch.network.RetrofitClient.TMDB_IMAGEURL
import com.example.watch.presenters.AddMoviePresenter
import com.squareup.picasso.Picasso

class AddMovieActivity : AppCompatActivity(), AddMovieContract.View {

    private lateinit var presenter: AddMoviePresenter

    private lateinit var titleEditText: EditText
    private lateinit var releaseDateEditText: EditText
    private lateinit var movieImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        titleEditText = findViewById(R.id.movie_title)
        releaseDateEditText = findViewById(R.id.movie_release_date)
        movieImageView = findViewById(R.id.movie_imageview)

        presenter = AddMoviePresenter(this, LocalDataSource(application))
    }

    fun goToSearchMovieActivity(v: View) {
        val title = titleEditText.text.toString()
        presenter.onSearchMovieButtonClicked(title)
    }

    fun onClickAddMovie(v: View) {
        val title = titleEditText.text.toString()
        val releaseDate = releaseDateEditText.text.toString()
        val posterPath = movieImageView.tag?.toString() ?: ""

        presenter.onAddMovieButtonClicked(title, releaseDate, posterPath)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SEARCH_MOVIE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(SearchActivity.EXTRA_TITLE) ?: ""
            val releaseDate = data?.getStringExtra(SearchActivity.EXTRA_RELEASE_DATE) ?: ""
            val posterPath = data?.getStringExtra(SearchActivity.EXTRA_POSTER_PATH) ?: ""

            presenter.onSearchMovieActivityResult(title, releaseDate, posterPath)
        }
    }

    // Реализация методов интерфейса View:

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun setMovieTitle(title: String) {
        titleEditText.setText(title)
    }

    override fun setMovieReleaseDate(releaseDate: String) {
        releaseDateEditText.setText(releaseDate)
    }

    override fun setMoviePoster(posterPath: String) {
        movieImageView.tag = posterPath
        Picasso.get().load(TMDB_IMAGEURL + posterPath).into(movieImageView)
    }

    override fun finishActivityWithResult() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    @Suppress("DEPRECATION")
    override fun startSearchMovieActivity(query: String) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra(SearchActivity.SEARCH_QUERY, query)
        startActivityForResult(intent, SEARCH_MOVIE_ACTIVITY_REQUEST_CODE)
    }

    companion object {
        private const val SEARCH_MOVIE_ACTIVITY_REQUEST_CODE = 2
    }
}

interface AddMovieContract {

    interface View {
        fun showToast(message: String)
        fun setMovieTitle(title: String)
        fun setMovieReleaseDate(releaseDate: String)
        fun setMoviePoster(posterPath: String)
        fun finishActivityWithResult()
        fun startSearchMovieActivity(query: String)
    }

    interface Presenter {
        fun onSearchMovieButtonClicked(title: String)
        fun onAddMovieButtonClicked(title: String, releaseDate: String, posterPath: String)
        fun onSearchMovieActivityResult(title: String, releaseDate: String, posterPath: String)
    }
}
