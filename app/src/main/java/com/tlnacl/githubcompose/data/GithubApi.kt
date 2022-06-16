package com.tlnacl.githubcompose.data

import com.tlnacl.githubcompose.data.model.Repo
import com.tlnacl.githubcompose.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{userLogin}")
    suspend fun getUser(@Path("userLogin") userLogin: String): User

    @GET("users/{userLogin}/repos")
    suspend fun getRepos(@Path("userLogin") userLogin: String): List<Repo>

}