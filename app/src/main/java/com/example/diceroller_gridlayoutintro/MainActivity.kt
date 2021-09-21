package com.example.diceroller_gridlayoutintro

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.diceroller_gridlayoutintro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //**Can save this chat for later*** Originally dieLIst set using the safe call operator and defined as null. Was getting fucky results though when adding results to dieList down in the setDiceList function. Don't know why
    private var dieList: ArrayList<DieModel> = ArrayList()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var howMany = 1
        var whatType = "d6"
        binding.btnRollDice.setOnClickListener { rollDice(howMany, whatType) }

        val spinnerHowMany = initHowManySpinner()
        val spinnerDiceType = initWhatTypeSpinner()

        spinnerHowMany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            //Need explanation on what p0/AdapterView, p1/View, & rowId are doing
            override fun onItemSelected(howManyAdapterView: AdapterView<*>?, view: View?, itemPosition: Int, rowId: Long) {
                howMany = itemPosition + 1
            }

            override fun onNothingSelected(howManyAdapterView: AdapterView<*>?) {
            }
        }
        spinnerDiceType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(diceTypeAdapterview: AdapterView<*>?, view: View?, itemPosition: Int, rowId: Long) {
                whatType = diceType(itemPosition, spinnerDiceType, whatType)
            }

            override fun onNothingSelected(diceTypeAdapterView: AdapterView<*>?) {
            }

        }
    }

    fun diceType(p2: Int, spinnerDiceType: Spinner, whatType: String): String {

        var whatTypeDummy = ""
        whatTypeDummy = spinnerDiceType.getItemAtPosition(p2).toString()
        return whatTypeDummy
    }

    private fun rollDice(howMany: Int, whatType: String) {

        dieList = ArrayList()
        binding.tvSum.text = ""

        dieList = getDiceValues(howMany, whatType)
        binding.tvSum.visibility = sumVisibility(howMany, whatType)
        binding.rvDieResults.adapter = DieResultAdapter(this, dieList)

        setRecyclerView(dieList)

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

        val howManyStrArray = resources.getStringArray(R.array.saHowManyDice)
        val spinnerHowMany = binding.spnHowManyDice
        spinnerHowMany.adapter = ArrayAdapter(this, R.layout.spinner_items, howManyStrArray)
        spinnerHowMany.setSelection(0)

        return spinnerHowMany
    }

    private fun initWhatTypeSpinner(): Spinner {

        val diceTypeStrArray = resources.getStringArray(R.array.saDiceType)
        val spinnerDiceType = binding.spnDiceType
        spinnerDiceType.adapter = ArrayAdapter(this, R.layout.spinner_items, diceTypeStrArray)
        spinnerDiceType.setSelection(1)

        return spinnerDiceType
    }

    private fun getDiceValues(howMany: Int, whatType: String): ArrayList<DieModel> {

        var sum = 0
        val whatTypeInt: Int = whatType.substring(1).toInt()
        for (i in 1..howMany) {
            var diceValue = (1..whatTypeInt).random()
            dieList = setDiceList(diceValue, whatTypeInt)
            sum = calculateSum(sum, diceValue)
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

    private fun setRecyclerView(dieList: ArrayList<DieModel>){

        val gridLayoutManager = GridLayoutManager(applicationContext, 2, GridLayoutManager.VERTICAL, false)
        gridLayoutManager.spanSizeLookup = DiceSpanLookup(dieList)
        binding.rvDieResults.layoutManager = gridLayoutManager
    }

    private fun calculateSum(sum: Int, diceValue: Int): Int {
        var sumDummy = sum
        sumDummy += diceValue
        return sumDummy
    }

    class DiceSpanLookup(dieList: ArrayList<DieModel>): GridLayoutManager.SpanSizeLookup() {

       private var dieListSize = dieList.size

        override fun getSpanSize(position: Int): Int {

            when(dieListSize) {
                4, 6 -> {return 1}
                5 -> {
                    if(position == 2){
                        return 2
                    }else {
                        return 1
                    }
                }
            }
            return 2
        }
    }
}


