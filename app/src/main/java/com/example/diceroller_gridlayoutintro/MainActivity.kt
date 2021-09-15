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
    lateinit var gridView: GridView
    private lateinit var spinnerDiceType: Spinner
    //Needed to initialize the howManyStrArray first before I could use howManySelect function. Why?
    private var howManyStrArray: Array<String> = emptyArray()
    private var diceTypeStrArray: Array<String> = emptyArray()
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Everything to make the How Many Spinner
       // ***Originally I used the following line to assign a string aray in the strings.xml to a variable.
        // then put that variable in the spinner adapter in order to create the spinner. Is there any reason to create the variable? Or is what I have now best practice?****
        // howManyStrArray= resources.getStringArray(R.array.saHowManyDice)
        val spinnerHowMany = binding.spnHowManyDice
        spinnerHowMany.adapter = ArrayAdapter(this, R.layout.spinner_items, resources.getStringArray(R.array.saHowManyDice))

        //HowMany Spinner Item Selected
        spinnerHowMany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            //tried to return an int here but that didn't work either.
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                howManySelect(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
          //     Do I need anything in here?
            }
        }

        //Everything to make the Dice Type Spinner
        //diceTypeStrArray = resources.getStringArray(R.array.saDiceType)
        spinnerDiceType = binding.spnDiceType
        spinnerDiceType.adapter = ArrayAdapter(this, R.layout.spinner_items, resources.getStringArray(R.array.saDiceType))
        spinnerDiceType.visibility = View.INVISIBLE

        //DiceType Spinner Item Selected
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
            spinnerDiceType.visibility = View.INVISIBLE
        }else{
            howMany = p2
            spinnerDiceType.visibility = View.VISIBLE
            //originally had an else here to return p2, didn't work
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
        var sum = 0
        if (howMany == 0 || whatType == "") {
            binding.tvSum.visibility = View.GONE
        } else {
            if (howMany == 1) {
                gridView = findViewById(R.id.gvDieResults)
                gridView.numColumns = 1
                binding.tvSum.visibility = View.GONE
                val whatTypeInt = whatType.substring(1).toInt()
                //created the var whatTypeInt because when I just changed whatType to an int, the app would crash after rolling twice. I assume it's because of the previous if statement on line 92. Don't know why tho
                for (i in 1..howMany) {
                    //Could do another instance of removing the variable diceValue altogether and just throwing ((1..whatTypeInt).random()) into setDiceList. Is better to do?
                    val diceValue = (1..whatTypeInt).random()
                    dieList = setDiceList(diceValue, whatTypeInt)
                    sum += diceValue
                }
            }else{
                //Originally only had one initilization of gridView on line 104 "gridView = findViewby...." App would crash if I picked any 2-6 dice to start since it wasn't initialized. Is a better way to do this other than writing the same line twice?
                gridView = findViewById(R.id.gvDieResults)
                binding.tvSum.visibility = View.VISIBLE

                when(howMany){
                    2,3 -> {gridView.numColumns = 1}
                    4,5,6 -> {gridView.numColumns = 2}
                }

                val whatTypeInt: Int = whatType.substring(1).toInt()
                //created the var whatTypeInt because when I just changed whatType to an int, the app would crash after rolling twice. I assume it's because of the previous if statement on line 92. Don't know why tho
                for (i in 1..howMany) {
                    val diceValue = (1..whatTypeInt).random()
                    dieList = setDiceList(diceValue, whatTypeInt)
                    sum += diceValue
                }
            }
            binding.tvSum.append("Sum: $sum")
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

}