package com.mercury.alcyone.Presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.activityViewModels
import com.example.alcyone.R
import com.mercury.alcyone.Data.MySharedPreferencesManager
import com.mercury.alcyone.Data.sharedPref
import com.mercury.alcyone.Presentation.ViewModels.SecondSubGroupFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BottomAppBarState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mercury.alcyone.Data.userState

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var sharedPref: sharedPref
    private val viewModel: SecondSubGroupFragmentViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    MainScreen()
                }
            }
        }
        return view
    }

    @Composable
    fun MainScreen(viewModel: SecondSubGroupFragmentViewModel = viewModel()) {
        val context = LocalContext.current
        val userState by viewModel.userStateLD
        var userEmail by remember { mutableStateOf("") }
        var userPassword by remember { mutableStateOf("") }
        var showPassword by remember { mutableStateOf(true) }
        var showErrorEmail by remember { mutableStateOf(false) }
        var showErrorPassword by remember { mutableStateOf(false) }

        var currentUserState by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .background(colorResource(R.color.background))
                .fillMaxSize()
                .padding(horizontal = 16.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userEmail,
                    isError = showErrorEmail,
                    placeholder = {
                        Text(text = getString(R.string.email))
                    },
                    onValueChange = {
                        userEmail = it
                    })
                if (showErrorEmail) {
                    Text(text = getString(R.string.emailformat), color = Color.Red)
                }

                Spacer(modifier = Modifier.padding(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userPassword,
                    placeholder = {
                        Text(text = getString(R.string.password))
                    },
                    visualTransformation = if (showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (showPassword) "Скрыть пароль" else "Показать пароль"
                            )
                        }
                    },
                    isError = showErrorPassword,
                    onValueChange = {
                        userPassword = it
                    }
                )
                if (showErrorPassword) {
                    Text(text = getString(R.string.longsix), color = Color.Red)
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button)),
                    onClick = {
                        val isValidEmail =
                            android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
                        val isValidPassword = userPassword.length >= 6

                        if (!isValidEmail) {
                            showErrorEmail = true
                        } else {
                            showErrorEmail = false
                        }
                        if (!isValidPassword) {
                            showErrorPassword = true
                        } else {
                            showErrorPassword = false
                        }
                        if (isValidEmail && isValidPassword) {
                            viewModel.login(
                                context,
                                userEmail,
                                userPassword,
                            )
                        }

                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = getString(R.string.login))
                }
            }
            Column {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = colorResource(R.color.text),
                    text = getString(R.string.acc1),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = colorResource(R.color.text),
                    text = getString(R.string.acc2),
                    textAlign = TextAlign.Center
                )
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button)),
                    onClick = {
                        val isValidEmail =
                            android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
                        val isValidPassword = userPassword.length >= 6

                        if (!isValidEmail) {
                            showErrorEmail = true
                        } else {
                            showErrorEmail = false
                        }
                        if (!isValidPassword) {
                            showErrorPassword = true
                        } else {
                            showErrorPassword = false
                        }
                        if (isValidEmail && isValidPassword) {
                            viewModel.signUp(
                                context,
                                userEmail,
                                userPassword,
                            )
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = getString(R.string.signup))
                }
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button)),
                    onClick = {
                        viewModel.anonymousLogin(
                            context
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = getString(R.string.loginanonym),)
                }
                val mContext = LocalContext.current

                when (userState) {
                    is userState.Loading -> {
                    }

                    is userState.Success -> {
                        val message = (userState as userState.Success).message
                        currentUserState = message

                        val fragmentManager = requireActivity().supportFragmentManager
                        val secondFragment = BasicFragment()
                        val transaction = fragmentManager.beginTransaction()
                        transaction.replace(R.id.fragment_container, secondFragment)
                        transaction.addToBackStack(null)
                        transaction.commit()

                    }

                    is userState.Error -> {
                        val message = (userState as userState.Error).message
//                    currentUserState = message
                        currentUserState = getString(R.string.error)
                    }

                    else -> {}
                }
                if (currentUserState.isNotEmpty()) {
                    Text(text = currentUserState)
                }
            }
        }
    }
}