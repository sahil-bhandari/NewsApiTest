package com.sahil.cocoontest.models.network

data class Multimedia (
    val url : String,
    val format : String,
    val height : Int,
    val width : Int,
    val type : String,
    val subtype : String,
    val caption : String,
    val copyright : String
)