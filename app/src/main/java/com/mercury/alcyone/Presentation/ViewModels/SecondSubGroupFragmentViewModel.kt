package com.mercury.alcyone.Presentation.ViewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercury.alcyone.DI.Supabase
import com.mercury.alcyone.Data.DataSources.ApiResult
import com.mercury.alcyone.Data.MySharedPreferencesManager
import com.mercury.alcyone.Data.Repos.Repository
import com.mercury.alcyone.Data.TableTestDto
import com.mercury.alcyone.Data.sharedPref
import com.mercury.alcyone.Data.userState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.signInAnonymously
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecondSubGroupFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val mySharedPreferencesManager: MySharedPreferencesManager,
    private val supabaseClient: SupabaseClient,
    private val sharedPref: sharedPref
): ViewModel() {

    private val _uiState = MutableStateFlow<ApiResult<List<TableTestDto>>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<List<TableTestDto>>> get() = _uiState

    private val _filteredDataFlow = MutableStateFlow<ApiResult<List<TableTestDto>>>(ApiResult.Loading)
    val filteredDataFlow: StateFlow<ApiResult<List<TableTestDto>>> get() = _filteredDataFlow



    fun filterData(selectDay: String, selectWeek: String) {
        val per = mySharedPreferencesManager.getData("altMode", false)
        var dayOfWeek = selectDay
        Log.e("ApiResult", dayOfWeek)
        var numOfWeek = selectWeek
        Log.e("ApiResult", numOfWeek)
        var parityOFWeek: String = if (per) {
            if (numOfWeek.toInt() % 2 == 1) { "четная" } else { "нечетная" }
        } else {
            if (numOfWeek.toInt() % 2 == 0) { "четная" } else { "нечетная" }
        }
        viewModelScope.launch {
            _uiState.collect {apiResult ->
                when(apiResult) {
                    is ApiResult.Success -> {
                        val filteredData = apiResult.data.filter { table ->
                            table.day == dayOfWeek && table.parity == parityOFWeek
                        }
                        _filteredDataFlow.value = ApiResult.Success(filteredData)
                        Log.e("ApiResult2", filteredDataFlow.value.toString())
                    }
                    is ApiResult.Error -> {
                        // Оставляем ошибку без изменений и сохраняем Loading в отфильтрованных данных
                        _filteredDataFlow.value = ApiResult.Loading
                    }
                    ApiResult.Loading -> {
                        // Оставляем состояние загрузки без изменений
                        _filteredDataFlow.value = ApiResult.Loading
                    }
                }
            }
        }
    }
    init {
        fetchTables()
    }

    fun fetchTables() {
        val pos = mySharedPreferencesManager.getPosition()
        if (pos == 0 ) {
            viewModelScope.launch {
                repository.getExampleFlowTest().collectLatest { data ->
                    _uiState.update { data }
                }
            }
        }
        if (pos == 1) {
            viewModelScope.launch {
                repository.getData3842FlowTest().collectLatest { data ->
                    _uiState.update { data }
                }
            }
        }
        if (pos == 2) {
            viewModelScope.launch {
                repository.getData3832FlowTest().collectLatest { data ->
                    _uiState.update { data }
                }
            }
        }
    }

    private val _userState = mutableStateOf<userState>(userState.Loading)
    val userStateLD: State<userState> = _userState

    //функция регистарции
    fun signUp(
        context: Context,
        userEmail: String,
        userPassword: String
    ) {
        viewModelScope.launch {
            try {
                supabaseClient.auth.signUpWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                saveToken(context)
                _userState.value = userState.Success("Registered user successfully!")
            } catch (e: Exception) {
                _userState.value = userState.Error("Error: ${e.message}")
            }
        }
    }

    fun anonymousLogin(context: Context,) {
        viewModelScope.launch {
            try {
                supabaseClient.auth.currentUserOrNull()
                saveToken(context)
                _userState.value = userState.Success("Logged user successfully!")
            } catch (e: Exception) {
                _userState.value = userState.Error("Error: ${e.message}")
            }
        }
    }

    //функция входа в систему
    fun login(
        context: Context,
        userEmail: String,
        userPassword: String
    ) {
        viewModelScope.launch {
            try {
                supabaseClient.auth.signInWith(Email) {
                    email = userEmail
                    password = userPassword
                }
                saveToken(context)
                _userState.value = userState.Success("Logged user successfully!")
            } catch (e: Exception) {
                _userState.value = userState.Error("Error: ${e.message}")
            }
        }
    }

    //функция выхода из системы
    fun logout(context: Context) {
        viewModelScope.launch {
            _userState.value = userState.Loading
            try {
                supabaseClient.auth.signOut()
                sharedPref.clearPreferences()
                _userState.value = userState.Success("Logged out successfully!")
            } catch (e: Exception) {
                _userState.value = userState.Error("Error: ${e.message}")
            }
        }
    }

    //проверка, зашел ли пользователь в систему ранее
    fun isUserLoggedIn(context: Context) {
        viewModelScope.launch{
            try {
                val token = getToken(context)
                if (token.isNullOrEmpty()) {
                    _userState.value = userState.Error("User is not Logged in!")
                } else {
                    supabaseClient.auth.retrieveUser(token)
                    supabaseClient.auth.refreshCurrentSession()
                    saveToken(context)
                    _userState.value = userState.Success("User is already logged in!")
                }
            } catch (e: Exception) {
                _userState.value = userState.Error("Error: ${e.message}")
            }
        }
    }

    fun saveToken(context: Context) {
        viewModelScope.launch {
            val accessToken = supabaseClient.auth.currentAccessTokenOrNull() ?: ""
            sharedPref.saveStringData("accessToken", accessToken)
        }
    }

    fun getToken(context: Context): String? {
        return sharedPref.getStringData("accessToken")
    }
}