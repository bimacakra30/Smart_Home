package com.karebet.smarthome.model

import androidx.compose.ui.graphics.vector.ImageVector

data class RelayInfo(
    val name: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)