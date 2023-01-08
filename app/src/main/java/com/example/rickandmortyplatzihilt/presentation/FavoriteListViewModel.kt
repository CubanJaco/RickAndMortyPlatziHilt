package com.example.rickandmortyplatzihilt.presentation

import com.example.rickandmortyplatzihilt.domain.Character
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmortyplatzihilt.presentation.FavoriteListViewModel.FavoriteListNavigation.ShowCharacterList
import com.example.rickandmortyplatzihilt.presentation.FavoriteListViewModel.FavoriteListNavigation.ShowEmptyListMessage
import com.example.rickandmortyplatzihilt.presentation.utils.Event
import com.example.rickandmortyplatzihilt.usescases.GetAllFavoriteCharactersUseCase
import io.reactivex.disposables.CompositeDisposable

class FavoriteListViewModel (
    private val getAllFavoriteCharactersUseCase: GetAllFavoriteCharactersUseCase
) : ViewModel(){

    //region Fields

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<FavoriteListNavigation>>()
    val events: LiveData<Event<FavoriteListNavigation>> get() = _events

    val favoriteCharacterList: LiveData<List<Character>>
        get() = LiveDataReactiveStreams.fromPublisher(getAllFavoriteCharactersUseCase.invoke())

    //endregion

    //region Override Methods & Callbacks

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    //endregion

    //region Public Methods

    fun onFavoriteCharacterList(favoriteCharacterList: List<Character>) {
        if (favoriteCharacterList.isEmpty()) {
            _events.value = Event(ShowCharacterList(emptyList()))
            _events.value = Event(ShowEmptyListMessage)
            return
        }

        _events.value = Event(ShowCharacterList(favoriteCharacterList))
    }

    //endregion

    //region Inner Classes & Interfaces

    sealed class FavoriteListNavigation {
        data class ShowCharacterList(val characterList: List<Character>) : FavoriteListNavigation()
        object ShowEmptyListMessage : FavoriteListNavigation()
    }

    //endregion

}
