package com.example.a436group_proj

// This is where the entire order to formed
class Order {
    private var pizzas : ArrayList<Pizza> = ArrayList<Pizza>()
    private var totalPrice : Float = 0.0f

    fun addPizza(newPizza : Pizza) {
        pizzas.add(newPizza)
    }

    fun getOrder() : ArrayList<Pizza> {
        return pizzas
    }

    fun getTotalPrice() : Float {
        return totalPrice
    }

}