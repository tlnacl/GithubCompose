package com.tlnacl.githubcompose.ui.feature.users.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.tlnacl.githubcompose.ui.SIDE_EFFECTS_KEY
import com.tlnacl.githubcompose.ui.feature.common.Progress
import com.tlnacl.githubcompose.ui.feature.users.UsersContract
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import com.tlnacl.githubcompose.ui.feature.users.UsersViewModel
import com.tlnacl.githubcompose.R

@Composable
fun UsersScreen(showRepo:(userId:String) -> Unit) {
    val viewModel = hiltViewModel<UsersViewModel>()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val snackbarMessage = stringResource(R.string.users_screen_snackbar_loaded_message)

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is UsersContract.Effect.DataWasLoaded -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = snackbarMessage,
                        duration = SnackbarDuration.Short
                    )
                }
                is UsersContract.Effect.Navigation.ToRepos -> showRepo(effect.userId)
            }
        }.collect()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { UsersTopBar() }
    ) {
        if (viewModel.viewState.value.isLoading) {
            Progress()
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                UsersList(users = viewModel.viewState.value.users) { user ->
                    viewModel.setEvent(UsersContract.Event.UserSelection(user))
                }
            }
        }
    }
}
