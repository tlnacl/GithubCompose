package com.tlnacl.githubcompose.ui.feature.users

import com.tlnacl.githubcompose.data.model.User
import com.tlnacl.githubcompose.ui.ViewEvent
import com.tlnacl.githubcompose.ui.ViewSideEffect
import com.tlnacl.githubcompose.ui.ViewState

class UsersContract {
    sealed class Event : ViewEvent {
        data class UserSelection(val user: User) : Event()
    }

    data class State(
        val users: List<User> = emptyList(),
        val isLoading: Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        object DataWasLoaded : Effect()

        sealed class Navigation : Effect() {
            data class ToRepos(val userId: String): Navigation()
        }
    }

}