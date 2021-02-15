package com.kylix.submissionmade2.follow

import androidx.lifecycle.*
import com.kylix.core.data.Resource
import com.kylix.core.domain.model.User
import com.kylix.core.domain.usecase.UserUseCase
import com.kylix.submissionmade2.util.TypeView

class FollowViewModel(userUseCase: UserUseCase) : ViewModel() {
    private var username: MutableLiveData<String> = MutableLiveData()
    private lateinit var typeView: TypeView

    fun setFollow(user: String, type: TypeView) {
        if (username.value == user) {
            return
        }
        username.value = user
        typeView = type
    }

    val favoriteUsers: LiveData<Resource<List<User>>> = Transformations
        .switchMap(username) {
            when (typeView) {
                TypeView.FOLLOWER -> userUseCase.getAllFollowers(it).asLiveData()
                TypeView.FOLLOWING -> userUseCase.getAllFollowing(it).asLiveData()
            }
        }
}