package com.example.a436group_proj

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
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

    //set onclicklisteners
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

    }
}