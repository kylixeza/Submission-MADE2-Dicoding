package com.kylix.submissionmade2.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kylix.core.data.Resource
import com.kylix.core.ui.UserAdapter
import com.kylix.submissionmade2.R
import com.kylix.submissionmade2.databinding.FavoriteFragmentBinding
import com.kylix.submissionmade2.databinding.FollowFragmentBinding
import com.kylix.submissionmade2.databinding.HomeFragmentBinding
import com.kylix.submissionmade2.util.ShowState
import org.koin.android.ext.android.getKoin
import org.koin.android.viewmodel.ViewModelParameter
import org.koin.android.viewmodel.koin.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

class HomeFragment : Fragment(), ShowState {

    private inline fun<reified VM: ViewModel> Fragment.sharedGraphViewModel (
        @IdRes navGraphId: Int,
        qualifier: Qualifier? = null,
        noinline parameters: ParametersDefinition?= null
    ) = lazy {
        val store = findNavController().getViewModelStoreOwner(navGraphId).viewModelStore
        getKoin().getViewModel(ViewModelParameter(VM::class, qualifier, parameters, store))
    }

    private var _homeBinding: HomeFragmentBinding? = null
    private val homeBinding get() = _homeBinding!!
    private lateinit var homeAdapter: UserAdapter
    private val homeViewModel: HomeViewModel by sharedGraphViewModel(R.id.user_navigation)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.app_name)
        _homeBinding = HomeFragmentBinding.inflate(layoutInflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        homeBinding.errLayout.emptyText.text = getString(R.string.search_hint)

        homeAdapter = UserAdapter(arrayListOf()) {username, iv ->
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(username),
                FragmentNavigatorExtras(iv to username)
            )
        }

        homeBinding.recyclerHome.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }

        homeBinding.searchView.apply {
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    homeViewModel.setSearch(query)
                    homeBinding.searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean = false
            })
        }
        observeHome()
    }

    private fun observeHome() {
        homeViewModel.users.observe(viewLifecycleOwner, {
            if (it != null) {
                when(it) {
                    is Resource.Success -> {
                        onSuccessState(homeFragmentBinding = homeBinding)
                        it.data?.let { data -> homeAdapter.setData(data) }
                    }
                    is Resource.Loading -> onLoadingState(homeFragmentBinding = homeBinding)
                    is Resource.Error -> onErrorState(homeFragmentBinding = homeBinding, message = it.message)
                }
            }
        })
    }

    override fun onSuccessState(
        homeFragmentBinding: HomeFragmentBinding?,
        followFragmentBinding: FollowFragmentBinding?,
        favoriteFragmentBinding: FavoriteFragmentBinding?
    ) {
        homeFragmentBinding?.apply {
            errLayout.mainNotFound.visibility = View.GONE
            progress.visibility = View.GONE
            recyclerHome.visibility = View.VISIBLE
        }
    }

    override fun onLoadingState(
        homeFragmentBinding: HomeFragmentBinding?,
        followFragmentBinding: FollowFragmentBinding?,
        favoriteFragmentBinding: FavoriteFragmentBinding?
    ) {
        homeFragmentBinding?.apply {
            errLayout.mainNotFound.visibility = View.GONE
            progress.visibility = View.VISIBLE
            recyclerHome.visibility = View.GONE
        }
    }

    override fun onErrorState(
        homeFragmentBinding: HomeFragmentBinding?,
        followFragmentBinding: FollowFragmentBinding?,
        favoriteFragmentBinding: FavoriteFragmentBinding?,
        message: String?
    ) {
        homeFragmentBinding?.apply {
            errLayout.apply {
                mainNotFound.visibility = View.VISIBLE
                if (message == null) {
                    emptyText.text = resources.getString(R.string.not_found)
                    ivSearch.setImageResource(R.drawable.ic_search_reset)
                } else {
                    emptyText.text = message
                    ivSearch.setImageResource(R.drawable.ic_search_off)
                }
            }
            progress.visibility = View.GONE
            recyclerHome.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        homeBinding.recyclerHome.adapter = null
        _homeBinding = null
        super.onDestroyView()
    }
}