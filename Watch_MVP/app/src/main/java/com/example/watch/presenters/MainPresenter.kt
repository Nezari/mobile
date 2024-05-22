package com.example.watch.presenters

import com.example.watch.MainContract
import com.example.watch.models.LocalDataSource
import com.example.watch.models.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenter(
    private val view: MainContract.View,
    private val dataSource: LocalDataSource
) : MainContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun getMovies() {
        val disposable = dataSource.allMovies
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ movieList ->
                view.displayMovies(movieList)
            }, { error ->
                view.showError("Error fetching movie list: ${error.message}")
            })

        compositeDisposable.add(disposable)
    }

    override fun deleteMovie(movie: Movie) {
        dataSource.delete(movie)
    }
}
