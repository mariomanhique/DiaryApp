package com.example.diaryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.diaryapp.MainCoroutineRule
import com.example.diaryapp.presentation.screens.auth.authWithCredentials.AuthWithCredentialsViewModel
import com.example.diaryapp.repository.FakeAuthRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthViewModelTest {

    private lateinit var authViewModel: AuthWithCredentialsViewModel

    //It makes sure everything run in the main thread and in order
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    //It makes sure everything run as if we are in the main thread because we are not going to use the emulator.
    // Note that if we were in the androidTest, we wouldnt need this, because the test run in the main thread.
    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Before
    fun setup(){
        authViewModel = AuthWithCredentialsViewModel(FakeAuthRepository())
    }

    @Test
    fun `sign up an user`() = runTest{
        authViewModel.signUp(
            name = "Mario",
            email = "mario@gmail.com",
            password = "1233456",
            onSuccess = {},
            onFailure = {}
        )

        val state = authViewModel.isAuthenticated.value

        assertThat(state).isEqualTo(true)
    }

    @Test
    fun `sign in user`(){
        authViewModel.signIn(
            email = "me@gmail.com",
            password = "1234",
            onSuccess = {},
            onFailure = {}
        )
        val state = authViewModel.isAuthenticated.value

        assertThat(state).isEqualTo(true)
    }
}