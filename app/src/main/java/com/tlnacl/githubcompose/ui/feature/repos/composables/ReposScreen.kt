package com.tlnacl.githubcompose.ui.feature.repos.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tlnacl.githubcompose.ui.SIDE_EFFECTS_KEY
import com.tlnacl.githubcompose.ui.feature.common.Progress
import com.tlnacl.githubcompose.ui.feature.repos.ReposContract
import com.tlnacl.githubcompose.ui.feature.repos.ReposViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun ReposScreen(navigateUp: () -> Unit) {
    val viewModel = hiltViewModel<ReposViewModel>()

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        viewModel.effect.onEach { effect ->
            when (effect) {
                ReposContract.Effect.Navigation.Back -> navigateUp()
            }
        }.collect()
    }

    Scaffold(
        topBar = {
            ReposTopBar {
                viewModel.setEvent(ReposContract.Event.BackButtonClicked)
            }
        }
    ) {
        if (viewModel.viewState.value.isUserLoading || viewModel.viewState.value.isReposLoading) {
            Progress()
        } else {
            viewModel.viewState.value.user?.let { user ->
                Column(modifier = Modifier.fillMaxSize()) {
                    ReposList(
                        header = { ReposListHeader(user = user) },
                        reposList = viewModel.viewState.value.reposList
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReposScreenPreview() {
    ReposScreen {}
}
