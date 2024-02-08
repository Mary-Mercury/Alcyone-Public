package com.mercury.alcyone.Data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TableTestDto(
    @SerialName("id")
    val id: Int,
    @SerialName("aud_name")
    val AudName: String,
    @SerialName("sub_name")
    val SubName: String,
    @SerialName("subgroup")
    val subGroup: String,
    @SerialName("parity")
    val parity: String,
    @SerialName("time")
    val time: String,
    @SerialName("day")
    val day: String
)