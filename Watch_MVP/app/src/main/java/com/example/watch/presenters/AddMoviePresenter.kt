package com.example.watch.presenters

import com.example.watch.AddMovieContract
import com.example.watch.models.LocalDataSource
import com.example.watch.models.Movie

class AddMoviePresenter(
    private val view: AddMovieContract.View,
    private val dataSource: LocalDataSource
) : AddMovieContract.Presenter {

    override fun onSearchMovieButtonClicked(title: String) {
        view.startSearchMovieActivity(title)
    }

    override fun onAddMovieButtonClicked(title: String, releaseDate: String, posterPath: String) {
        if (title.isEmpty()) {
            view.showToast("Movie title can't be empty")
        } else {
            val movie = Movie(title = title, releaseDate = releaseDate, posterPath = posterPath)
            dataSource.insert(movie)
            view.finishActivityWithResult()
        }
    }

    override fun onSearchMovieActivityResult(title: String, releaseDate: String, posterPath: String) {
        view.setMovieTitle(title)
        view.setMovieReleaseDate(releaseDate)
        view.setMoviePoster(posterPath)
    }
}
