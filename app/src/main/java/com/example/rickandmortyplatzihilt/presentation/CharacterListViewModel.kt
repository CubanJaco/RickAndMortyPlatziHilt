package com.example.rickandmortyplatzihilt.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmortyplatzihilt.domain.Character
import com.example.rickandmortyplatzihilt.presentation.CharacterListViewModel.CharacterListNavigation.*
import com.example.rickandmortyplatzihilt.presentation.utils.Event
import com.example.rickandmortyplatzihilt.usescases.GetAllCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getAllCharactersUseCase: GetAllCharactersUseCase
): ViewModel() {

    //region Fields

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<CharacterListNavigation>>()
    val events: LiveData<Event<CharacterListNavigation>> get() = _events

    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false

    //endregion

    //region Override Methods & Callbacks

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    //endregion

    //region Public Methods

    fun onLoadMoreItems(visibleItemCount: Int, firstVisibleItemPosition: Int, totalItemCount: Int) {
        if (isLoading || isLastPage || !isInFooter(visibleItemCount, firstVisibleItemPosition, totalItemCount)) {
            return
        }

        currentPage += 1
        onGetAllCharacters()
    }

    fun onRetryGetAllCharacter(itemCount: Int) {
        if (itemCount > 0) {
            _events.value = Event(HideLoading)
            return
        }

        onGetAllCharacters()
    }

    fun onGetAllCharacters(){
        disposable.add(
            getAllCharactersUseCase
                .invoke(currentPage)
                .doOnSubscribe { showLoading() }
                .subscribe({ characterList ->
                    if (characterList.size < PAGE_SIZE) {
                        isLastPage = true
                    }

                    hideLoading()
                    _events.value = Event(ShowCharacterList(characterList))
                }, { error ->
                    isLastPage = true
                    hideLoading()
                    _events.value = Event(ShowCharacterError(error))
                })
        )
    }

    //endregion

    //region Private Methods

    private fun isInFooter(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ): Boolean {
        return visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE
    }

    private fun showLoading() {
        isLoading = true
        _events.value = Event(ShowLoading)
    }

    private fun hideLoading() {
        isLoading = false
        _events.value = Event(HideLoading)
    }

    //endregion

    //region Inner Classes & Interfaces

    sealed class CharacterListNavigation {
        data class ShowCharacterError(val error: Throwable) : CharacterListNavigation()
        data class ShowCharacterList(val characterList: List<Character>) : CharacterListNavigation()
        object HideLoading : CharacterListNavigation()
        object ShowLoading : CharacterListNavigation()
    }

    //endregion

    //region Companion Object

    companion object {

        private const val PAGE_SIZE = 20
    }

    //endregion

}
