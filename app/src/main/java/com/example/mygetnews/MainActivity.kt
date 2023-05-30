package com.example.mygetnews

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygetnews.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var adapter: MyAdapter
    val url = "https://news.nate.com/rank/interest?sc=spo"
    val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
        //initSearch()
    }

    private fun initLayout() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText)
                return true
            }
        })


        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true
            getnews()

        }


        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true
            getnews()

        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        adapter = MyAdapter(ArrayList<MyData>())
        adapter.itemClickListener = object:MyAdapter.OnItemClickListener{
            override fun OnItemClick(position: Int) {
                val newsItem = adapter.items[position]
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(adapter.items[position].url))
                startActivity(intent)

            }
        }

        binding.recyclerView.adapter=adapter
        getnews()

    }

    private fun getnews() {
        scope.launch {
            adapter.items.clear()
            val doc = Jsoup.connect(url).get()

            val newsItems = doc.select("ul[class='mduSubject mduRankSubject'] li a")
            for(newsItem in newsItems) {
                val title = newsItem.text()
                val newsUrl = newsItem.absUrl("href")
                adapter.items.add(MyData(title, newsUrl))

            }
            withContext(Dispatchers.Main){
                adapter.notifyDataSetChanged()
                binding.swipe.isRefreshing = false
            }
        }
    }


}
