package com.example.mavuno.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mavuno.data.local.TokenManager
import com.example.mavuno.data.repository.AuthRepository
import com.example.mavuno.ui.components.AuthBtn
import com.example.mavuno.ui.components.AuthHeader
import com.example.mavuno.ui.components.AuthNav
import com.example.mavuno.ui.components.Input
import com.example.mavuno.ui.theme.CropTheme

@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel(
        factory = androidx.compose.runtime.remember {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val tokenManager = TokenManager(context.applicationContext)
                    val repository = AuthRepository(tokenManager = tokenManager)
                    @Suppress("UNCHECKED_CAST")
                    return AuthViewModel(repository) as T
                }
            }
        }
    )

    val authState by viewModel.authState.collectAsState()
    // rememberSaveable preserves field content across configuration changes
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.navigate("main") {
                popUpTo("signin") { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    // verticalScroll + imePadding ensure content is reachable when the
    // soft keyboard is open, even on small-screen devices
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        AuthHeader(
            "Get started with smarter farming",
            "Sign in to your account",
            false,
            navController
        )
        Spacer(modifier = Modifier.height(30.dp))
        AuthNav(login = true, register = false, navController)
        Spacer(modifier = Modifier.height(20.dp))

        Input(
            "Email",
            value = email,
            onValueChange = { email = it },
            placeholder = "Enter your email"
        )
        Spacer(modifier = Modifier.height(20.dp))
        Input(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            placeholder = "Setup your password",
            isPassword = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            "Forgot Password?",
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .clickable(onClick = { navController.navigate("forgot") })
                .fillMaxWidth()
        )

        if (authState is AuthState.Error) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        AuthBtn(
            onClick = { viewModel.login(email, password) },
            text = "Login",
            enabled = authState !is AuthState.Loading,
            isLoading = authState is AuthState.Loading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an account? ", color = CropTheme.colors.tag)
            Text(
                "Sign up",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = { navController.navigate("signup") })
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
