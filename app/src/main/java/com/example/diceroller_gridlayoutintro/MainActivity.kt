package com.example.diceroller_gridlayoutintro

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diceroller_gridlayoutintro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //private var howMany = 0

    //**Can save this chat for later*** Originally dieLIst set using the safe call operator and defined as null. Was getting fucky results though when adding results to dieList down in the setDiceList function. Don't know why
    private var dieList: ArrayList<DieModel> = ArrayList()
   // private var whatType = ""
    //private lateinit var spinnerHowMany: Spinner
    //private lateinit var spinnerDiceType: Spinner
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var howMany = 0
        var whatType = ""
        binding.btnRollDice.setOnClickListener { rollDice(howMany, whatType) }

        val spinnerHowMany = initHowManySpinner()
        val spinnerDiceType = initWhatTypeSpinner()

        spinnerHowMany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                howMany = howManySelect(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        spinnerDiceType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                whatType = diceType(p2, spinnerDiceType, whatType)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    fun howManySelect(p2: Int): Int {

        if (p2 == 0) {
            dieList = ArrayList()
            //Originally had !! after dieList (because tutorial told me to).
            //I assume there's some big ass conversation here about nullability which we can save for a later date
            binding.gvDieResults.adapter = DieResultAdapter(this, dieList)
            binding.tvSum.text = ""
        }
        return p2
    }

    fun diceType(p2: Int, spinnerDiceType: Spinner, whatType: String): String {

        //didn't want to have whatTypeDummy here. Planned to justt have whatType = spinnerDiceType....
        //kept getting error of "Val cannot be reassigned" as an error. No idea why. It's not a val?
        var whatTypeDummy = ""

        if (p2 != 0) {
            whatTypeDummy = spinnerDiceType.getItemAtPosition(p2).toString()
        }
        return whatTypeDummy
    }

    fun rollDice(howMany: Int, whatType: String) {

        dieList = ArrayList()
        binding.tvSum.text = ""

        dieList = getDiceValues(howMany, whatType)
        setRecyclerView(howMany, whatType)
        binding.tvSum.visibility = sumVisibility(howMany, whatType)

        binding.gvDieResults.adapter = DieResultAdapter(this, dieList)

        //images did not show up without these two lines. Why?
        //still fucking with this. dice used to be a gridView. Now recycler view. Does not work how I want yet.
        val gridLayoutManager = GridLayoutManager(applicationContext, 1, GridLayoutManager.VERTICAL, false)
        binding.gvDieResults.layoutManager = gridLayoutManager
        //probably don't need setHasFixedSize(). Leaving for now until I get everything working.
        binding.gvDieResults.setHasFixedSize(false)
    }

    private fun setDiceList(diceValue: Int, diceTypeInt: Int): ArrayList<DieModel> {

        when (diceTypeInt) {
            4 -> dieList.add(DieModel(resources.getIdentifier("d4_$diceValue", "drawable", packageName), "d4_+$diceValue"))
            6 -> dieList.add(DieModel(resources.getIdentifier("d6_$diceValue", "drawable", packageName), "d6_+$diceValue"))
            8 -> dieList.add(DieModel(resources.getIdentifier("d8_$diceValue", "drawable", packageName), "d8_+$diceValue"))
            10 -> dieList.add(DieModel(resources.getIdentifier("d10_$diceValue", "drawable", packageName), "d10_+$diceValue"))
            12 -> dieList.add(DieModel(resources.getIdentifier("d12_$diceValue", "drawable", packageName), "d12_+$diceValue"))
            20 -> dieList.add(DieModel(resources.getIdentifier("d20_$diceValue", "drawable", packageName), "d20_+$diceValue"))
        }
        return dieList
    }

    private fun initHowManySpinner(): Spinner {

        // ***Originally I used the following line to assign a string array in the strings.xml to a variable.
        // then put that variable in the spinner adapter in order to create the spinner. Is there any reason to create the variable? Or is what I have now best practice?****
        // *** LEAVE THIS LINE IN, FIX LATER***
        var howManyStrArray= resources.getStringArray(R.array.saHowManyDice)
        val spinnerHowMany = binding.spnHowManyDice
        spinnerHowMany.adapter = ArrayAdapter(this, R.layout.spinner_items, howManyStrArray)

        return spinnerHowMany
    }

    private fun initWhatTypeSpinner(): Spinner {

        val spinnerDiceType = binding.spnDiceType
        spinnerDiceType.adapter = ArrayAdapter(this, R.layout.spinner_items, resources.getStringArray(R.array.saDiceType))

        return spinnerDiceType
    }

    private fun setRecyclerView(howMany: Int, whatType: String) {

        //Leaving this until I fix formatting of dice.
        binding.tvSum.visibility = sumVisibility(howMany, whatType)
        var gridView: RecyclerView = findViewById(R.id.gvDieResults)
        when (howMany) {
            1 -> {
                // gridView.numColumns = 1
            }

            2, 3 -> {
                // gridView.numColumns = 1
            }

            4, 5, 6 -> {
                //  gridView.numColumns = 2
            }
        }
    }

    private fun getDiceValues(howMany: Int, whatType: String): ArrayList<DieModel> {
        var sum = 0
        val whatTypeInt: Int = whatType.substring(1).toInt()
        for (i in 1..howMany) {
            val diceValue = (1..whatTypeInt).random()
            sum += diceValue
            dieList = setDiceList(diceValue, whatTypeInt)
        }
        binding.tvSum.append("Sum: $sum")
        return dieList
    }

    private fun sumVisibility(howMany: Int, whatType: String): Int {
        if (howMany == 0 || howMany == 1 || whatType == "") {
            return View.GONE
        } else {
            return View.VISIBLE
        }
    }
}


