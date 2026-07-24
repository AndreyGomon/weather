package io.github.andreygomon.weather.ui.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    value: String,
    values: List<String>,
    label: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                expanded = it
            }
        },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            enabled = enabled,
            readOnly = true,
            singleLine = true,
            label = {
                Text(text = label)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor(
                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                enabled = enabled,
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
        ) {
            values.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(text = item)
                    },
                    onClick = {
                        expanded = false
                        onValueChanged(item)
                    },
                )
            }
        }
    }
}