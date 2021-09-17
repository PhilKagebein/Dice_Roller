package com.example.diceroller_gridlayoutintro

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.diceroller_gridlayoutintro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var howMany = 0
    //Originally dieLIst set using the safe call operator and defined as null. Was getting fucky results though when adding results to dieList down in the setDiceList function. Don't know why
    private var dieList: ArrayList<DieModel> = ArrayList()
    private var whatType = ""
    private lateinit var gridView: GridView
    private var sum = 0
    private lateinit var spinnerHowMany: Spinner
    private lateinit var spinnerDiceType: Spinner
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSpinners()
        //Kept the onItemSelectedListeners out of initSpinners() because you said keep UI code away from business logic. Is correct to do?
        spinnerHowMany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            //tried to return an int here but that didn't work either.
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                howManySelect(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //     Do I need anything in here?
            }
        }
        spinnerDiceType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                whatType(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    fun howManySelect(p2: Int){
        if(p2 == 0) {
            dieList = ArrayList()
            //Originally had !! after dieList
            binding.gvDieResults.adapter = DieResultAdapter(this, dieList)
            binding.tvSum.text = ""
            howMany = p2
        }else{
            howMany = p2
        }
    }

    fun whatType(p2: Int) = if(p2==0){
        whatType = ""
    }else {
        whatType = spinnerDiceType.getItemAtPosition(p2).toString()
    }

    //why do I need view: View? Android Studio gives me a lil warning guy saying parameter view is never used, but if you take it away, then the app won't load
    fun rollDice(view: View) {

        dieList = ArrayList()
        binding.tvSum.text = ""

        if (howMany == 0 || whatType == "") {
           //Is this how you meant to separate everything?
            sumVisibility(howMany, whatType)
        } else {
            if (howMany == 1) {

                setGridView(howMany)
                sumVisibility(howMany, whatType)
                dieList = getDiceValues(howMany, whatType)

            }else{

                setGridView(howMany)
                dieList = getDiceValues(howMany, whatType)
            }
        }
        binding.gvDieResults.adapter = DieResultAdapter(this, dieList)
    }

    private fun setDiceList(diceValue: Int, diceTypeInt: Int): ArrayList<DieModel>{

         when(diceTypeInt){
             4 -> dieList.add(DieModel(resources.getIdentifier("d4_$diceValue", "drawable", packageName), "d4_+$diceValue"))
             6 -> dieList.add(DieModel(resources.getIdentifier("d6_$diceValue", "drawable", packageName), "d6_+$diceValue"))
             8 -> dieList.add(DieModel(resources.getIdentifier("d8_$diceValue", "drawable", packageName), "d8_+$diceValue"))
             10 -> dieList.add(DieModel(resources.getIdentifier("d10_$diceValue", "drawable", packageName), "d10_+$diceValue"))
             12 -> dieList.add(DieModel(resources.getIdentifier("d12_$diceValue", "drawable", packageName), "d12_+$diceValue"))
             20 -> dieList.add(DieModel(resources.getIdentifier("d20_$diceValue", "drawable", packageName), "d20_+$diceValue"))
         }
         return dieList
    }

    private fun initSpinners(){

        // ***Originally I used the following line to assign a string array in the strings.xml to a variable.
        // then put that variable in the spinner adapter in order to create the spinner. Is there any reason to create the variable? Or is what I have now best practice?****
        // howManyStrArray= resources.getStringArray(R.array.saHowManyDice)
        spinnerHowMany = binding.spnHowManyDice
        spinnerHowMany.adapter = ArrayAdapter(this, R.layout.spinner_items, resources.getStringArray(R.array.saHowManyDice))

        spinnerDiceType = binding.spnDiceType
        spinnerDiceType.adapter = ArrayAdapter(this, R.layout.spinner_items, resources.getStringArray(R.array.saDiceType))

    }

    private fun sumVisibility(howMany: Int, whatType: String) {
        if (howMany == 0 || whatType == "") {
            binding.tvSum.visibility = View.GONE
        }else if(howMany == 1){
            binding.tvSum.visibility = View.GONE
        }else{
            binding.tvSum.visibility = View.VISIBLE
        }
    }

    private fun setGridView(howMany: Int){

        when(howMany) {
            1 -> {
                gridView = findViewById(R.id.gvDieResults)
                gridView.numColumns = 1
            }

            2, 3 -> {
                gridView = findViewById(R.id.gvDieResults)
                sumVisibility(howMany, whatType)
                gridView.numColumns = 1
            }

            4, 5, 6 -> {
                gridView = findViewById(R.id.gvDieResults)
                sumVisibility(howMany, whatType)
                gridView.numColumns = 2
            }
        }
    }

    private fun getDiceValues(howMany: Int, whatType: String): ArrayList<DieModel>{
        sum = 0
        val whatTypeInt: Int = whatType.substring(1).toInt()
        //Could do another instance of removing the variable diceValue altogether and just throwing ((1..whatTypeInt).random()) into setDiceList. Is better to do?
        for (i in 1..howMany) {
            val diceValue = (1..whatTypeInt).random()
            sum += diceValue
            dieList = setDiceList(diceValue, whatTypeInt)
             }
        binding.tvSum.append("Sum: $sum")
        return dieList
    }

}