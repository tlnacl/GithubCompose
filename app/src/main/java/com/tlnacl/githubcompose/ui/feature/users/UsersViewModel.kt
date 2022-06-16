package com.tlnacl.githubcompose.ui.feature.users

import androidx.lifecycle.viewModelScope
import com.tlnacl.githubcompose.data.GithubRepository
import com.tlnacl.githubcompose.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val githubRepository: GithubRepository) :
    BaseViewModel<UsersContract.Event, UsersContract.State, UsersContract.Effect>() {

    init {
        getUsers()
    }

    override fun setInitialState() = UsersContract.State(
        users = emptyList(),
        isLoading = true
    )

    override fun handleEvents(event: UsersContract.Event) {
        when (event) {
            is UsersContract.Event.UserSelection -> {
                setEffect {
                    UsersContract.Effect.Navigation.ToRepos(event.user.login)
                }
            }
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            val users = githubRepository.getUsers()
            setState {
                copy(users = users, isLoading = false)
            }
            setEffect {
                UsersContract.Effect.DataWasLoaded
            }
        }
    }
}
