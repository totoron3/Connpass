package com.s24.connpassclient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.s24.connpassclient.databinding.SearchListBinding


class EventListAdapter (
    private val layoutInflater: LayoutInflater,
    private var eventList: ArrayList<Event>,
    private val callback: Callback?
) : RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    interface Callback {                                                //Callbackを使用できるようにしたもの
        fun openDetail(event: Event)
    }

    override fun getItemCount() = eventList.size                        //自分が持っているリストの要素数を調べる

    override fun onCreateViewHolder(                                    //ビューホルダーを作成する
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = DataBindingUtil.inflate<SearchListBinding>(
            layoutInflater,
            R.layout.search_list,
            parent,
            false
        )
        return ViewHolder(
            binding,
            callback
        )
    }

    override fun onBindViewHolder(                                     //ホルダーに1つずつ結果を入れる
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(eventList[position])
    }

    class ViewHolder(                                                      //ビューホルダーの設計図
        private val binding: SearchListBinding,
        private val callback: Callback?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {                                            //bindはデータバインディングを使ってデータを入れるための関数
            binding.event = event
            binding.root.setOnClickListener {
                callback?.openDetail(event)
            }
        }
    }
}
