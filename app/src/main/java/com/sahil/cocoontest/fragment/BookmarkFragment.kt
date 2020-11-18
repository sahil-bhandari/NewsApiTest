package com.sahil.cocoontest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sahil.cocoontest.R
import com.sahil.cocoontest.adapter.BookMarkAdapter
import com.sahil.cocoontest.models.localdb.NewsTable
import com.sahil.cocoontest.room.AppDatabase


/**
 * A simple [Fragment] subclass.
 * Use the [BookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarkFragment : Fragment() {

    lateinit var rootView: View
    lateinit var adapter: BookMarkAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var noDataFoundImage: ImageView
    var topStoriesList = ArrayList<NewsTable>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_bookmark, container, false)
        initViews()
        fetchBookmarkData()
        return rootView
    }

    override fun onResume() {
        super.onResume()
        //Fetch data stored in SQLite DB
        fetchBookmarkData()
    }

    private fun initViews() {
        recyclerView = rootView.findViewById(R.id.recyclerView)
        noDataFoundImage = rootView.findViewById(R.id.noDataImg)
        adapter = BookMarkAdapter(requireActivity(), topStoriesList,)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }


    private fun fetchBookmarkData() {
        topStoriesList.clear()
        val dao = AppDatabase.getInstance(requireActivity()).topStoriesDao()
        topStoriesList.addAll(dao.getAllData())
        if (topStoriesList.size == 0) {
            recyclerView.visibility = View.GONE
            noDataFoundImage.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            noDataFoundImage.visibility = View.GONE
        }
        adapter.notifyDataSetChanged()
    }
}