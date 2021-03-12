package com.example.myfoodapp.model

import org.json.JSONArray

data class OrderDetails (
    val id: Int,
    val resName: String,
    val orderDate: String,
    val foodItem: JSONArray
)
