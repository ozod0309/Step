package com.miki.step

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.miki.step.lib.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInUI(private val context: Context?, val onClick: (result: Boolean) -> Unit) {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationGraphicsApi::class)
    @Composable
    fun UI() {
        val isLoading  = remember { mutableStateOf(false) }
        val image1 = AnimatedImageVector.animatedVectorResource(id = R.drawable.ic_logo_anim)
        val image2 = AnimatedImageVector.animatedVectorResource(id = R.drawable.ic_logo_anim)
        var atEnd by remember { mutableStateOf(false) }
        Scaffold(
            topBar = {
                LargeTopAppBar(
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                    ),
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = context!!.resources.getString(R.string.login),
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(padding),
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            signUp(isLoading)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(35.dp, 0.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    )
                    {
                        Image(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = ""
                        )
                        Text(
                            text = context!!.resources.getString(R.string.google_sign_in),
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(6.dp)
                        )
                    }
                }
            },
            bottomBar = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = context!!.resources.getString(R.string.created_by),
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.onTertiary,
                        textAlign = TextAlign.Center
                    )
            }
        )
        if(isLoading.value) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Image(
                    painter = rememberAnimatedVectorPainter(
                        animatedImageVector = if(atEnd) image1 else image2,
                        atEnd = !atEnd,
                    ),
                    contentDescription = null
                )
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(2000)
                        atEnd = !atEnd
                    }
                }
            }
        }
    }

    private fun signUp(isLoading: MutableState<Boolean>) {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // Query all google accounts on the device
            .setServerClientId(context!!.resources.getString(R.string.default_web_client_id))
            .build()

        val request = androidx.credentials.GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(context)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )
                handleSignIn(result, onFinish = {
                    isLoading.value = false
                    onClick(it)
                })
                isLoading.value = true

            } catch (e: GetCredentialException) {
                Log.e("MainActivity", "GetCredentialException", e)
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse, onFinish:(result: Boolean) -> Unit) {
        val credential = result.credential

        if (credential is CustomCredential) {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                var signInResult = true
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)
                    MainActivity.stepUser = User(
                        accountName = googleIdTokenCredential.id,
                        name = googleIdTokenCredential.givenName.toString(),
                        surname = googleIdTokenCredential.familyName.toString(),
                        googleToken = googleIdTokenCredential.idToken,
                        pictureURL = googleIdTokenCredential.profilePictureUri.toString()
                    )
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e(TAG, "Received an invalid google id token response", e)
                    signInResult = false
                } finally {
                    onFinish(signInResult)
                }
            } else {
                Log.e(TAG, "Unexpected type of credential")
            }
        } else {
            Log.e(TAG, "Unexpected type of credential")
        }
    }
}

