package com.example.rickandmortyplatzihilt.ui

import com.example.rickandmortyplatzihilt.domain.Character
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.rickandmortyplatzihilt.R
import com.example.rickandmortyplatzihilt.adapters.EpisodeListAdapter
import com.example.rickandmortyplatzihilt.data.datasources.LocalCharacterDataSource
import com.example.rickandmortyplatzihilt.data.datasources.RemoteCharacterDataSource
import com.example.rickandmortyplatzihilt.data.datasources.RemoteEpisodeDataSource
import com.example.rickandmortyplatzihilt.data.repositories.CharacterRepositoryImpl
import com.example.rickandmortyplatzihilt.data.repositories.EpisodeRepositoryImpl
import com.example.rickandmortyplatzihilt.databasemanager.CharacterDatabase
import com.example.rickandmortyplatzihilt.databasemanager.CharacterRoomDataSource
import com.example.rickandmortyplatzihilt.databinding.ActivityCharacterDetailBinding
import com.example.rickandmortyplatzihilt.imagemanager.bindCircularImageUrl
import com.example.rickandmortyplatzihilt.parcelables.CharacterParcelable
import com.example.rickandmortyplatzihilt.parcelables.toCharacterDomain
import com.example.rickandmortyplatzihilt.presentation.CharacterDetailViewModel
import com.example.rickandmortyplatzihilt.presentation.CharacterDetailViewModel.CharacterDetailNavigation
import com.example.rickandmortyplatzihilt.presentation.CharacterDetailViewModel.CharacterDetailNavigation.*
import com.example.rickandmortyplatzihilt.presentation.utils.Event
import com.example.rickandmortyplatzihilt.requestmanager.APIConstants.BASE_API_URL
import com.example.rickandmortyplatzihilt.requestmanager.CharacterRequest
import com.example.rickandmortyplatzihilt.requestmanager.CharacterRetrofitDataSource
import com.example.rickandmortyplatzihilt.requestmanager.EpisodeRequest
import com.example.rickandmortyplatzihilt.requestmanager.EpisodeRetrofitDataSource
import com.example.rickandmortyplatzihilt.usescases.GetEpisodeFromCharacterUseCase
import com.example.rickandmortyplatzihilt.usescases.GetFavoriteCharacterStatusUseCase
import com.example.rickandmortyplatzihilt.usescases.UpdateFavoriteCharacterStatusUseCase
import com.example.rickandmortyplatzihilt.utils.Constants
import com.example.rickandmortyplatzihilt.utils.getViewModel
import com.example.rickandmortyplatzihilt.utils.showLongToast
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity: AppCompatActivity() {

    //region Fields

    private lateinit var episodeListAdapter: EpisodeListAdapter
    private lateinit var binding: ActivityCharacterDetailBinding

    private val episodeRequest: EpisodeRequest by lazy {
        EpisodeRequest(BASE_API_URL)
    }

    private val characterRequest: CharacterRequest by lazy {
        CharacterRequest(BASE_API_URL)
    }

    private val localCharacterDataSource: LocalCharacterDataSource by lazy {
        CharacterRoomDataSource(CharacterDatabase.getDatabase(applicationContext))
    }

    private val remoteCharacterDataSource: RemoteCharacterDataSource by lazy {
        CharacterRetrofitDataSource(characterRequest)
    }

    private val characterRepository: CharacterRepositoryImpl by lazy {
        CharacterRepositoryImpl(remoteCharacterDataSource, localCharacterDataSource)
    }

    private val remoteEpisodeDataSource: RemoteEpisodeDataSource by lazy {
        EpisodeRetrofitDataSource(episodeRequest)
    }

    private val episodeRepository: EpisodeRepositoryImpl by lazy {
        EpisodeRepositoryImpl(remoteEpisodeDataSource)
    }

    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase by lazy {
        GetEpisodeFromCharacterUseCase(episodeRepository)
    }

    private val getFavoriteCharacterStatusUseCase: GetFavoriteCharacterStatusUseCase by lazy {
        GetFavoriteCharacterStatusUseCase(characterRepository)
    }

    private val updateFavoriteCharacterStatusUseCase: UpdateFavoriteCharacterStatusUseCase by lazy {
        UpdateFavoriteCharacterStatusUseCase(characterRepository)
    }

    private val characterDetailViewModel: CharacterDetailViewModel by lazy {
        getViewModel {
            CharacterDetailViewModel(
                intent.getParcelableExtra<CharacterParcelable>(Constants.EXTRA_CHARACTER)?.toCharacterDomain(),
                getEpisodeFromCharacterUseCase,
                getFavoriteCharacterStatusUseCase,
                updateFavoriteCharacterStatusUseCase
            )
        }
    }

    //endregion

    //region Override Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detail)
        binding.lifecycleOwner = this@CharacterDetailActivity

        episodeListAdapter = EpisodeListAdapter { episode ->
            this@CharacterDetailActivity.showLongToast("Episode -> $episode")
        }
        rvEpisodeList.adapter = episodeListAdapter

        characterFavorite.setOnClickListener { characterDetailViewModel.onUpdateFavoriteCharacterStatus() }

        characterDetailViewModel.characterValues.observe(this, Observer(this::loadCharacter))
        characterDetailViewModel.isFavorite.observe(this, Observer(this::updateFavoriteIcon))
        characterDetailViewModel.events.observe(this, Observer(this::validateEvents))

        characterDetailViewModel.onCharacterValidation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    //endregion

    //region Private Methods

    private fun loadCharacter(character: Character){
        binding.characterImage.bindCircularImageUrl(
            url = character.image,
            placeholder = R.drawable.ic_camera_alt_black,
            errorPlaceholder = R.drawable.ic_broken_image_black
        )
        binding.characterDataName = character.name
        binding.characterDataStatus = character.status
        binding.characterDataSpecies = character.species
        binding.characterDataGender = character.gender
        binding.characterDataOriginName = character.origin.name
        binding.characterDataLocationName = character.location.name
    }

    private fun updateFavoriteIcon(isFavorite: Boolean?){
        characterFavorite.setImageResource(
            if (isFavorite != null && isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_border
            }
        )
    }

    private fun validateEvents(event: Event<CharacterDetailNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ShowEpisodeError -> navigation.run {
                    this@CharacterDetailActivity.showLongToast("Error -> ${error.message}")
                }
                is ShowEpisodeList -> navigation.run {
                    episodeListAdapter.updateData(episodeList)
                }
                CloseActivity -> {
                    this@CharacterDetailActivity.showLongToast(R.string.error_no_character_data)
                    finish()
                }
                HideEpisodeListLoading -> {
                    episodeProgressBar.isVisible = false
                }
                ShowEpisodeListLoading -> {
                    episodeProgressBar.isVisible = true
                }
            }
        }
    }

    //endregion
}
