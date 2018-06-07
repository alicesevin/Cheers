package com.hetic.cheers.model

import java.io.Serializable

data class Tag(
        var id: Int,
        var name: String = "",
        var type : String = "") : Serializable
