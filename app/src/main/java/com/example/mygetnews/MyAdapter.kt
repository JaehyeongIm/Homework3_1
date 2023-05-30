package com.example.mygetnews

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygetnews.databinding.RowBinding
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(var items:ArrayList<MyData>):RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    val filteredItems = ArrayList<MyData>()
    init {
        filteredItems.addAll(items)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun filter(query:String?){
        filteredItems.clear()
        if(query.isNullOrBlank()){
            filteredItems.addAll(items)
        }
        else{
            val hangulQuery = filterHangulCharacters(query)
            val lowerCaseQuery = hangulQuery.toLowerCase(Locale.getDefault())
            items.forEach { item->
                val filterTitle = filterHangulCharacters(item.title)
                if(filterTitle.toLowerCase(Locale.getDefault()).contains(lowerCaseQuery)){
                    filteredItems.add(item)
            }

            }

        }
        notifyDataSetChanged()
    }
    @SuppressLint("SuspiciousIndentation")
    fun filterHangulCharacters(input:String):String {
    val hangulRegex=Regex("[ㄱ-ㅎㅏ-ㅣ가-힣0-9]+")
        return input.filter {it.toString().matches(hangulRegex)}


    }
    interface OnItemClickListener{
        fun OnItemClick(position:Int)
    }
    var itemClickListener:OnItemClickListener?=null
    inner class MyViewHolder(val binding:RowBinding):RecyclerView.ViewHolder(binding.root){
        init{
            binding.newstitle.setOnClickListener{
                itemClickListener?.OnItemClick(adapterPosition)
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        if (filteredItems.isNullOrEmpty())
            holder.binding.newstitle.text=items[position].title
        else
        holder.binding.newstitle.text=filteredItems[position].title


    }

    override fun getItemCount(): Int {
        if (filteredItems.isNullOrEmpty())
            return items.size
        else
            return filteredItems.size
    }

}