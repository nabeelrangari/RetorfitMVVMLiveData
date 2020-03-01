package com.nabeel.erostestapp.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akbartravel.erostest.view.base.BaseFragment
import com.nabeel.erostestapp.R
import com.nabeel.erostestapp.model.Result
import com.nabeel.erostestapp.util.Favourite
import com.nabeel.erostestapp.view.adapter.MovieListGridRecyclerAdapter
import com.nabeel.erostestapp.viewmodel.FavouriteViewModel

class FavouriteFragment : BaseFragment<FavouriteViewModel>() {

    lateinit var rootView: View
    lateinit var recyclerView: RecyclerView
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var adapter: MovieListGridRecyclerAdapter

    override fun providerVMClass(): Class<FavouriteViewModel> = FavouriteViewModel::class.java

    override fun provideYourFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_favourite, parent, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        setAdapter()
        setRVLayoutManager()
        return rootView
    }

    private fun setAdapter() {
        adapter = MovieListGridRecyclerAdapter(requireContext(), object : Favourite {
            override fun add(result: Result) {
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
    }

    override fun startObserve() {
        super.startObserve()

        mViewModel?.getFavouriteMovies()?.observe(this, Observer {
            adapter.setMovieList(it)
        })
    }

    fun removeFav(rank: Int) {
        mViewModel?.deleteFavouriteMovie(rank)?.observe(this, Observer {
            Toast.makeText(requireContext(), "Item deleted successfully", Toast.LENGTH_LONG).show()
        })
    }
}
