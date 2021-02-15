package com.kylix.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int?,
    val login: String?,
    val url: String?,
    val avatarUrl: String?,
    val name: String?,
    val location: String?,
    val type: String?,
    val publicRepos: Int?,
    val followers: Int?,
    val following: Int?,
    var isFavorite: Boolean?
): Parcelable
