package com.nabeel.erostestapp.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.nabeel.erostestapp.R
import com.nabeel.erostestapp.data.database.Entity.FavouriteMovie
import com.nabeel.erostestapp.util.AppConstant
import com.nabeel.erostestapp.util.Favourite
import com.nabeel.erostestapp.view.activity.MovieDetailsActivity
import kotlinx.android.synthetic.main.item_list_grid_top_rated.view.*


class MovieListGridRecyclerAdapter(
    val context: Context, favlistener: Favourite

) :
    RecyclerView.Adapter<MovieListGridRecyclerAdapter.MovieListViewHolder>() {
    private var listOfMovies = listOf<FavouriteMovie>()
    var mContext = context
    var favlistener = favlistener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list_grid_top_rated,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listOfMovies.size

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        val result = listOfMovies[position]
        Glide.with(mContext)
            .load(AppConstant.IMAGE_BASE_URL + result.poster)
            .apply(RequestOptions.centerCropTransform())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.ic_poster)

        holder.itemView.btn_favourite.isChecked = true
        holder.itemView.btn_favourite.tag = 1
        holder.itemView.btn_favourite.setShineDistanceMultiple(1.8f)

        holder.itemView.btn_favourite.setOnClickListener {
            if (holder.itemView.btn_favourite.tag == 0) {
                holder.itemView.btn_favourite.tag = 1
                holder.itemView.btn_favourite.setShapeResource(R.drawable.heart)
            } else {
                holder.itemView.btn_favourite.tag = 0
                holder.itemView.btn_favourite.setShapeResource(R.drawable.icc_heart)
            }
        }

        holder.itemView.btn_favourite.setOnCheckStateChangeListener { _: View?, checked: Boolean ->
            Log.e("HeartButton", "click $checked")
            if (!checked) {
                favlistener.remove(result.rank!!)
            }
        }

        holder.itemView.setOnClickListener {
            context.startActivity(
                Intent(context, MovieDetailsActivity::class.java).putExtra(
                    "id",
                    result.rank
                )
            )
        }

    }

    fun setMovieList(listOfMovies: List<FavouriteMovie>) {
        this.listOfMovies = listOfMovies
        notifyDataSetChanged()
    }

    class MovieListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ic_poster = itemView.findViewById<AppCompatImageView>(R.id.ic_poster)
    }
}