package com.mercury.alcyone.Data.DataSources

import com.mercury.alcyone.Data.TableTestDto
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TestDataSource3842 @Inject constructor(
    private val postgrest: Postgrest
) {
    fun getData3842FlowTest(): Flow<ApiResult<List<TableTestDto>>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                val result = postgrest["Schedule3842"]
                    .select {
                        order("id", Order.ASCENDING)
                    }.decodeList<TableTestDto>()
                emit(ApiResult.Success(result))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }
        }
    }

    fun getExampleFlowTest(): Flow<ApiResult<List<TableTestDto>>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                val result = postgrest["Example"]
                    .select {
                        order("id", Order.ASCENDING)
                    }.decodeList<TableTestDto>()
                emit(ApiResult.Success(result))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }
        }
    }

    fun getData3832FlowTest(): Flow<ApiResult<List<TableTestDto>>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                val result = postgrest["Schedule3832"]
                    .select {
                        order("id", Order.ASCENDING)
                    }.decodeList<TableTestDto>()
                emit(ApiResult.Success(result))
            } catch (e: Exception) {
                emit(ApiResult.Error(e.message))
            }
        }
    }
}

sealed class ApiResult<out R> {
    data class Success<out R>(val data: R): ApiResult<R>()
    data class Error(val message: String?): ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}