package com.sahil.cocoontest.adapter

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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sahil.cocoontest.R
import com.sahil.cocoontest.WebViewActivity
import com.sahil.cocoontest.room.AppDatabase
import com.sahil.cocoontest.models.localdb.NewsTable
import com.sahil.cocoontest.models.network.Results
import com.sahil.cocoontest.utils.utility.covertTimeToText

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

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val result: Results = resultsList[position]
        holder.textViewDate.text = "published: " + covertTimeToText(result.published_date)
        holder.textViewTitle.text = result.title
        holder.textViewDescription.text = result.abstract
        try {
            Glide.with(mContext)
                .load(result.multimedia[0].url)
                .into(holder.imageView)
        } catch (ex: Exception) {
        }

        holder.cardView.setOnClickListener { view: View? ->
            val intent = Intent(mContext, WebViewActivity::class.java)
            intent.putExtra(mContext.resources.getString(R.string.url), result.url)
            mContext.startActivity(intent)

        }
        holder.imageBookmark.setOnClickListener { view: View? ->

            val topStories = NewsTable(
                title = result.title,
                description = result.abstract,
                publishDate = result.published_date,
                image = result.multimedia.get(0).url,
                mainUrl = result.url
            )
            saveData(topStories)
        }
    }

    private fun saveData(topStoriesTable: NewsTable) {

        class DataSave: AsyncTask<Void, Void, Void>(){
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = AppDatabase.getInstance(mContext).topStoriesDao()
                dao.insertData(topStoriesTable)
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                Toast.makeText(mContext , "News is bookmarked.", Toast.LENGTH_SHORT).show()
            }

        }
        DataSave().execute()

    }


    override fun getItemCount(): Int {
        return resultsList.size
    }
}