package com.example.a436group_proj

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Customize : AppCompatActivity() {
    private lateinit var sizeRG: RadioGroup
    private lateinit var crustRG: RadioGroup
    private lateinit var cheeseRG: RadioGroup
    private lateinit var proniMinB: androidx.appcompat.widget.AppCompatButton
    private lateinit var proniPlusB: androidx.appcompat.widget.AppCompatButton
    private lateinit var sardineMinB: androidx.appcompat.widget.AppCompatButton
    private lateinit var sardinePlusB: androidx.appcompat.widget.AppCompatButton
    private lateinit var sausageMinB: androidx.appcompat.widget.AppCompatButton
    private lateinit var sausagePlusB: androidx.appcompat.widget.AppCompatButton
    private lateinit var baconMinB: androidx.appcompat.widget.AppCompatButton
    private lateinit var baconPlusB: androidx.appcompat.widget.AppCompatButton
    private lateinit var mushMinB: androidx.appcompat.widget.AppCompatButton
    private lateinit var mushPlusB: androidx.appcompat.widget.AppCompatButton
    private lateinit var pepperMinB: androidx.appcompat.widget.AppCompatButton
    private lateinit var pepperPlusB: androidx.appcompat.widget.AppCompatButton
    private lateinit var pappleMinB: androidx.appcompat.widget.AppCompatButton
    private lateinit var papplePlusB: androidx.appcompat.widget.AppCompatButton
    private lateinit var basilMinB: androidx.appcompat.widget.AppCompatButton
    private lateinit var basilPlusB: androidx.appcompat.widget.AppCompatButton
    private lateinit var olivesMinB: androidx.appcompat.widget.AppCompatButton
    private lateinit var olivesPlusB: androidx.appcompat.widget.AppCompatButton
    private lateinit var spinachMinB: androidx.appcompat.widget.AppCompatButton
    private lateinit var spinachPlusB: androidx.appcompat.widget.AppCompatButton
    private lateinit var numPizzaMinB: androidx.appcompat.widget.AppCompatButton
    private lateinit var numPizzaPlusB: androidx.appcompat.widget.AppCompatButton
    private lateinit var numPizzasNumber: TextView
    private lateinit var totalCost: TextView
    private lateinit var finOrder: androidx.appcompat.widget.AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_customize)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    //initializations
        sizeRG = findViewById(R.id.sizeSelectRBs)
        crustRG = findViewById(R.id.crustSelectRBs)
        cheeseRG = findViewById(R.id.cheeseSelectRBs)
        proniMinB = findViewById(R.id.proniMinB)
        proniPlusB = findViewById(R.id.proniPlusB)
        sardineMinB = findViewById(R.id.sardineMinB)
        sardinePlusB = findViewById(R.id.sardinePlusB)
        sausageMinB = findViewById(R.id.sausageMinB)
        sausagePlusB = findViewById(R.id.sausagePlusB)
        baconMinB = findViewById(R.id.baconMinB)
        baconPlusB = findViewById(R.id.baconPlusB)
        mushMinB = findViewById(R.id.mushroomMinB)
        mushPlusB = findViewById(R.id.mushroomPlusB)
        pepperMinB = findViewById(R.id.pepperMinB)
        pepperPlusB = findViewById(R.id.pepperPlusB)
        pappleMinB = findViewById(R.id.pappleMinB)
        papplePlusB = findViewById(R.id.papplePlusB)
        basilMinB = findViewById(R.id.basilMinB)
        basilPlusB = findViewById(R.id.basilPlusB)
        olivesMinB = findViewById(R.id.olivesMinB)
        olivesPlusB = findViewById(R.id.olivesPlusB)
        spinachMinB = findViewById(R.id.spinachMinB)
        spinachPlusB = findViewById(R.id.spinachPlusB)
        numPizzaMinB = findViewById(R.id.minusPizzaB)
        numPizzaPlusB = findViewById(R.id.plusPizzaB)
        numPizzasNumber = findViewById(R.id.numPizzasNumber)
        totalCost = findViewById(R.id.costNum)
        finOrder = findViewById(R.id.finishPizza)

    //make new pizza
        var p: Pizza = Pizza()

    //set listeners for radio buttons
        sizeRG.setOnCheckedChangeListener {_, checkedId ->
            when(checkedId){
                R.id.sizeS -> {p.setBasePrice(10.00f)
                               p.setSize("Small")
                               totalCost.text = p.getCostStr()}
                R.id.sizeM -> {p.setBasePrice(12.00f)
                               p.setSize("Medium")
                               totalCost.text = p.getCostStr()}
                R.id.sizeL -> {p.setBasePrice(16.00f)
                               p.setSize("Large")
                               totalCost.text = p.getCostStr()}
                else -> {p.setBasePrice(0f)
                         p.setSize("")}
            }
        }
        crustRG.setOnCheckedChangeListener {_, checkedId ->
            when(checkedId){
                R.id.crust1 -> p.setCrust("Regular")
                R.id.crust2 -> p.setCrust("Thin Crust")
                R.id.crust3 -> p.setCrust("Garlic Butter")
                else -> p.setCrust("")
            }
        }
        cheeseRG.setOnCheckedChangeListener {_, checkedId ->
            when(checkedId){
                R.id.cheese1 -> p.setCheese("Mozzarella")
                R.id.cheese2 -> p.setCheese("Swiss")
                R.id.cheese3 -> p.setCheese("Parmesan")
                R.id.cheese4 -> p.setCheese("None")
                else -> p.setCheese("")
            }
        }
    //set listeners for +/- buttons for toppings and numPizzas
        proniMinB.setOnClickListener{
            if(p.removeTopping("pepperoni")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Pepperoni removed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't remove", Toast.LENGTH_SHORT).show()
            }
        }
        proniPlusB.setOnClickListener{
            if(p.addTopping("pepperoni")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Pepperoni added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't add again", Toast.LENGTH_SHORT).show()
            }
        }
        sardineMinB.setOnClickListener{
            if(p.removeTopping("sardines")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Sardines removed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't remove", Toast.LENGTH_SHORT).show()
            }
        }
        sardinePlusB.setOnClickListener{
            if(p.addTopping("sardines")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Sardines added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't add again", Toast.LENGTH_SHORT).show()
            }
        }
        sausageMinB.setOnClickListener{
            if(p.removeTopping("sausage")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Sausage removed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't remove", Toast.LENGTH_SHORT).show()
            }
        }
        sausagePlusB.setOnClickListener{
            if(p.addTopping("sausage")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Sausage added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't add again", Toast.LENGTH_SHORT).show()
            }
        }
        baconMinB.setOnClickListener{
            if(p.removeTopping("bacon")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Bacon removed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't remove", Toast.LENGTH_SHORT).show()
            }
        }
        baconPlusB.setOnClickListener{
            totalCost.text = p.getCostStr()
            if(p.addTopping("bacon")){
                Toast.makeText(this, "Bacon added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't add again", Toast.LENGTH_SHORT).show()
            }
        }
        mushMinB.setOnClickListener{
            if(p.removeTopping("mushrooms")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Mushrooms removed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't remove", Toast.LENGTH_SHORT).show()
            }
        }
        mushPlusB.setOnClickListener{
            if(p.addTopping("mushrooms")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Mushrooms added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't add again", Toast.LENGTH_SHORT).show()
            }
        }
        pepperMinB.setOnClickListener{
            if(p.removeTopping("peppers")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Peppers removed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't remove", Toast.LENGTH_SHORT).show()
            }
        }
        pepperPlusB.setOnClickListener{
            if(p.addTopping("peppers")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Peppers added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't add again", Toast.LENGTH_SHORT).show()
            }
        }
        pappleMinB.setOnClickListener{
            if(p.removeTopping("pineapple")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Pineapple removed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't remove", Toast.LENGTH_SHORT).show()
            }
        }
        papplePlusB.setOnClickListener{
            if(p.addTopping("pineapple")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Pineapple added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't add again", Toast.LENGTH_SHORT).show()
            }
        }
        basilMinB.setOnClickListener{
            if(p.removeTopping("basil")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Basil removed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't remove", Toast.LENGTH_SHORT).show()
            }
        }
        basilPlusB.setOnClickListener{
            if(p.addTopping("basil")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Basil added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't add again", Toast.LENGTH_SHORT).show()
            }
        }
        olivesMinB.setOnClickListener{
            if(p.removeTopping("olives")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Olives removed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't remove", Toast.LENGTH_SHORT).show()
            }
        }
        olivesPlusB.setOnClickListener{
            if(p.addTopping("olives")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Olives added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't add again", Toast.LENGTH_SHORT).show()
            }
        }
        spinachMinB.setOnClickListener{
            if(p.removeTopping("spinach")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Spinach removed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't remove", Toast.LENGTH_SHORT).show()
            }
        }
        spinachPlusB.setOnClickListener{
            if(p.addTopping("spinach")){
                totalCost.text = p.getCostStr()
                Toast.makeText(this, "Spinach added", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Can't add again", Toast.LENGTH_SHORT).show()
            }
        }
        numPizzaMinB.setOnClickListener{
            if(p.getNumPizzas() > 1){
                p.setNumPizzas(p.getNumPizzas() - 1)
                numPizzasNumber.text = p.getNumPizzaStr()
                totalCost.text = p.getCostStr()
            }else{
                Toast.makeText(this, "Must have at least 1 pizza", Toast.LENGTH_SHORT).show()
            }
        }
        numPizzaPlusB.setOnClickListener{
            p.setNumPizzas(p.getNumPizzas() + 1)
            numPizzasNumber.text = p.getNumPizzaStr()
            totalCost.text = p.getCostStr()
        }
    //set listener for finishOrder button
    //add pizza to the order object in HomeActivity and
    //finish customize only if all radio buttons selected
        finOrder.setOnClickListener{
            if(sizeRG.checkedRadioButtonId == -1){
                Toast.makeText(this, "Select a Size", Toast.LENGTH_SHORT).show()
            }else if(crustRG.checkedRadioButtonId == -1){
                Toast.makeText(this, "Select a Crust", Toast.LENGTH_SHORT).show()
            }else if(cheeseRG.checkedRadioButtonId == -1){
                Toast.makeText(this, "Select a cheese", Toast.LENGTH_SHORT).show()
            }else{
                HomeActivity.order.addPizza(p)
                this.finish()
            }
        }

    }
}