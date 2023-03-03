package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.RecyclerviewnewsBinding
import com.example.newsapp.models.News
import com.squareup.picasso.Picasso

class RecyclerAdapter(
    private val news: List<News>,
    private val onRecyclerViewClick: OnRecyclerViewClick, private val x: ArrayList<String>
) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: RecyclerviewnewsBinding) :
        RecyclerView.ViewHolder(binding.root)

    //By Using ViewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = RecyclerviewnewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
            titleText.text = news[position].title
            descriptionText.text = news[position].description
            dateText.text = news[position].publishedAt
            Picasso.Builder(imageNews.context).build()
                .load(x[position].toUri()).into(imageNews)
            customLayout.setOnClickListener {
                onRecyclerViewClick.onClicked(position)
            }
        }
    }
    override fun getItemCount(): Int {
        return news.size
    }

   /* //insert new items to the list in database(i don't use it here)
    fun insertItem(newList:List<News>){
        val differCallback = MyDiffUtil(news,newList)
        val differResult = DiffUtil.calculateDiff(differCallback)

        news.toMutableList().addAll(newList)
        differResult.dispatchUpdatesTo(this)
    }*/

    //to clear the list in database then update the list
    fun updateItem(newList:List<News>){
        val differCallback = MyDiffUtil(news,newList)
        val differResult = DiffUtil.calculateDiff(differCallback)

        news.toMutableList().clear()//to clear oldList
        news.toMutableList().addAll(newList)
        differResult.dispatchUpdatesTo(this)
    }
}