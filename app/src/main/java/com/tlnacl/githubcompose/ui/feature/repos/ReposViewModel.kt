package com.tlnacl.githubcompose.ui.feature.repos

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tlnacl.githubcompose.data.GithubRepository
import com.tlnacl.githubcompose.ui.BaseViewModel
import com.tlnacl.githubcompose.ui.navigation.Navigation.Args.USER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReposViewModel @Inject constructor(
    private val githubRepository: GithubRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ReposContract.Event, ReposContract.State, ReposContract.Effect>() {

    init {
        val userLogin: String? = savedStateHandle[USER_ID]
        Timber.d("userLogin: $userLogin")
        viewModelScope.launch {
            getUser(userLogin!!) // TODO null check
            getRepos(userLogin)
        }
    }

    override fun setInitialState() = ReposContract.State(
        user = null,
        reposList = emptyList(),
        isUserLoading = true,
        isReposLoading = true
    )

    override fun handleEvents(event: ReposContract.Event) {
        when (event) {
            ReposContract.Event.BackButtonClicked -> {
                setEffect {
                    ReposContract.Effect.Navigation.Back
                }
            }
        }
    }

    private suspend fun getUser(userLogin: String) {
        val user = githubRepository.getUser(userLogin)
        setState {
            copy(user = user, isUserLoading = false)
        }
    }

    private suspend fun getRepos(userLogin: String) {
        val repos = githubRepository.getRepos(userLogin)
        setState {
            copy(reposList = repos, isReposLoading = false)
        }
    }

}