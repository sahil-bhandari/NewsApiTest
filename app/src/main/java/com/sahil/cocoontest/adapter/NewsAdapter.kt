package com.sahil.cocoontest.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.sahil.cocoontest.R
import com.sahil.cocoontest.WebViewActivity
import com.sahil.cocoontest.room.AppDatabase
import com.sahil.cocoontest.models.localdb.NewsTable
import com.sahil.cocoontest.models.network.Results
import com.sahil.cocoontest.utils.utility.covertTimeToText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NewsAdapter(mContext: Context, resultsList: List<Results>) :
    RecyclerView.Adapter<NewsAdapter.Holder>() {
    private val mContext: Context = mContext
    private var resultsList = ArrayList<Results>()

    init {
        this.resultsList = resultsList as ArrayList<Results>
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val textViewTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val textViewDescription: TextView = itemView.findViewById(R.id.txtDescription)
        val textViewDate: TextView = itemView.findViewById(R.id.txtDate)
        val imageBookmark: ImageView = itemView.findViewById(R.id.imageBookmark)
        val cardView: CardView = itemView.findViewById(R.id.cardView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_top_stories, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val result: Results = resultsList[position]
        holder.textViewDate.text = result.published_date
        holder.textViewTitle.text = result.title
        holder.textViewDescription.text = result.abstract
        holder.imageBookmark.tag = R.drawable.ic_baseline_bookmark_border_24
        try {
            val requestOptions: RequestOptions = RequestOptions().diskCacheStrategy(
                DiskCacheStrategy.AUTOMATIC)

            Glide.with(mContext)
                .load(result.multimedia[0].url)
                .apply(requestOptions)
                .into(holder.imageView)
        } catch (ex: Exception) {
        }

        holder.cardView.setOnClickListener { view: View? ->
            val intent = Intent(mContext, WebViewActivity::class.java)
            intent.putExtra(mContext.resources.getString(R.string.url), result.url)
            mContext.startActivity(intent)

        }
        holder.imageBookmark.setOnClickListener { view: View? ->

            if (holder.imageBookmark.tag == R.drawable.ic_baseline_bookmark_border_24){
                val topStories = NewsTable(
                    title = result.title,
                    description = result.abstract,
                    publishDate = result.published_date,
                    image = result.multimedia[0].url,
                    mainUrl = result.url
                )
                saveData(topStories)
                holder.imageBookmark.setImageDrawable(
                    ContextCompat.getDrawable(
                    mContext, // Context
                    R.drawable.ic_baseline_bookmark_24 // Drawable
                ))
                holder.imageBookmark.tag = R.drawable.ic_baseline_bookmark_24
            } else{
                Toast.makeText(mContext,"Already bookmarked",Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun saveData(topStoriesTable: NewsTable) {

        GlobalScope.launch(Dispatchers.Main) { // Coroutine Dispatcher confined to Main UI Thread
            async {
                val dao = AppDatabase.getInstance(mContext).topNewsDao()
                dao.insertData(topStoriesTable)
            }
            Toast.makeText(mContext , "News is bookmarked.", Toast.LENGTH_SHORT).show()
        }

    }


    override fun getItemCount(): Int {
        return resultsList.size
    }
}
