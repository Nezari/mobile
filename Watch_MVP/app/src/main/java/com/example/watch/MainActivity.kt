package com.example.watch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.watch.adapters.MainAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.watch.models.LocalDataSource
import com.example.watch.models.Movie
import com.example.watch.presenters.MainPresenter
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity(), MainContract.View {

    private lateinit var presenter: MainContract.Presenter

    private lateinit var moviesRecyclerView: RecyclerView
    private var adapter: MainAdapter? = null
    private lateinit var fab: FloatingActionButton
    private lateinit var noMoviesLayout: LinearLayout

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()

        presenter = MainPresenter(this, LocalDataSource(application))
    }

    override fun onStart() {
        super.onStart()
        presenter.getMovies()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun setupViews() {
        moviesRecyclerView = findViewById(R.id.movies_recyclerview)
        moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        fab = findViewById(R.id.fab)
        noMoviesLayout = findViewById(R.id.no_movies_layout)
        supportActionBar?.title = "Movies to Watch"
    }

    override fun displayMovies(movieList: List<Movie>?) {
        if (movieList.isNullOrEmpty()) {
            moviesRecyclerView.visibility = INVISIBLE
            noMoviesLayout.visibility = VISIBLE
        } else {
            adapter = MainAdapter(movieList, this@MainActivity)
            moviesRecyclerView.adapter = adapter

            moviesRecyclerView.visibility = VISIBLE
            noMoviesLayout.visibility = INVISIBLE
        }
    }

    override fun showError(message: String) {
        showToast(message)
    }

    @Suppress("DEPRECATION")
    fun goToAddMovieActivity(v: View) {
        val myIntent = Intent(this@MainActivity, AddMovieActivity::class.java)
        startActivityForResult(myIntent, ADD_MOVIE_ACTIVITY_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_MOVIE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            showToast("Movie successfully added.")
        } else {
            showError("Movie could not be added.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteMenuItem) {
            val adapter = this.adapter
            if (adapter != null) {
                for (movie in adapter.selectedMovies) {
                    presenter.deleteMovie(movie)
                }

                val message = when (adapter.selectedMovies.size) {
                    1 -> "Movie deleted"
                    else -> "Movies deleted"
                }
                showToast(message)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(str: String) {
        Toast.makeText(this@MainActivity, str, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val ADD_MOVIE_ACTIVITY_REQUEST_CODE = 1
    }
}

interface MainContract {

    interface View {
        fun displayMovies(movieList: List<Movie>?)
        fun showError(message: String)
    }

    interface Presenter {
        fun getMovies()
        fun deleteMovie(movie: Movie)
    }
}
