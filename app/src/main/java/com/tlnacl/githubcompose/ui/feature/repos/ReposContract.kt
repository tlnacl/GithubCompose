package com.tlnacl.githubcompose.ui.feature.repos

import com.tlnacl.githubcompose.data.model.Repo
import com.tlnacl.githubcompose.data.model.User
import com.tlnacl.githubcompose.ui.ViewEvent
import com.tlnacl.githubcompose.ui.ViewSideEffect
import com.tlnacl.githubcompose.ui.ViewState

class ReposContract {

    sealed class Event : ViewEvent {
        object BackButtonClicked : Event()
    }

    data class State(
        val user: User?,
        val reposList: List<Repo>,
        val isUserLoading: Boolean,
        val isReposLoading: Boolean
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            object Back : Navigation()
        }
    }

}
