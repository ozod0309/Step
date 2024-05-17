package com.miki.step

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class LoginUI(val context: Context?) {


    @Composable
    fun UI(onClick:(result: Boolean) -> Unit) {
        Text(text = stringResource(id = R.string.login))

        Button(
            onClick = {
                onClick(true)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = ""
            )
            Text(text = "Sign in with Google", modifier = Modifier.padding(6.dp))
        }
    }
}
