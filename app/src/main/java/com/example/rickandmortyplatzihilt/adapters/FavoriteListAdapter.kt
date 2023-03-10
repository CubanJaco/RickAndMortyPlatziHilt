package com.example.rickandmortyplatzihilt.adapters

import com.example.rickandmortyplatzihilt.domain.Character
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyplatzihilt.R
import com.example.rickandmortyplatzihilt.databinding.ItemGridFavoriteCharacterBinding
import com.example.rickandmortyplatzihilt.imagemanager.bindImageUrl
import com.example.rickandmortyplatzihilt.utils.bindingInflate
import kotlinx.android.synthetic.main.item_grid_favorite_character.view.*


class FavoriteListAdapter(
    private val listener: (Character) -> Unit
): RecyclerView.Adapter<FavoriteListAdapter.FavoriteListViewHolder>() {

    private val characterList: MutableList<Character> = mutableListOf()

    fun updateData(newData: List<Character>) {
        characterList.clear()
        characterList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavoriteListViewHolder(
            parent.bindingInflate(R.layout.item_grid_favorite_character, false),
            listener
        )

    override fun getItemCount() = characterList.size

    override fun getItemId(position: Int): Long = characterList[position].id.toLong()

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        holder.bind(characterList[position])
    }

    class FavoriteListViewHolder(
        private val dataBinding: ItemGridFavoriteCharacterBinding,
        private val listener: (Character) -> Unit
    ): RecyclerView.ViewHolder(dataBinding.root) {

        //region Public Methods

        fun bind(item: Character){
            dataBinding.character = item
            itemView.character_image.bindImageUrl(
                url = item.image,
                placeholder = R.drawable.ic_camera_alt_black,
                errorPlaceholder = R.drawable.ic_broken_image_black
            )
            itemView.setOnClickListener { listener(item) }
        }

        //endregion
    }
}
