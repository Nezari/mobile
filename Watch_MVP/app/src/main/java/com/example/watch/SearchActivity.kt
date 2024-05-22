package com.example.watch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watch.adapters.SearchAdapter
import com.example.watch.models.RemoteDataSource
import com.example.watch.models.TmdbResponse
import com.example.watch.presenters.SearchPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchActivity : AppCompatActivity(), SearchContract.View {

    private lateinit var presenter: SearchPresenter

    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var adapter: SearchAdapter
    private lateinit var noMoviesTextView: TextView
    private lateinit var progressBar: ProgressBar
    private var query = ""

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movie)

        searchResultsRecyclerView = findViewById(R.id.search_results_recyclerview)
        noMoviesTextView = findViewById(R.id.no_movies_textview)
        progressBar = findViewById(R.id.progress_bar)

        val i = intent
        query = i.getStringExtra(SEARCH_QUERY) ?: ""

        setupViews()

        // Инициализируем Presenter
        presenter = SearchPresenter(this, RemoteDataSource(), Schedulers.io(), AndroidSchedulers.mainThread())
    }

    override fun onStart() {
        super.onStart()
        presenter.searchMovies(query)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
        presenter.onStop()
    }

    private fun setupViews() {
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    // Реализация методов интерфейса View:

    override fun showLoading() {
        progressBar.visibility = VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = INVISIBLE
    }

    override fun displaySearchResults(tmdbResponse: TmdbResponse) {
        if (tmdbResponse.totalResults == null || tmdbResponse.totalResults == 0) {
            searchResultsRecyclerView.visibility = INVISIBLE
            noMoviesTextView.visibility = VISIBLE
        } else {
            adapter = SearchAdapter(tmdbResponse.results ?: arrayListOf(), this, itemListener)
            searchResultsRecyclerView.adapter = adapter

            searchResultsRecyclerView.visibility = VISIBLE
            noMoviesTextView.visibility = INVISIBLE
        }
    }

    override fun displayError(string: String) {
        showToast(string)
    }

    private fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val SEARCH_QUERY = "searchQuery"
        const val EXTRA_TITLE = "SearchActivity.TITLE_REPLY"
        const val EXTRA_RELEASE_DATE = "SearchActivity.RELEASE_DATE_REPLY"
        const val EXTRA_POSTER_PATH = "SearchActivity.POSTER_PATH_REPLY"
    }

    private var itemListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemClick(v: View, position: Int) {
            val movie = adapter.getItemAtPosition(position)

            val replyIntent = Intent()
            replyIntent.putExtra(EXTRA_TITLE, movie.title)
            replyIntent.putExtra(EXTRA_RELEASE_DATE, movie.getReleaseYearFromDate())
            replyIntent.putExtra(EXTRA_POSTER_PATH, movie.posterPath)
            setResult(Activity.RESULT_OK, replyIntent)

            finish()
        }
    }

    interface RecyclerItemListener {
        fun onItemClick(v: View, position: Int)
    }
}

// Контракт для MVP
interface SearchContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun displaySearchResults(tmdbResponse: TmdbResponse)
        fun displayError(string: String)
    }

    interface Presenter {
        fun searchMovies(query: String)
        fun onStop()
    }
}
