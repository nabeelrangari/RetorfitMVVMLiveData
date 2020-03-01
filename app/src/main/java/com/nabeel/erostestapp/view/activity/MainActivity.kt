package com.nabeel.erostestapp.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.akbartravel.revampapi.view.base.BaseActivity
import com.nabeel.erostestapp.R
import com.nabeel.erostestapp.util.Communicate
import com.nabeel.erostestapp.util.NetworkStateReceiver
import com.nabeel.erostestapp.util.Utils
import com.nabeel.erostestapp.view.adapter.ViewPagerAdapter
import com.nabeel.erostestapp.view.fragment.FavouriteFragment
import com.nabeel.erostestapp.view.fragment.TopRatedFragment
import com.nabeel.erostestapp.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<HomeViewModel>(),
    NetworkStateReceiver.NetworkStateReceiverListener {

    private var networkStateReceiver: NetworkStateReceiver? = null
    private lateinit var svSearch: SearchView
    private var listener: Communicate? = null
    override fun layoutId(): Int = R.layout.activity_main
    override fun providerVMClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addListener()
    }

    override fun initView() {
        super.initView()
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(TopRatedFragment(), "Top Rated")
        adapter.addFragment(FavouriteFragment(), "Favourite")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    fun setOnSearchListener(listener: Communicate) {
        this.listener = listener
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        svSearch = menu!!.findItem(R.id.action_search).actionView as SearchView
        svSearch.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        svSearch.maxWidth = Int.MAX_VALUE
        svSearch.queryHint = "Enter movie name..."

        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewPager.currentItem = 0
                query?.let {
                    listener?.getSearchQuery(query)
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let {
                    listener?.getSearchQuery(query)
                }
                return false
            }

        })
        return true
    }

    override fun onBackPressed() {
        try {
            if (!svSearch.isIconified) {
                svSearch.isIconified = true
                svSearch.clearFocus()
                return
            } else {
                super.onBackPressed()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun networkAvailable() {
    }

    override fun networkUnavailable() {
        Utils.showSnackBar(ll_root, resources.getString(R.string.str_nointernetconn))
    }

    private fun addListener() {
        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver!!.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    private fun removeListener() {
        try {
            networkStateReceiver!!.removeListener(this)
            unregisterReceiver(networkStateReceiver)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeListener()
    }
}
