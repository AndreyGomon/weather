package ru.mascot.features.weather.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import ru.mascot.utils.ui.MascotExposedDropdownMenu
import ru.mascot.utils.ui.MascotTextField
import ru.mascot.utils.ui.icons.ChevronDown
import ru.mascot.utils.ui.icons.ChevronUp
import ru.mascot.utils.ui.icons.MascotIcons

@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    value: String,
    values: List<String>,
    label: String,
    onValueChanged: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var textSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    val icon = if (expanded) {
        MascotIcons.ChevronUp
    } else {
        MascotIcons.ChevronDown
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MascotExposedDropdownMenu(
            expanded = expanded,
            content = {
                MascotTextField(
                    value = value,
                    onValueChange = {},
                    label = { Text(text = label) },
                    onClick = {
                        expanded = !expanded
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            run {
                                textSize = coordinates.size.toSize()
                            }
                        }
                )
            },
            onDismissRequest = {
                expanded = false
            },
            modifier = modifier.fillMaxWidth()
                .height(70.dp)
        ) {
            values.forEachIndexed { _, s ->
                DropdownMenuItem(
                    text = { Text(s) },
                    onClick = {
                        expanded = false
                        onValueChanged(s)
                    })
            }
        }
    }
}