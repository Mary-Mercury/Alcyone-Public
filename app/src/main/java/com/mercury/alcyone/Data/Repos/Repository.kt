package com.mercury.alcyone.Data.Repos

import com.mercury.alcyone.Data.DataSources.ApiResult
import com.mercury.alcyone.Data.DataSources.TestDataSource3842
import com.mercury.alcyone.Data.TableTestDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface Repository {

    suspend fun getTest3842(): List<TableTestDto>

    suspend fun getExample(): List<TableTestDto>

    fun getData3842FlowTest(): Flow<ApiResult<List<TableTestDto>>>

    fun getExampleFlowTest(): Flow<ApiResult<List<TableTestDto>>>
}

class RepositoryImpl @Inject constructor(
    private val test3842: TestDataSource3842
) : Repository {

    override suspend fun getTest3842(): List<TableTestDto> {
        return test3842.getData3842()
    }

    override suspend fun getExample(): List<TableTestDto> {
        return test3842.getExample()
    }

    override fun getData3842FlowTest(): Flow<ApiResult<List<TableTestDto>>> {
        return test3842.getData3842FlowTest()
    }

    override fun getExampleFlowTest(): Flow<ApiResult<List<TableTestDto>>> {
        return test3842.getExampleFlowTest()
    }
}