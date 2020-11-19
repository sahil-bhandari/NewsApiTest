package com.sahil.cocoontest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sahil.cocoontest.BuildConfig
import com.sahil.cocoontest.retrofit.RetrofitAPISetup
import com.sahil.cocoontest.R
import com.sahil.cocoontest.adapter.NewsAdapter
import com.sahil.cocoontest.models.network.Results
import com.sahil.cocoontest.utils.utility
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : Fragment() {
    lateinit var rootView: View
    lateinit var adapter: NewsAdapter
    var resultsList = ArrayList<Results>()
    lateinit var recyclerView: RecyclerView
    lateinit var mySwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_news, container, false)
        initView()
        getNewsData()
        return rootView
    }

    private fun getNewsData() {
        // check internet connectivity
        if (utility.isOnline) {
            recyclerView.visibility = View.VISIBLE
            getData()
        } else {
            progressBar.visibility=View.GONE
            Toast.makeText(context,"Internet not available.",Toast.LENGTH_LONG).show()
        }
    }


    private fun initView() {
        // add data to recycler view adapters to view in items
        adapter = context?.let { NewsAdapter(mContext = it, resultsList = resultsList) }!!
        recyclerView = rootView.findViewById(R.id.top_recycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = null
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.notifyItemRangeInserted(0, resultsList.size)
        mySwipeRefreshLayout = rootView.findViewById(R.id.top_swipe)
        progressBar = rootView.findViewById(R.id.progress)
        mySwipeRefreshLayout.setOnRefreshListener {
            // refresh data
            getNewsData()
        }
    }

    private fun getData() {
        // fetch data using retrofit
        val apiService = RetrofitAPISetup.API_INTERFACE
        //coroutine lib used instead of async to avoid ANR error
        CoroutineScope(Dispatchers.IO).launch {
            resultsList.clear()
            val response = apiService.getTopStories(BuildConfig.APIKEY)
            MainScope().launch {
                withContext(Dispatchers.Default) {
                    resultsList.addAll(response.execute().body()!!.results)
                }
                progressBar.visibility=View.GONE
                adapter.notifyDataSetChanged()
            }
        }
        if (mySwipeRefreshLayout.isRefreshing) {
            mySwipeRefreshLayout.isRefreshing = false
        }
    }

}