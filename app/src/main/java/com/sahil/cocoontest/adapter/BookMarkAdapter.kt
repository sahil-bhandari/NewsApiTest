package com.sahil.cocoontest.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sahil.cocoontest.R
import com.sahil.cocoontest.WebViewActivity
import com.sahil.cocoontest.models.localdb.NewsTable
import com.sahil.cocoontest.utils.utility.covertTimeToText

class BookMarkAdapter(mContext: Context, topStoriesList: List<NewsTable>,private val listener: ItemClickListener) : RecyclerView.Adapter<BookMarkAdapter.Holder>() {
    private val mContext: Context = mContext
    private var topStoriesList = ArrayList<NewsTable>()

    init {
        this.topStoriesList = topStoriesList as ArrayList<NewsTable>
    }

    companion object {
        var mClickListener: ItemClickListener? = null
    }

    interface ItemClickListener {
        fun clickToDelete(position: NewsTable)
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val textViewTitle: TextView = itemView.findViewById(R.id.title)
        val textViewDate: TextView = itemView.findViewById(R.id.body)
        val cardView: LinearLayout = itemView.findViewById(R.id.cardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_bookmark, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val newsTable: NewsTable = topStoriesList[position]
        mClickListener = listener
        holder.textViewDate.text = "Added: ${covertTimeToText(newsTable.publishDate)}"
        holder.textViewTitle.text = newsTable.title
        try {
            val requestOptions: RequestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

            Glide.with(mContext)
                .load(newsTable.image)
                .apply(requestOptions)
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .into(holder.imageView)

        } catch (ex: Exception) {
            Log.d("", "onBindViewHolder: $ex")
        }

        holder.cardView.setOnClickListener {
            val intent = Intent(mContext, WebViewActivity::class.java)
            intent.putExtra(mContext.resources.getString(R.string.url), newsTable.mainUrl)
            mContext.startActivity(intent)
        }

        holder.cardView.setOnLongClickListener {
            showDialog(newsTable,mContext)
        }
    }

    private fun showDialog(position: NewsTable, mContext: Context): Boolean {
        val alertDialog= AlertDialog.Builder(mContext)
        alertDialog.setTitle("Delete!")
            .setMessage("Are you Sure?")
            .setPositiveButton(android.R.string.ok) { dialogInterface, i ->
                try {
                    mClickListener?.clickToDelete(position)
                } catch (e: Exception) {
                    Toast.makeText(mContext,"Unable to delete entry.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(android.R.string.cancel){dialogInterface, i ->null}
            .show()

        return true
    }

    override fun getItemCount(): Int {
        return topStoriesList.size
    }
}