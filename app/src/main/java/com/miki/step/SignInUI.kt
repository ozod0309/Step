package com.miki.step

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInUI(private val context: Context?, val onClick: (result: Boolean) -> Unit) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI() {
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
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            signUp()
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
    }

    private fun signUp() {
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
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.e("MainActivity", "GetCredentialException", e)
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential

        if (credential is CustomCredential) {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                var signInResult = true
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)
                    Toast.makeText(context, googleIdTokenCredential.idToken, Toast.LENGTH_LONG)
                        .show()
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e(TAG, "Received an invalid google id token response", e)
                    signInResult = false
                } finally {
                    onClick(signInResult)
                }
            } else {
                Log.e(TAG, "Unexpected type of credential")
            }
        } else {
            Log.e(TAG, "Unexpected type of credential")
        }
    }
}

