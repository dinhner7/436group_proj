package com.example.a436group_proj

class Pizza {
    private var size: Int = 1           //0,1,2 = small, medium, large
    private var totalCost: Float = 0f
    private var numPizzas: Int = 0

    //constant array of toppings and crust
    private val toppings: Array<String> =
        arrayOf("pepperoni", "sardines", "sausage", "bacon", "mushrooms",
            "peppers", "pineapple", "basil", "spinach", "olives")
    private val crustTypes: Array<String> = arrayOf("normal", "thin crust", "garlic butter")


}