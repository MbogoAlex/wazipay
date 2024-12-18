package com.escrow.wazipay.utils.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource

@Composable
fun TextFieldComposable(
    shape: Shape?,
    label: String,
    value: String,
    leadingIcon: Int?,
    keyboardOptions: KeyboardOptions,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        shape = shape ?: RoundedCornerShape(0),
        label = {
            Text(
                text = label
            )
        },
        leadingIcon = {
            if(leadingIcon != null) {
                Icon(painter = painterResource(id = leadingIcon), contentDescription = null)
            }
        },
        value = value,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        onValueChange = onValueChange,
        modifier = modifier
    )
}