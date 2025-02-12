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

    fun addToPrice(amount : Float) {
        totalPrice += amount
    }

    fun applyDiscount(percentage: Int) {
        val discount = totalPrice * (percentage / 100.0f)
        totalPrice -= discount
    }

    fun clearOrder() {
        totalPrice = 0.0f
        pizzas = ArrayList<Pizza>()
    }

    fun orderSummary() : String {
        val summary = StringBuilder()
        pizzas.forEachIndexed { index, pizza ->
            //summary.append("Items:\n")
            summary.append("Pizza ${index + 1}:\n")
            summary.append("Size: ${pizza.getSizeStr()}\n")
            summary.append("Crust: ${pizza.getCrust()}\n")
            summary.append("Cheese: ${pizza.getCheese()}\n")
            var toppings = pizza.getToppings().joinToString(", ")
            if (toppings.isEmpty()) {
                summary.append("Toppings: None\n\n")
            } else {
                summary.append("Toppings: ${toppings}\n\n")
            }
        }

        return summary.toString()
    }

}