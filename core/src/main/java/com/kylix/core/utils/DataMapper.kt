package com.kylix.core.utils

import com.kylix.core.data.source.local.entity.UserEntity
import com.kylix.core.data.source.remote.response.UserResponse
import com.kylix.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object DataMapper {
    fun mapResponsesToDomain(input: List<UserResponse>): Flow<List<User>> {
        val dataArray = ArrayList<User>()
        input.map {
            val user = User(
                it.id,
                it.login,
                it.url,
                it.avatarUrl,
                it.name,
                it.location,
                it.type,
                it.publicRepos,
                it.followers,
                it.following,
                false
            )
            dataArray.add(user)
        }
        return flowOf(dataArray)
    }

    fun mapResponseToDomain(input: UserResponse): Flow<User> {
        return flowOf(
            User(
                input.id,
                input.login,
                input.url,
                input.avatarUrl,
                input.name,
                input.location,
                input.type,
                input.publicRepos,
                input.followers,
                input.following,
                false
            )
        )
    }

    fun mapEntitiesToDomain(input: List<UserEntity>): List<User> =
        input.map { userEntity ->
            User(
                userEntity.id,
                userEntity.login,
                userEntity.url,
                userEntity.avatarUrl,
                userEntity.name,
                userEntity.location,
                userEntity.type,
                userEntity.publicRepos,
                userEntity.followers,
                userEntity.following,
                userEntity.isFavorite
            )
        }

    fun mapEntityToDomain(input: UserEntity?): User {
        return User(
            input?.id,
            input?.login,
            input?.url,
            input?.avatarUrl,
            input?.name,
            input?.location,
            input?.type,
            input?.publicRepos,
            input?.followers,
            input?.following,
            input?.isFavorite
        )
    }


    fun mapDomainToEntity(input: User) = UserEntity(
        input.id,
        input.login,
        input.url,
        input.avatarUrl,
        input.name,
        input.location,
        input.type,
        input.publicRepos,
        input.followers,
        input.following,
        input.isFavorite
    )
}