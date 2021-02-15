package com.kylix.submissionmade2.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kylix.core.data.Resource
import com.kylix.core.domain.model.User
import com.kylix.submissionmade2.R
import com.kylix.submissionmade2.databinding.DetailFragmentBinding
import com.kylix.submissionmade2.follow.FollowFragment
import com.shashank.sony.fancytoastlib.FancyToast
import org.koin.android.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private var _detailBinding: DetailFragmentBinding? = null
    private val detailBinding get() = _detailBinding!!
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var user: User
    private var isFavorite = false
    private val args: DetailFragmentArgs by navArgs()
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = args.Username
        _detailBinding = DetailFragmentBinding.inflate(layoutInflater, container, false)
        detailBinding.lifecycleOwner = viewLifecycleOwner
        val view = detailBinding.root
        observeDetail()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabList = arrayOf(resources.getString(R.string.followers), resources.getString(R.string.following))
        pagerAdapter = PagerAdapter(tabList, args.Username, this)
        detailBinding.pager.adapter = pagerAdapter

        detailBinding.tabs.let {
            TabLayoutMediator(it, detailBinding.pager) { tab, position ->
                tab.text = tabList[position]
            }.attach()
        }
    }

    private fun observeDetail() {

        detailViewModel.detailUsers(args.Username).observe(viewLifecycleOwner, {
            when(it) {
                is Resource.Success -> {
                    user = it.data!!
                    detailBinding.data = it.data
                    detailViewModel.getDetailState(args.Username)?.observe(viewLifecycleOwner, { user ->
                        isFavorite = user.isFavorite == true
                        changedFavorite(isFavorite)
                    })
                    detailBinding.fabFavorite.show()
                }

                is Resource.Error -> {
                    detailBinding.fabFavorite.hide()
                }

                is Resource.Loading -> {
                    detailBinding.fabFavorite.hide()
                }
            }
            changedFavorite(isFavorite)
            detailBinding.fabFavorite.setOnClickListener {
                addOrRemoveFavorite()
                changedFavorite(isFavorite)
            }
        })
    }

    private fun addOrRemoveFavorite() {
        if (!isFavorite) {
            user.isFavorite = !isFavorite
            detailViewModel.insertFavorite(user)
            FancyToast.makeText(
                context, resources.getString(R.string.favorite_add, user.login), Toast.LENGTH_SHORT, FancyToast.SUCCESS, false
            ).show()
            isFavorite = !isFavorite
        } else {
            user.isFavorite = !isFavorite
            detailViewModel.deleteFavorite(user)
            FancyToast.makeText(
                context, resources.getString(R.string.favorite_remove, user.login), Toast.LENGTH_SHORT, FancyToast.ERROR, false
            ).show()
            isFavorite = !isFavorite
        }
    }

    private fun changedFavorite(statusFavorite: Boolean) {
        if (statusFavorite){
            detailBinding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        }
        else {
            detailBinding.fabFavorite.setImageResource(R.drawable.ic_unfavorite)
        }
    }

    inner class PagerAdapter(
        private val tabList: Array<String>,
        private val username: String,
        fragment: Fragment
    ) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = tabList.size

        override fun createFragment(position: Int): Fragment =
            FollowFragment.newInstance(username, tabList[position])
    }

    override fun onDestroyView() {
        _detailBinding = null
        super.onDestroyView()
    }
}