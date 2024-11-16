package com.example.a436group_proj

class Pizza {
    private var size: String = ""           //0,1,2 = small, medium, large
    private var basePrice: Float = 0f   //base cost updated based on pizza size
    private var numPizzas: Int = 1
    private var toppings: ArrayList<String> = ArrayList()
    private var crust: String = ""
    private var cheese: String = ""

    //constant array of toppings, crust, cheese just for record keeping
    private val toppingTypes: Array<String> =
        arrayOf("pepperoni", "sardines", "sausage", "bacon", "mushrooms",
            "peppers", "pineapple", "basil", "olives", "spinach")   //$1 extra per topping
    private val crustTypess: Array<String> = arrayOf("normal", "thin crust", "garlic butter")
    private val cheeseTypes: Array<String> = arrayOf("mozzarella", "swiss", "parmesan", "none")

    fun setSize(s: String) {
        size = s
    }
    fun setCrust(s: String) {
        crust = s
    }
    fun setCheese(s: String){
        cheese = s
    }

    fun setBasePrice(f: Float) {
        basePrice = f
    }

    fun getNumPizzas(): Int {
        return numPizzas
    }
    fun getNumPizzaStr(): String {
        return numPizzas.toString()
    }
    fun setNumPizzas(num: Int) {
        numPizzas = num
    }

    fun getTotalCost(): Float {
        return numPizzas*(basePrice + toppings.size)
    }

    fun getCostStr(): String {
        return "%.2f".format(getTotalCost())
    }

    //add topping only if not already in list
    fun addTopping(str: String): Boolean{
        if(!toppings.contains(str)){
            toppings.add(str)
            return true
        }
        return false
    }
    //remove topping only if it is in the list
    fun removeTopping(str: String): Boolean{
        if(toppings.contains(str)){
            toppings.remove(str)
            return true
        }
        return false
    }
}