package com.mercury.alcyone.Presentation.TabLayout

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mercury.alcyone.Data.DataSources.ApiResult
import com.mercury.alcyone.Data.TableTestDto
import com.mercury.alcyone.Presentation.ViewModels.SecondSubGroupFragmentViewModel
import com.mercury.alcyone.Presentation.recyclerview_2.SecondSubRecyclerView2
import com.example.alcyone.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SecondSubGroupFragment : Fragment() {

    private lateinit var llSub: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SecondSubRecyclerView2
    private val viewModel: SecondSubGroupFragmentViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_second_sub_group, container, false)

        llSub = view.findViewById(R.id.ll_text)
        sharedPreferences = requireActivity().getSharedPreferences("MyAltMode", Context.MODE_PRIVATE)
        recyclerView = view.findViewById(R.id.recyclerView2)
        adapter = SecondSubRecyclerView2(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredDataFlow.collect {apiResult ->
                when(apiResult) {
                    is ApiResult.Success -> {
                        adapter.updateData(apiResult.data)
                    }
                    else -> {}
                }
            }
        }
        val dataListStateFlow: StateFlow<ApiResult<List<TableTestDto>>> = viewModel.filteredDataFlow

        lifecycleScope.launchWhenStarted {
            dataListStateFlow.collect { apiResult ->
                when (apiResult) {
                    is ApiResult.Success -> {
                        val dataList = apiResult.data
                        var index = 1
                        var message: String? = null

                        for (data in dataList.takeLast(6)) {
                            if (data.SubName != "СП") {
                                message = getString(R.string.class_text1) + " ${data.time} "
                                break
                            } else {
                                message = getString(R.string.text_dayoff)
                            }
                            index++
                        }
                        llSub.text = message
                    }
                    is ApiResult.Error -> {
                    }
                    is ApiResult.Loading -> {
                    }
                }
            }
        }
        return view
    }
}
