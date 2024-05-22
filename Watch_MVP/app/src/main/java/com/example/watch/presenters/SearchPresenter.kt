package com.example.watch.presenters

import com.example.watch.SearchContract
import com.example.watch.models.RemoteDataSource
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

// Presenter
class SearchPresenter(
    private val view: SearchContract.View,
    private val dataSource: RemoteDataSource,
    private val ioScheduler: Scheduler,
    private val mainScheduler: Scheduler
) : SearchContract.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun searchMovies(query: String) {
        view.showLoading()
        val disposable = dataSource.searchResultsObservable(query)
            .subscribeOn(ioScheduler)
            .observeOn(mainScheduler)
            .subscribe(
                { response ->
                    view.hideLoading()
                    view.displaySearchResults(response)
                },
                { error ->
                    view.hideLoading()
                    view.displayError("Error fetching Movie Data: ${error.message}")
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun onStop() {
        compositeDisposable.clear()
    }
}