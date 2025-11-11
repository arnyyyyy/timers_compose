package com.arno.timers_compose.feature_auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arno.timers_compose.BuildConfig.GOOGLE_WEB_CLIENT_ID
import com.arno.timers_compose.feature_firestore_sync.FirestoreSyncManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
        private val firestoreSyncManager: FirestoreSyncManager
) : ViewModel() {
        private val auth = FirebaseAuth.getInstance()

        private val _authState = MutableStateFlow(
                AuthState(user = auth.currentUser)
        )
        val authState: StateFlow<AuthState> = _authState.asStateFlow()

        private var googleSignInClient: GoogleSignInClient? = null

        fun initGoogleSignIn(context: Context) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(GOOGLE_WEB_CLIENT_ID)
                        .requestEmail()
                        .build()

                googleSignInClient = GoogleSignIn.getClient(context, gso)
        }

        fun getSignInIntent(): Intent? {
                return googleSignInClient?.signInIntent
        }

        fun handleSignInResult(data: Intent?) {
                viewModelScope.launch {
                        try {
                                _authState.value =
                                        _authState.value.copy(isLoading = true, error = null)

                                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                                val account = task.getResult(ApiException::class.java)


                                if (account != null) {
                                        firebaseAuthWithGoogle(account)
                                } else {
                                        _authState.value = _authState.value.copy(
                                                isLoading = false,
                                                error = "Не удалось получить аккаунт Google"
                                        )
                                }
                        } catch (e: ApiException) {
                                Log.e(
                                        TAG,
                                        "Google sign in failed: код=${e.statusCode}, сообщение=${e.message}",
                                        e
                                )
                                _authState.value = _authState.value.copy(
                                        isLoading = false,
                                        error = "Ошибка входа (код ${e.statusCode}): ${e.message}"
                                )
                        } catch (e: Exception) {
                                Log.e(TAG, "Неожиданная ошибка", e)
                                _authState.value = _authState.value.copy(
                                        isLoading = false,
                                        error = "Неожиданная ошибка: ${e.message}"
                                )
                        }
                }
        }

        private suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
                try {
                        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                        val authResult = auth.signInWithCredential(credential).await()

                        _authState.value = _authState.value.copy(
                                user = authResult.user,
                                isLoading = false,
                                error = null
                        )

                        loadTimersFromFirestore()

                } catch (e: Exception) {
                        Log.e(TAG, "Firebase auth failed", e)
                        _authState.value = _authState.value.copy(
                                isLoading = false,
                                error = "Ошибка авторизации Firebase: ${e.message}"
                        )
                }
        }

        private suspend fun loadTimersFromFirestore() {
                try {
                        firestoreSyncManager.loadTimersFromFirestore()
                } catch (e: Exception) {
                        Log.e(TAG, e.message.toString())
                }
        }

        fun signOut(context: Context) {
                auth.signOut()
                googleSignInClient?.signOut()
                _authState.value = AuthState(user = null)
        }

        companion object {
                private const val TAG = "AuthViewModel"
        }
}
