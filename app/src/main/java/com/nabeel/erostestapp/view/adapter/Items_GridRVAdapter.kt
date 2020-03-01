package com.nabeel.erostestapp.view.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nabeel.erostestapp.R
import com.nabeel.erostestapp.data.database.Entity.FavouriteMovie
import com.nabeel.erostestapp.util.AppConstant
import com.nabeel.erostestapp.util.Favourite
import kotlinx.android.synthetic.main.item_list_grid_top_rated.view.*
import kotlinx.android.synthetic.main.progress_loading.view.*
import com.nabeel.erostestapp.model.Result
import com.nabeel.erostestapp.view.activity.MovieDetailsActivity

class Items_GridRVAdapter(
    private var itemsCells: ArrayList<Result?>,
    private var itemListDB: List<FavouriteMovie>,
    var context: Context,
    favlistener: Favourite
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context
    var favlistener = favlistener
    var isFavourite: Boolean = false

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addData(dataViews: ArrayList<Result?>) {
        this.itemsCells.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun setSearchMovieList(itemsCells: ArrayList<Result?>) {
        this.itemsCells = itemsCells
        notifyDataSetChanged()
    }

    fun getItemAtPosition(position: Int): Result? {
        return itemsCells[position]
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            itemsCells.add(null)
            notifyItemInserted(itemsCells.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (itemsCells.size != 0) {
            itemsCells.removeAt(itemsCells.size - 1)
            notifyItemRemoved(itemsCells.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        return if (viewType == AppConstant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_grid_top_rated, parent, false)
            ItemViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(mcontext).inflate(R.layout.progress_loading, parent, false)
            view.progressbar.indeterminateDrawable.setColorFilter(
                Color.WHITE,
                PorterDuff.Mode.MULTIPLY
            )
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return itemsCells.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemsCells[position] == null) {
            AppConstant.VIEW_TYPE_LOADING
        } else {
            AppConstant.VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == AppConstant.VIEW_TYPE_ITEM) {

            val result = itemsCells[position]

            Glide.with(mcontext)
                .load(AppConstant.IMAGE_BASE_URL + result?.posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_placeholder)
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.itemView.ic_poster)

            for (favourite in itemListDB) {
                if (result?.id == favourite.rank) {
                    isFavourite = true
                    break
                } else {
                    isFavourite = false
                }
            }

            if (isFavourite) {
                holder.itemView.btn_favourite.isChecked = true
                holder.itemView.btn_favourite.tag = 1
            } else {
                holder.itemView.btn_favourite.tag = 0
                holder.itemView.btn_favourite.isChecked = false
            }

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
                if (checked) {
                    favlistener.add(result!!)
                    itemsCells[position]?.favourite = true
                } else {
                    favlistener.remove(result?.id!!)
                    itemsCells[position]?.favourite = false
                }
            }

            holder.itemView.setOnClickListener {
                context.startActivity(
                    Intent(context, MovieDetailsActivity::class.java).putExtra(
                        "id",
                        result?.id
                    )
                )
            }
        }
    }
}