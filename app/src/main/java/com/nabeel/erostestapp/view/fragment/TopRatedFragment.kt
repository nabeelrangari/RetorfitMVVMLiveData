package com.nabeel.erostestapp.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akbartravel.erostest.view.base.BaseFragment
import com.example.loadmoreexample.OnLoadMoreListener
import com.example.loadmoreexample.RecyclerViewLoadMoreScroll
import com.nabeel.erostestapp.R
import com.nabeel.erostestapp.data.database.Entity.FavouriteMovie
import com.nabeel.erostestapp.data.network.Status
import com.nabeel.erostestapp.model.Result
import com.nabeel.erostestapp.util.AppConstant
import com.nabeel.erostestapp.util.Communicate
import com.nabeel.erostestapp.util.Favourite
import com.nabeel.erostestapp.view.activity.MainActivity
import com.nabeel.erostestapp.view.adapter.Items_GridRVAdapter
import com.nabeel.erostestapp.viewmodel.TopRatedViewModel
import kotlinx.android.synthetic.main.activity_movie_details.*

class TopRatedFragment : BaseFragment<TopRatedViewModel>(), Communicate {
    lateinit var rootView: View
    lateinit var recyclerView: RecyclerView

    /* LOAD MORE */
    lateinit var adapter: Items_GridRVAdapter
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    lateinit var itemsCells: ArrayList<Result?>
    lateinit var loadMoreItemsCells: ArrayList<Result?>
    lateinit var searchItemsCells: ArrayList<Result?>

    var itemListDB: List<FavouriteMovie> = ArrayList()
    var page: Int = 1
    var totalPages: Int = 0

    override fun providerVMClass(): Class<TopRatedViewModel> = TopRatedViewModel::class.java
    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_top_rated, parent, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)

        setItemsData()
        setAdapter()
        setRVLayoutManager()
        setRVScrollListener()
        return rootView
    }

    private fun setItemsData() {
        itemsCells = ArrayList()

        mViewModel?.getFavouriteMovies()?.observe(this, Observer {
            Log.e("Favourite", "DB Size - " + it.size)
            itemListDB = it
            setAdapter()
        })
    }

    private fun setAdapter() {
        adapter = Items_GridRVAdapter(itemsCells, itemListDB, requireContext(), object : Favourite {
            override fun add(result: Result) {
                addFav(result)
            }

            override fun remove(rank: Int) {
                removeFav(rank)
            }
        })
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
    }

    private fun setRVLayoutManager() {
        mLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        (mLayoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter.getItemViewType(position)) {
                        AppConstant.VIEW_TYPE_ITEM -> 1
                        AppConstant.VIEW_TYPE_LOADING -> 2 //number of columns of the grid
                        else -> -1
                    }
                }
            }
    }

    private fun setRVScrollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as GridLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                if (page <= totalPages)
                    loadMore(page)
            }
        })

        recyclerView.addOnScrollListener(scrollListener)
    }

    override fun startObserve() {
        super.startObserve()
        movies()
        (activity as MainActivity?)?.setOnSearchListener(object : Communicate {
            override fun getSearchQuery(query: String) {
                Log.e("Fragment", "query - $query")
                if (query.isNotEmpty())
                    searchMovie(query)
                else
                    adapter.setSearchMovieList(itemsCells)
            }
        })
    }

    private fun movies() {
        mViewModel?.getTopRatedMovies(page)?.observe(this, Observer { networkResource ->
            when (networkResource.status) {
                Status.LOADING -> {
                    Log.e("TAG", "Loading data from network")
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val topRatedMovies = networkResource.data
                    topRatedMovies.let {
                        Log.e("movies", "result size - " + topRatedMovies?.results?.size)
                        Log.e("movies", "page - $page")
                        progressBar.visibility = View.GONE

                        topRatedMovies?.results?.let { it1 ->
                            page++
                            totalPages = topRatedMovies.totalPages!!
                            itemsCells.addAll(it1)
                            adapter.addData(itemsCells)
                        }
                    }
                }
                Status.ERROR -> {
                    Log.e("TAG", "Error loading data from network")
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun loadMore(pageNumber: Int) {
        //Add the Loading View
        adapter.addLoadingView()
        loadMoreItemsCells = ArrayList()

        mViewModel?.getTopRatedMovies(pageNumber)?.observe(this, Observer { networkResource ->
            when (networkResource.status) {
                Status.LOADING -> {
                    Log.e("TAG", "Loading data from network")
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val topRatedMovies = networkResource.data
                    topRatedMovies.let {
                        Log.e("loadMore", "result size - " + topRatedMovies?.results?.size)
                        Log.e("loadMore", "page - $page")
                        progressBar.visibility = View.GONE

                        topRatedMovies?.results?.let { it1 ->
                            page++
                            loadMoreItemsCells.addAll(it1)

                            //Remove the Loading View
                            adapter.removeLoadingView()
                            //We adding the data to our main ArrayList
                            adapter.addData(loadMoreItemsCells)
                            //Change the boolean isLoading to false
                            scrollListener.setLoaded()
                            //Update the recyclerView in the main thread
                            recyclerView.post {
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    Log.e("TAG", "Error loading data from network")
                    progressBar.visibility = View.GONE
                    scrollListener.setLoaded()
                }
            }
        })
    }

    fun searchMovie(input: String) {
        searchItemsCells = ArrayList()
        mViewModel?.getSearchResult(input, page)?.observe(this, Observer { networkResource ->
            when (networkResource.status) {
                Status.LOADING -> {
                    Log.e("TAG", "Loading data from network")
                    progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val searchResult = networkResource.data
                    searchResult.let {
                        Log.e("TAG", "movieDetails - " + searchResult?.totalResults)
                        progressBar.visibility = View.GONE
                        searchResult?.results?.let { it1 ->
                            searchItemsCells.addAll(it1)
                            adapter.setSearchMovieList(searchItemsCells)
                            Log.e("movies", "itemsCells - ${searchItemsCells.size}")
                        }
                    }
                }
                Status.ERROR -> {
                    Log.e("TAG", "Error loading data from network")
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun getSearchQuery(query: String) {
        Log.e("Fragment", "query - $query")
    }

    fun addFav(result: Result) {
        val favourite = FavouriteMovie(0, result.id!!, result.title!!, result.posterPath!!)
        mViewModel?.addFavouriteMovie(favourite)?.observe(this, Observer {
            Log.e("Fragment", "Add - ${favourite.title}")
        })
    }

    fun removeFav(rank: Int) {
        mViewModel?.deleteFavouriteMovie(rank)?.observe(this, Observer {
            Log.e("Fragment", "remove - ${rank}")
        })
    }
}