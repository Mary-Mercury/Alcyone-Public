package com.mercury.alcyone.Presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mercury.alcyone.Data.MySharedPreferencesManager
import com.example.alcyone.R
import com.google.android.material.button.MaterialButtonToggleGroup
import com.mercury.alcyone.Data.sharedPref
import com.mercury.alcyone.Presentation.ViewModels.SecondSubGroupFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject
    lateinit var sharedPreferencesManager: MySharedPreferencesManager
    lateinit var sharedPref: sharedPref
    private val viewModel: SecondSubGroupFragmentViewModel by activityViewModels()

    private lateinit var switch: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedPreferences2: SharedPreferences
    private lateinit var  switch2: SwitchCompat
    private lateinit var spinner: Spinner
    private lateinit var LogoutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
        sharedPreferences2 = requireActivity().getSharedPreferences("MyAltMode", Context.MODE_PRIVATE)
    }



    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        switch = view.findViewById(R.id.swDarkMode)
        switch2 = view.findViewById(R.id.swAltMode)
        spinner = view.findViewById(R.id.spinner)
        LogoutBtn = view.findViewById(R.id.Logout)

        LogoutBtn.setOnClickListener {
            context?.let { it1 -> viewModel.logout(it1) }
            val fragmentManager = requireActivity().supportFragmentManager
            val secondFragment = LoginFragment()
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, secondFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_items,
            android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        val isNightModeOn = sharedPreferences.getBoolean("night_mode", false)
        switch.isChecked = isNightModeOn

        switch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("night_mode", isChecked)
            editor.apply()

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val isAlModeOn = sharedPreferences2.getBoolean("alt_mode", false)
        switch2.isChecked = isAlModeOn
        switch2.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferencesManager.saveData("altMode", isChecked)
            val editor = sharedPreferences2.edit()
            editor.putBoolean("alt_mode", isChecked)
            editor.apply()
        }

        val selectedItemPosition = sharedPreferencesManager.getIntData("selectedItemPosition", 0)
        spinner.setSelection(selectedItemPosition)
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sharedPreferencesManager.saveIntData("selectedItemPosition", position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })
        return view
    }
}