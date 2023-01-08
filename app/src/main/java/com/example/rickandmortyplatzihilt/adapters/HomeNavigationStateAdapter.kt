package com.example.rickandmortyplatzihilt.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rickandmortyplatzihilt.ui.CharacterListFragment
import com.example.rickandmortyplatzihilt.ui.FavoriteListFragment

class HomeNavigationStateAdapter(
    activity: AppCompatActivity
) : FragmentStateAdapter(activity) {

    //region Fields

    private val homeStateFragmentList: List<Fragment> = listOf(
        CharacterListFragment.newInstance(),
        FavoriteListFragment.newInstance()
    )

    //endregion

    //region Override Methods & Callbacks

    override fun getItemCount(): Int = homeStateFragmentList.size

    override fun createFragment(position: Int): Fragment = homeStateFragmentList[position]

    //endregion
}
