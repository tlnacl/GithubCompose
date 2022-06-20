package com.tlnacl.githubcompose

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class TestDispatcherRule : TestWatcher() {

    private val testDispatcherProvider = object : DispatcherProvider {
        override fun default(): CoroutineDispatcher = StandardTestDispatcher()
        override fun io(): CoroutineDispatcher = StandardTestDispatcher()
        override fun main(): CoroutineDispatcher = UnconfinedTestDispatcher()
        override fun unconfined(): CoroutineDispatcher = UnconfinedTestDispatcher()
    }

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        AppCoroutineDispatcher.dispatcher = testDispatcherProvider
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}