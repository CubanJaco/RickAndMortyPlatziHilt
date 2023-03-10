package com.example.rickandmortyplatzihilt.adapters

import com.example.rickandmortyplatzihilt.domain.Episode
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyplatzihilt.R
import com.example.rickandmortyplatzihilt.databinding.ItemListEpisodeBinding
import com.example.rickandmortyplatzihilt.utils.bindingInflate

class EpisodeListAdapter(
    private val listener: (Episode) -> Unit
): RecyclerView.Adapter<EpisodeListAdapter.EpisodeListViewHolder>() {

    private val episodeList: MutableList<Episode> = mutableListOf()

    fun updateData(newData: List<Episode>) {
        episodeList.clear()
        episodeList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeListViewHolder(
            parent.bindingInflate(R.layout.item_list_episode, false),
            listener
        )

    override fun getItemCount() = episodeList.size

    override fun onBindViewHolder(holder: EpisodeListViewHolder, position: Int) {
        holder.bind(episodeList[position])
    }

    class EpisodeListViewHolder(
        private val dataBinding: ItemListEpisodeBinding,
        private val listener: (Episode) -> Unit
    ): RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(item: Episode){
            dataBinding.episode = item
            itemView.setOnClickListener { listener(item) }
        }

    }
}
