package com.sahil.cocoontest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sahil.cocoontest.R
import com.sahil.cocoontest.adapter.BookMarkAdapter
import com.sahil.cocoontest.models.localdb.NewsTable
import com.sahil.cocoontest.room.AppDatabase
import com.sahil.cocoontest.room.NewsDao


/**
 * A simple [Fragment] subclass.
 * Use the [BookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarkFragment : Fragment() {

    lateinit var rootView: View
    lateinit var dao : NewsDao
    lateinit var adapter: BookMarkAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var noDataFoundImage: ImageView
    var topStoriesList = ArrayList<NewsTable>()
    lateinit var mySwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_bookmark, container, false)
        initViews()
        if (savedInstanceState == null) {
            fetchBookmarkData()
        }
        return rootView
    }




    private fun initViews() {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        noDataFoundImage = rootView.findViewById(R.id.noDataImg)
        adapter = BookMarkAdapter(requireActivity(), topStoriesList,object : BookMarkAdapter.ItemClickListener {
            override fun clickToDelete(position: NewsTable) {
                dao.deleteData(position)
                Toast.makeText(context,"Entry deleted.",Toast.LENGTH_LONG).show()
                fetchBookmarkData()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.notifyItemRangeInserted(0, topStoriesList.size)
        mySwipeRefreshLayout = rootView.findViewById(R.id.top_swipe)

        mySwipeRefreshLayout.setOnRefreshListener {
            // refresh data
            fetchBookmarkData()
        }
    }


    private fun fetchBookmarkData() {
        topStoriesList.clear()
        dao = context?.let { AppDatabase.getInstance(it).topNewsDao() }!!
        topStoriesList.addAll(dao.getAllData())
        if (topStoriesList.size > 0) {
            recyclerView.visibility = View.VISIBLE
            noDataFoundImage.visibility = View.GONE
        } else{
            recyclerView.visibility = View.GONE
            noDataFoundImage.visibility = View.VISIBLE
        }
        adapter.notifyDataSetChanged()

        if (mySwipeRefreshLayout.isRefreshing) {
            mySwipeRefreshLayout.isRefreshing = false
        }
    }
}