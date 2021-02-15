package com.kylix.core.data

import com.kylix.core.data.source.local.LocalDataSource
import com.kylix.core.data.source.remote.RemoteDataSource
import com.kylix.core.data.source.remote.network.ApiResponse
import com.kylix.core.data.source.remote.response.UserResponse
import com.kylix.core.domain.model.User
import com.kylix.core.domain.repository.IUserRepository
import com.kylix.core.utils.DataMapper
import kotlinx.coroutines.flow.*

class UserRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): IUserRepository {

    override fun getAllUsers(query: String?): Flow<Resource<List<User>>> {
        return object : NetworkOnlyResource<List<User>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> {
               return remoteDataSource.getAllUsers(query)
            }

        }.asFlow()
    }

    override fun getAllFollowers(username: String): Flow<Resource<List<User>>> {
        return object : NetworkOnlyResource<List<User>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> {
                return remoteDataSource.getAllFollowers(username)
            }

        }.asFlow()
    }

    override fun getAllFollowing(username: String): Flow<Resource<List<User>>> {
        return object : NetworkOnlyResource<List<User>, List<UserResponse>>() {
            override fun loadFromNetwork(data: List<UserResponse>): Flow<List<User>> {
                return DataMapper.mapResponsesToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> {
                return remoteDataSource.getAllFollowing(username)
            }
        }.asFlow()
    }

    override fun getDetailUser(username: String): Flow<Resource<User>> {
        return object : NetworkOnlyResource<User, UserResponse>() {
            override fun loadFromNetwork(data: UserResponse): Flow<User> {
                return DataMapper.mapResponseToDomain(data)
            }

            override suspend fun createCall(): Flow<ApiResponse<UserResponse>> {
                return remoteDataSource.getUserDetail(username)
            }

        }.asFlow()
    }

    override fun getFavoriteUser(): Flow<List<User>> {
        return localDataSource.getFavoriteUser().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun getDetailState(username: String): Flow<User>? {
        return localDataSource.getDetailState(username)?.map {
            DataMapper.mapEntityToDomain(it)
        }
    }


    override suspend fun insertUser(user: User) {
        val domainUser = DataMapper.mapDomainToEntity(user)
        return localDataSource.insertUser(domainUser)
    }

    override suspend fun deleteUser(user: User): Int {
        val domainUser = DataMapper.mapDomainToEntity(user)
        return localDataSource.deleteUser(domainUser)
    }
}