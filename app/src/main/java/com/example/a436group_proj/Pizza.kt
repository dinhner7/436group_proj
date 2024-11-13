package com.example.a436group_proj

class Pizza {
    private var size: Int = 1           //0,1,2 = small, medium, large
    private var totalCost: Float = 0f
    private var numPizzas: Int = 1
    private var toppings: ArrayList<String> = ArrayList()

    //constant array of toppings, crust, cheese
    private val toppingTypes: Array<String> =
        arrayOf("pepperoni", "sardines", "sausage", "bacon", "mushrooms",
            "peppers", "pineapple", "basil", "olives", "spinach")
    private val crustTypess: Array<String> = arrayOf("normal", "thin crust", "garlic butter")
    private val cheeseTypes: Array<String> = arrayOf("mozzarella", "swiss", "parmesan", "none")

    fun setSize(s: Int) {
        size = s
    }

    fun setNumPizzas(num: Int) {
        numPizzas = num
    }

    fun getTotalCost(): Float {
        return totalCost
    }

    fun addTopping(str: String): Boolean{
        if(!toppings.contains(str)){
            toppings.add(str)
            return true
        }
        return false
    }
}