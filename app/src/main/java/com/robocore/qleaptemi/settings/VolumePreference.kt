package com.robocore.qleaptemi.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.robocore.qleaptemi.ui.theme.TemiGreen


/** Can be public or internal */
@Composable
fun VolumePreference(onItemSelected: (selected: Int) -> Unit = {}, lastSelected: Int = 0) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    val lastSelectedIndex = items.indexOf(lastSelected.toString())
    val disabledValue = "B"
    var selectedIndex = lastSelectedIndex

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            items[selectedIndex],
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clickable(onClick = { expanded = true })
                .background(
                    TemiGreen
                ),
            fontSize = 36.sp,
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .background(
                    Color.LightGray
                )

        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
//                    onOptionSelected(index)
                    onItemSelected(s.toInt())
                }) {
                    val disabledText = if (s == disabledValue) {
                        "(Disabled)"
                    } else {
                        ""
                    }
                    Text(text = s + disabledText)
                }
            }
        }
    }
}