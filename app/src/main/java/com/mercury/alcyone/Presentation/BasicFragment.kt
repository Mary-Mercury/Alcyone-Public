package com.mercury.alcyone.Presentation

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.alcyone.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mercury.alcyone.Presentation.ViewModels.SecondSubGroupFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BasicFragment : Fragment() {

    private lateinit var btnNavView: BottomNavigationView
    private lateinit var controller: FragmentContainerView
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: SecondSubGroupFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_basic, container, false)
//        btnNavView = view.findViewById(R.id.bottomNavigationView)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentNavConst) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val navController = findNavController()
//        btnNavView.setupWithNavController(navController)
    }
}