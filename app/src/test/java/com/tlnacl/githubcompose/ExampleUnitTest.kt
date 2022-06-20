package com.tlnacl.githubcompose

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import kotlin.system.measureTimeMillis

/**
 * practice 1.6 coroutine test
 */
@ExperimentalCoroutinesApi
class ExampleUnitTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private suspend fun fetchData(): String {
        delay(1000)
        return "Hello world"
    }

    // If your code moves the coroutine execution to other dispatchers (for example, by using withContext), runTest will still generally work, but delays will no longer be skipped
    private suspend fun fetchDataInIo(): String = withContext(Dispatchers.IO) {
        delay(1000)
        "Hello world"
    }

    @Test
    fun dataIsHelloWorld() = runTest { // runTest will automatically skip any delays in coroutines,
        val time = measureTimeMillis {
            val data = fetchData()
            assertEquals("Hello world", data)
        }
        println("##### Spend $time ms")
    }


    class UserRepo() {
        private val users = mutableListOf<String>()
        suspend fun addUser(user: String) {
            delay(1000)
            users.add(user)
        }

        suspend fun getAllUsers(): List<String> {
            delay(1000)
            return users.toList()
        }

    }

    // StandardTestDispatcher vs UnconfinedTestDispatcher
    // runTest is using StandardTestDispatcher by default
    // StandardTestDispatcher queued up on the underlying scheduler, to be run whenever the test thread is free to use.
    // To let these new coroutines run, you need to yield the test thread (free it up for other coroutines to use).
    // This queueing behavior gives you precise control over how new coroutines run during the test, and
    // it resembles the scheduling of coroutines in production code. Use it when ever possible
    @Test
    fun standardTestDispatcherTest() = runTest {
        val repo = UserRepo()

        launch { repo.addUser("a") }
        launch { repo.addUser("b") }
//        advanceUntilIdle() //

        assertEquals(listOf("a", "b"), repo.getAllUsers())
    }

    // UnconfinedTestDispatcher started eagerly on the current thread. This means that they’ll start running immediately, without waiting for their coroutine builder to return
    // this behavior is different from what you’ll see in production with non-test dispatchers. If your test focuses on concurrency, prefer using StandardTestDispatcher instead.
    @Test
    fun unconfinedTestDispatcherTest() = runTest(UnconfinedTestDispatcher()) {
        val repo = UserRepo()

        launch { repo.addUser("a") }
        launch { repo.addUser("b") }

        assertEquals(listOf("a", "b"), repo.getAllUsers())
    }

    // Test timing No activity 1 sec we will logout
    // Pre 1.6.0, advanceTimBy(n) would have run a task scheduled at currentTime + n,
    // but now it will stop just before executing any task starting at the next millisecond.
    // So now you have to advance time by another ms
    class LoginStatusRepo {
        fun getLoginStatus() = flow {
            emit("LoggedIn")
            delay(1000)
            emit("LoggedOut")
        }
    }

    @Test
    fun autoLogoutTest() = runTest {
        val repo = LoginStatusRepo()
        var loginStatus = "LoggedIn"
        launch {
            repo.getLoginStatus().collect {
                loginStatus = it
            }
        }

        assertEquals("LoggedIn", loginStatus)
        advanceTimeBy(1000)
//        advanceTimeBy(1001)
        assertEquals("LoggedOut", loginStatus)

    }


    // We should use own dispatch instead of Dispatchers.IO directly, due to we can't substitute it
    private suspend fun fetchDataInWithProvider(): String =
        withContext(AppCoroutineDispatcher.dispatcher.io()) {
            delay(1000L)
            "Hello world"
        }
}