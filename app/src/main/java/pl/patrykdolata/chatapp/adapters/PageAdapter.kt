package pl.patrykdolata.chatapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

data class FragmentWithTitle(val fragment: Fragment, val title: String)

class PageAdapter(fragmentManager: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fragmentManager, behavior) {

    private val fragments: ArrayList<FragmentWithTitle> = ArrayList()

    fun addFragment(fragment: Fragment, title: String) {
        fragments += FragmentWithTitle(fragment, title)
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position].fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragments[position].title
    }

    override fun getCount(): Int {
        return fragments.size
    }
}