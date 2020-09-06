package com.seanmeedev.shoppinglist

data class GroceryItem (
    val name: String = "",
    val key: String = "",
    val quantity: Int = 1,
    val gotten: Boolean = false
)