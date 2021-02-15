package com.kylix.core.data.source.local

import com.kylix.core.data.source.local.entity.UserEntity
import com.kylix.core.data.source.local.room.UserDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val userDao: UserDao) {

    fun getFavoriteUser(): Flow<List<UserEntity>> = userDao.getFavoriteUser()

    //private fun getDetailUser(username: String): Flow<UserEntity> = userDao.getDetailUser(username)

    fun getDetailState(username: String): Flow<UserEntity>? = userDao.getDetailState(username)

    suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    suspend fun deleteUser(user: UserEntity) = userDao.deleteUser(user)
}