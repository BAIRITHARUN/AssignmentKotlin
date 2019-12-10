package com.infy.assignmentkotlin.main_activity.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.infy.assignmentkotlin.R
import com.infy.assignmentkotlin.main_activity.model.Row
import com.infy.assignmentkotlin.main_activity.view.fragments.MainFragFragment
import java.util.ArrayList

class TitlesAdapter(
    internal var context: MainFragFragment,
    internal var rowArrayList: ArrayList<Row>,
    internal var listener: IOnRowClickListener
) :
    RecyclerView.Adapter<TitlesAdapter.TitlesHolder>() {

    interface IOnRowClickListener {
        fun onRowClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitlesHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.inflater_titiles_list_item, parent, false)
        return TitlesHolder(view)
    }

    override fun onBindViewHolder(holder: TitlesHolder, position: Int) {
        if (rowArrayList[position].title != null) {
            holder.mTvTitle.setText(rowArrayList[position].title)
        } else {
            holder.mTvTitle.text = "No Title"
        }

        if (rowArrayList[position].description != null) {
            holder.mTvItemDesc.setText(rowArrayList[position].description)
        } else {
            holder.mTvItemDesc.text = "No Description"
        }
        // example url "https://surrealhotels.com/wp-content/uploads/2014/10/Special_Offers2.jpg"
        var imgUrl = rowArrayList[position].imageHref
        if (imgUrl != null && imgUrl!!.contains("http://")) {
            imgUrl = imgUrl!!.replace("http://", "https://")
        }
        if (imgUrl != null) {
            try {
                Glide.with(context)
                    .load(imgUrl)
                    .error(R.drawable.no_preview)
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?, model: Any,
                            target: Target<Drawable>, isFirstResource: Boolean
                        ): Boolean {
                            holder.mPBLoadImage.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable, model: Any,
                            target: Target<Drawable>, dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.mPBLoadImage.visibility = View.GONE
                            return false
                        }
                    }).into(holder.mImgItem)

            } catch (e: Exception) {
                Toast.makeText(context.activity, e.message +"at position" +position, Toast.LENGTH_SHORT).show()
            }

        } else {
            holder.mImgItem.setImageResource(R.drawable.no_preview)
        }

        holder.mRelListItem.setOnClickListener { listener.onRowClick(position) }
    }

    override fun getItemCount(): Int {
        return rowArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class TitlesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var mImgItemDetails: ImageView
        internal var mImgItem: ImageView
        internal var mTvTitle: TextView
        internal var mTvItemDesc: TextView
        internal var mRelListItem: RelativeLayout
        internal var mPBLoadImage: ProgressBar

        init {
            //            ButterKnife.bind(this, itemView);
            mImgItemDetails = itemView.findViewById(R.id.mImgItemDetails)
            mTvTitle = itemView.findViewById(R.id.mTvTitle)
            mTvItemDesc = itemView.findViewById(R.id.mTvItemDesc)
            mImgItem = itemView.findViewById(R.id.mImgItem)
            mRelListItem = itemView.findViewById(R.id.mRelListItem)
            mPBLoadImage = itemView.findViewById(R.id.mPBLoadImage)
        }
    }
}