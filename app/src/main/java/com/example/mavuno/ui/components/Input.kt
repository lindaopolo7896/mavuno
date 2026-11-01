package com.example.mavuno.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mavuno.ui.theme.CropTheme


@Composable
fun Input(label: String,
          value:String,
          onValueChange:(String)-> Unit,
          placeholder: String,
          isPassword: Boolean=false,
          isSearch: Boolean=false
          ){
    var isPasswordVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Text(label, color = Color.Black.copy(alpha = 0.73f))
        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            value=value,
            onValueChange = onValueChange,

            placeholder = {Text(placeholder)},
            visualTransformation = if(isPassword && isPasswordVisible)

                        VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
                if (isPassword) {
                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }

                if (isSearch) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            leadingIcon ={
                if (isSearch){
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint= MaterialTheme.colorScheme.secondary
                    )
                }
            } ,
            singleLine = true,
            modifier = Modifier

                .height(50.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CropTheme.colors.inputBackground,
                focusedContainerColor = Color.Transparent,
                unfocusedTextColor = Color.Black.copy(0.53f),
                focusedTextColor = Color.Black.copy(0.53f)

                ),
            shape = RoundedCornerShape(28.dp),

        )
    }
}
