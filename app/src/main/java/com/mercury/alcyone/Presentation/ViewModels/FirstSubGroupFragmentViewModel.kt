package com.mercury.alcyone.Presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercury.alcyone.Data.DataSources.ApiResult
import com.mercury.alcyone.Data.MySharedPreferencesManager
import com.mercury.alcyone.Data.Repos.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FirstSubGroupFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val mySharedPreferencesManager: MySharedPreferencesManager
): ViewModel() {


    private val _uiState = MutableStateFlow<ApiResult<*>>(ApiResult.Loading)
    val uiState: StateFlow<ApiResult<*>> = _uiState

    fun fetchData() {
        viewModelScope.launch {
            repository.getExampleFlowTest().collectLatest { data ->
                _uiState.update { data }
            }
        }
    }
}