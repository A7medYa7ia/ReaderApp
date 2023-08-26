package com.example.reader.screens.login

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.reader.R
import com.example.reader.components.EmailInput
import com.example.reader.components.PasswordInput
import com.example.reader.components.ReaderLogo

@Composable
fun ReaderLoginScreen(navController: NavController){
    val showLoginForm = rememberSaveable{
        mutableStateOf(true)
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
            ReaderLogo()
            if (showLoginForm.value)
            UserForm(loading = false, isCreateAccount = false){
                email, password ->

            }
            else{
                UserForm(loading = false, isCreateAccount = true) {
                    email, password ->

                }
            }
        }

        Spacer(modifier = Modifier.height(25.dp))
        Row (
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
                ){
            val text = if(showLoginForm.value)"Sign up" else "Login"
            Text(text = "New User?")
            Text(text, modifier = Modifier
                .clickable {
                    showLoginForm.value = !showLoginForm.value
                }
                .padding(start = 5.dp),
            fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    loading : Boolean =false,
    isCreateAccount:Boolean = false,
    oneDone:(String, String)->Unit = {email, pwd->}
){
    val email = rememberSaveable{
        mutableStateOf("")
    }
    val password = rememberSaveable{
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable{
        mutableStateOf(false)
    }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value){
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())

    if (isCreateAccount) Text(text = stringResource(id = R.string.create_acct), modifier = Modifier.padding(5.dp))
    else Text(text = "")

    Column(modifier,
        horizontalAlignment = Alignment.CenterHorizontally) {

        EmailInput(emailState = email, enabled = !loading, onAction = KeyboardActions {
            passwordFocusRequest.requestFocus()
        })
        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            onAction = KeyboardActions{
                if(!valid)return@KeyboardActions
                oneDone(email.value.trim(), password.value.trim())
            }

        )

        SubmitButton(
            textId = if (isCreateAccount)"Create Account" else "Login",
            loading = loading,
            validInputs = valid
        ){
            oneDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }


}

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick:()-> Unit
    ) {
    Button(
        onClick =  onClick ,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if(loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))
    }
}
