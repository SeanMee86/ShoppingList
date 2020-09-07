package com.seanmeedev.shoppinglist.models

data class GroceryItem (
    val name: String = "",
    val quantity: Int = 1,
    val gotten: Boolean = false
)