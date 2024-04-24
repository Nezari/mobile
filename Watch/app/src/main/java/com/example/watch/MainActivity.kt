package com.example.watch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var moviesRecyclerView: RecyclerView
    private var adapter: MainAdapter? = null
    private lateinit var fab: FloatingActionButton
    private lateinit var noMoviesLayout: LinearLayout

    private lateinit var dataSource: LocalDataSource
    private val compositeDisposable = CompositeDisposable()

    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    override fun onStart() {
        super.onStart()
        dataSource = LocalDataSource(application)
        getMyMoviesList()
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

    private fun getMyMoviesList() {
        val myMoviesDisposable = myMoviesObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer)

        compositeDisposable.add(myMoviesDisposable)
    }

    private val myMoviesObservable: Observable<List<Movie>>
        get() = dataSource.allMovies


    private val observer: DisposableObserver<List<Movie>>
        get() = object : DisposableObserver<List<Movie>>() {

            override fun onNext(movieList: List<Movie>) {
                displayMovies(movieList)
            }

            override fun onError(e: Throwable) {
                Log.d(tag, "Error$e")
                e.printStackTrace()
                displayError("Error fetching movie list")
            }

            override fun onComplete() {
                Log.d(tag, "Completed")
            }
        }

    fun displayMovies(movieList: List<Movie>?) {
        if (movieList.isNullOrEmpty()) {
            Log.d(tag, "No movies to display")
            moviesRecyclerView.visibility = INVISIBLE
            noMoviesLayout.visibility = VISIBLE
        } else {
            adapter = MainAdapter(movieList, this@MainActivity)
            moviesRecyclerView.adapter = adapter

            moviesRecyclerView.visibility = VISIBLE
            noMoviesLayout.visibility = INVISIBLE
        }
    }

    fun goToAddMovieActivity() {
        val myIntent = Intent(this@MainActivity, AddMovieActivity::class.java)
        startActivityForResult(myIntent, ADD_MOVIE_ACTIVITY_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_MOVIE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            showToast("Movie successfully added.")
        } else {
            displayError("Movie could not be added.")
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
                    dataSource.delete(movie)
                }

                if (adapter.selectedMovies.size == 1) {
                    showToast("Movie deleted")
                } else if (adapter.selectedMovies.size > 1) {
                    showToast("Movies deleted")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(str: String) {
        Toast.makeText(this@MainActivity, str, Toast.LENGTH_LONG).show()
    }

    fun displayError(e: String) {
        showToast(e)
    }

    companion object {
        const val ADD_MOVIE_ACTIVITY_REQUEST_CODE = 1
    }
}