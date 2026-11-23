package com.example.mavuno.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mavuno.ui.theme.CropTheme


@Composable
fun Input(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    isSearch: Boolean = false
) {
    // rememberSaveable preserves password-visibility toggle across config changes
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (label.isNotBlank()) {
            Text(label, color = CropTheme.colors.heading)
            Spacer(modifier = Modifier.padding(top = 5.dp))
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = CropTheme.colors.tag) },
            visualTransformation = if (isPassword) {
                if (isPasswordVisible)
                    androidx.compose.ui.text.input.VisualTransformation.None
                else
                    androidx.compose.ui.text.input.PasswordVisualTransformation()
            } else {
                androidx.compose.ui.text.input.VisualTransformation.None
            },
            trailingIcon = {
                if (isPassword) {
                    IconButton(
                        onClick = { isPasswordVisible = !isPasswordVisible }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordVisible)
                                "Hide password"
                            else
                                "Show password",
                            tint = CropTheme.colors.heading
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
            leadingIcon = {
                if (isSearch) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            singleLine = true,
            // heightIn instead of fixed height so text is never clipped when font
            // scale is increased by the user's accessibility settings
            modifier = Modifier
                .heightIn(min = 50.dp)
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CropTheme.colors.inputBackground,
                focusedContainerColor = CropTheme.colors.inputBackground,
                unfocusedTextColor = CropTheme.colors.heading,
                focusedTextColor = CropTheme.colors.heading,
                unfocusedIndicatorColor = CropTheme.colors.inputBorder,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(28.dp),
        )
    }
}
