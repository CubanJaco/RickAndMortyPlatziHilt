package com.example.rickandmortyplatzihilt.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.rickandmortyplatzihilt.R
import com.example.rickandmortyplatzihilt.adapters.HomeNavigationStateAdapter
import com.example.rickandmortyplatzihilt.domain.Character
import com.example.rickandmortyplatzihilt.parcelables.toCharacterParcelable
import com.example.rickandmortyplatzihilt.utils.Constants
import com.example.rickandmortyplatzihilt.utils.startActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    CharacterListFragment.OnCharacterListFragmentListener,
    FavoriteListFragment.OnFavoriteListFragmentListener {

    //region Fields

    private val homeStatePageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewPager.currentItem = position
            when(position) {
                0 -> bottomNavigation.menu.findItem(R.id.navigation_list).isChecked = true
                1 -> bottomNavigation.menu.findItem(R.id.navigation_favorites).isChecked = true
            }
        }
    }

    //endregion

    //region Override Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation.setOnNavigationItemSelectedListener(this)

        viewPager.adapter = HomeNavigationStateAdapter(this)
        viewPager.registerOnPageChangeCallback(homeStatePageChangeCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.unregisterOnPageChangeCallback(homeStatePageChangeCallback)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.navigation_list -> {
                viewPager.currentItem = 0
                true
            }
            R.id.navigation_favorites -> {
                viewPager.currentItem = 1
                true
            }
            else -> false
        }
    }

    override fun openCharacterDetail(character: Character) {
        startActivity<CharacterDetailActivity> {
            putExtra(Constants.EXTRA_CHARACTER, character.toCharacterParcelable())
        }
        overridePendingTransition(R.anim.entry, R.anim.exit)
    }

    //endregion
}
