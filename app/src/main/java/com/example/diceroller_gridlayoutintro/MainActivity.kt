package com.example.diceroller_gridlayoutintro

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.diceroller_gridlayoutintro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var howMany = 0
    //Originally dieLIst set using the safe call operator and defined as null. Was getting fucky results though when adding results to dieList down in the setDiceList function. Don't know why
    private var dieList: ArrayList<DieModel> = ArrayList()
    private var whatType = ""
    private lateinit var spinnerDiceType: Spinner
   // lateinit var dieResults: GridView
    //Needed to initialize the howManyStrArray first before I could use howManySelect function. Why?
    private var howManyStrArray: Array<String> = emptyArray()
    private var diceTypeStrArray: Array<String> = emptyArray()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Everything to make the How Many Spinner
        howManyStrArray= resources.getStringArray(R.array.saHowManyDice)
        val spinnerHowMany = binding.spnHowManyDice
        val howManyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, howManyStrArray)
        spinnerHowMany.adapter = howManyAdapter
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
        diceTypeStrArray = resources.getStringArray(R.array.saDiceType)
        spinnerDiceType = binding.spnDiceType
        val diceTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, diceTypeStrArray)
        spinnerDiceType.adapter = diceTypeAdapter
        spinnerDiceType.visibility = View.INVISIBLE
        //DiceType Spinner Item Selected
        spinnerDiceType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            //tried to return an int here but that didn't work either.
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                whatType(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
          //      binding.tvTest.text = ""
            }

        }
    }

    fun howManySelect(p2: Int){
        if(p2 == 0) {
            dieList = ArrayList()
            //Originally had !! after dieList
            val dieListAdapter = DieResultAdapter(this, dieList)
            binding.gvDieResults.adapter = dieListAdapter
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
       // binding.tvTest.text = ""
        whatType = ""
    }else {
        whatType = spinnerDiceType.getItemAtPosition(p2).toString()
    }

    //why do I need view: View? Android Studio gives me a lil warning guy saying parameter view is never used, but if you take it away, then the app won't load
    fun rollDice(view: View) {
      dieList = ArrayList()

       // binding.tvTest.text = ""
        binding.tvSum.text = ""

        //var diceResult = arrayOf<Int>()
        var sum = 0
        if (howMany == 0 || whatType == "") {
          //  binding.tvTest.text = ""
            binding.tvSum.visibility = View.GONE
        } else {
            if (howMany == 1) {
                binding.tvSum.visibility = View.INVISIBLE
                val whatTypeInt = whatType.substring(1).toInt()
                //created the var whatTypeInt because when I just changed whatType to an int, the app would crash after rolling twice. I assume it's because of the previous if statement on line 92. Don't know why tho
                for (i in 1..howMany) {
                    val diceValue = (1..whatTypeInt).random()
                    dieList = setDiceList(diceValue, whatTypeInt)
                  //  binding.tvTest.append("Die #$i: $diceValue")
                  //  binding.tvTest.append("\n")
                    sum += diceValue
                }
            }else{
                binding.tvSum.visibility = View.VISIBLE
                val whatTypeInt: Int = whatType.substring(1).toInt()
                //created the var whatTypeInt because when I just changed whatType to an int, the app would crash after rolling twice. I assume it's because of the previous if statement on line 92. Don't know why tho
                for (i in 1..howMany) {
                    val diceValue = (1..whatTypeInt).random()
                   // setDiceList(diceValue)
                    dieList = setDiceList(diceValue, whatTypeInt)
                  //  binding.tvTest.append("Die #$i: $diceValue")
                  //  binding.tvTest.append("\n")
                    sum += diceValue
                }
                //diceResult.set(i-1, diceValue)
            }
            binding.tvSum.append("Sum: $sum")

            // binding.tvTest.append(diceResult.forEach{ i -> print("$i") }.toString())
        }

        //add !! operator to dieList to remove error. why?
        val dieListAdapter = DieResultAdapter(this, dieList)
        binding.gvDieResults.adapter = dieListAdapter
    }

     private fun setDiceList(diceValue: Int, diceTypeInt: Int): ArrayList<DieModel>{

         val d4ImageNames = arrayOf(
            "d4_1",
            "d4_2",
            "d4_3",
            "d4_4",
        )
         val d4Images = intArrayOf(
             R.drawable.d4_1,
             R.drawable.d4_2,
             R.drawable.d4_3,
             R.drawable.d4_4,
         )

         val d6ImageNames = arrayOf(
            "d6_1",
            "d6_2",
            "d6_3",
            "d6_4",
            "d6_5",
            "d6_6",
        )
         val d6Images = intArrayOf(
            R.drawable.d6_1,
            R.drawable.d6_2,
            R.drawable.d6_3,
            R.drawable.d6_4,
            R.drawable.d6_5,
            R.drawable.d6_6, )

         val d8ImageNames = arrayOf(
             "d8_1",
             "d8_2",
             "d8_3",
             "d8_4",
             "d8_5",
             "d8_6",
             "d8_7",
             "d8_8",
         )
         val d8Images = intArrayOf(
             R.drawable.d8_1,
             R.drawable.d8_2,
             R.drawable.d8_3,
             R.drawable.d8_4,
             R.drawable.d8_5,
             R.drawable.d8_6,
             R.drawable.d8_7,
             R.drawable.d8_8,)

         val d10ImageNames = arrayOf(
             "d10_1",
             "d10_2",
             "d10_3",
             "d10_4",
             "d10_5",
             "d10_6",
             "d10_7",
             "d10_8",
             "d10_9",
             "d10_10",
         )
         val d10Images = intArrayOf(
             R.drawable.d10_1,
             R.drawable.d10_2,
             R.drawable.d10_3,
             R.drawable.d10_4,
             R.drawable.d10_5,
             R.drawable.d10_6,
             R.drawable.d10_7,
             R.drawable.d10_8,
             R.drawable.d10_9,
             R.drawable.d10_10,)

         val d12ImageNames = arrayOf(
             "d12_1",
             "d12_2",
             "d12_3",
             "d12_4",
             "d12_5",
             "d12_6",
             "d12_7",
             "d12_8",
             "d12_9",
             "d12_10",
             "d12_11",
             "d12_12",
         )
         val d12Images = intArrayOf(
             R.drawable.d12_1,
             R.drawable.d12_2,
             R.drawable.d12_3,
             R.drawable.d12_4,
             R.drawable.d12_5,
             R.drawable.d12_6,
             R.drawable.d12_7,
             R.drawable.d12_8,
             R.drawable.d12_9,
             R.drawable.d12_10,
             R.drawable.d12_11,
             R.drawable.d12_12, )

         val d20ImageNames = arrayOf(
             "d20_1",
             "d20_2",
             "d20_3",
             "d20_4",
             "d20_5",
             "d20_6",
             "d20_7",
             "d20_8",
             "d20_9",
             "d20_10",
             "d20_11",
             "d20_12",
             "d20_13",
             "d20_14",
             "d20_15",
             "d20_16",
             "d20_17",
             "d20_18",
             "d20_19",
             "d20_20",
         )
         val d20Images = intArrayOf(
             R.drawable.d20_1,
             R.drawable.d20_2,
             R.drawable.d20_3,
             R.drawable.d20_4,
             R.drawable.d20_5,
             R.drawable.d20_6,
             R.drawable.d20_7,
             R.drawable.d20_8,
             R.drawable.d20_9,
             R.drawable.d20_10,
             R.drawable.d20_11,
             R.drawable.d20_12,
             R.drawable.d20_13,
             R.drawable.d20_14,
             R.drawable.d20_15,
             R.drawable.d20_16,
             R.drawable.d20_17,
             R.drawable.d20_18,
             R.drawable.d20_19,
             R.drawable.d20_20, )

         when(diceTypeInt){
          4 -> dieList.add(DieModel(d4Images[diceValue-1], d4ImageNames[diceValue-1]))
          6 -> dieList.add(DieModel(d6Images[diceValue-1], d6ImageNames[diceValue-1]))
          8 -> dieList.add(DieModel(d8Images[diceValue-1], d8ImageNames[diceValue-1]))
          10 -> dieList.add(DieModel(d10Images[diceValue-1], d10ImageNames[diceValue-1]))
          12 -> dieList.add(DieModel(d12Images[diceValue-1], d12ImageNames[diceValue-1]))
          20 -> dieList.add(DieModel(d20Images[diceValue-1], d20ImageNames[diceValue-1]))
         }


         return dieList
    }//fuckScrewn

}