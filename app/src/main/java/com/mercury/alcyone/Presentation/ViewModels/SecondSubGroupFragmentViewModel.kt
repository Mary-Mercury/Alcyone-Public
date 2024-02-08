package com.mercury.alcyone.Presentation.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercury.alcyone.Data.DataSources.ApiResult
import com.mercury.alcyone.Data.MySharedPreferencesManager
import com.mercury.alcyone.Data.Repos.Repository
import com.mercury.alcyone.Data.TableTestDto
import dagger.hilt.android.lifecycle.HiltViewModel
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
    }
}