package com.example.rickandmortyplatzihilt.ui

import com.example.rickandmortyplatzihilt.domain.Character
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.rickandmortyplatzihilt.R
import com.example.rickandmortyplatzihilt.adapters.FavoriteListAdapter
import com.example.rickandmortyplatzihilt.data.datasources.LocalCharacterDataSource
import com.example.rickandmortyplatzihilt.data.datasources.RemoteCharacterDataSource
import com.example.rickandmortyplatzihilt.data.repositories.CharacterRepositoryImpl
import com.example.rickandmortyplatzihilt.databasemanager.CharacterDatabase
import com.example.rickandmortyplatzihilt.databasemanager.CharacterRoomDataSource
import com.example.rickandmortyplatzihilt.databinding.FragmentFavoriteListBinding
import com.example.rickandmortyplatzihilt.presentation.FavoriteListViewModel
import com.example.rickandmortyplatzihilt.presentation.FavoriteListViewModel.FavoriteListNavigation
import com.example.rickandmortyplatzihilt.presentation.FavoriteListViewModel.FavoriteListNavigation.*
import com.example.rickandmortyplatzihilt.presentation.utils.Event
import com.example.rickandmortyplatzihilt.requestmanager.APIConstants.BASE_API_URL
import com.example.rickandmortyplatzihilt.requestmanager.CharacterRequest
import com.example.rickandmortyplatzihilt.requestmanager.CharacterRetrofitDataSource
import com.example.rickandmortyplatzihilt.usescases.GetAllFavoriteCharactersUseCase
import com.example.rickandmortyplatzihilt.utils.getViewModel
import com.example.rickandmortyplatzihilt.utils.setItemDecorationSpacing
import kotlinx.android.synthetic.main.fragment_favorite_list.*

class FavoriteListFragment : Fragment() {

    //region Fields

    private lateinit var favoriteListAdapter: FavoriteListAdapter
    private lateinit var listener: OnFavoriteListFragmentListener

    private val characterRequest: CharacterRequest by lazy {
        CharacterRequest(BASE_API_URL)
    }

    private val localCharacterDataSource: LocalCharacterDataSource by lazy {
        CharacterRoomDataSource(CharacterDatabase.getDatabase(activity!!.applicationContext))
    }

    private val remoteCharacterDataSource: RemoteCharacterDataSource by lazy {
        CharacterRetrofitDataSource(characterRequest)
    }

    private val characterRepository: CharacterRepositoryImpl by lazy {
        CharacterRepositoryImpl(remoteCharacterDataSource, localCharacterDataSource)
    }

    private val getAllFavoriteCharactersUseCase: GetAllFavoriteCharactersUseCase by lazy {
        GetAllFavoriteCharactersUseCase(characterRepository)
    }

    private val favoriteListViewModel: FavoriteListViewModel by lazy {
        getViewModel { FavoriteListViewModel(getAllFavoriteCharactersUseCase) }
    }

    //endregion

    //region Override Methods & Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as OnFavoriteListFragmentListener
        }catch (e: ClassCastException){
            throw ClassCastException("$context must implement OnFavoriteListFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentFavoriteListBinding>(
            inflater,
            R.layout.fragment_favorite_list,
            container,
            false
        ).apply {
            lifecycleOwner = this@FavoriteListFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteListAdapter = FavoriteListAdapter { character ->
            listener.openCharacterDetail(character)
        }
        favoriteListAdapter.setHasStableIds(true)

        rvFavoriteList.run {
            setItemDecorationSpacing(resources.getDimension(R.dimen.list_item_padding))
            adapter = favoriteListAdapter
        }

        favoriteListViewModel.favoriteCharacterList.observe(this, Observer(favoriteListViewModel::onFavoriteCharacterList))
        favoriteListViewModel.events.observe(this, Observer(this::validateEvents))
    }

    //endregion

    //region Private Methods

    private fun validateEvents(event: Event<FavoriteListNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ShowCharacterList -> navigation.run {
                    tvEmptyListMessage.isVisible = false
                    favoriteListAdapter.updateData(characterList)
                }
                ShowEmptyListMessage -> {
                    tvEmptyListMessage.isVisible = true
                    favoriteListAdapter.updateData(emptyList())
                }
            }
        }
    }

    //endregion

    //region Inner Classes & Interfaces

    interface OnFavoriteListFragmentListener {
        fun openCharacterDetail(character: Character)
    }

    //endregion

    //region Companion object

    companion object {

        fun newInstance(args: Bundle? = Bundle()) = FavoriteListFragment().apply {
            arguments = args
        }
    }

    //endregion

}
