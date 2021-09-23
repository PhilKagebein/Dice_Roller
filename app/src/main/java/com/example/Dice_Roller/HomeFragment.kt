package com.example.Dice_Roller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.diceroller_gridlayoutintro.R
import com.example.diceroller_gridlayoutintro.databinding.HomeFragmentBinding


class HomeFragment: Fragment() {
    //**Can save this chat for later*** Originally dieLIst set using the safe call operator and defined as null. Was getting fucky results though when adding results to dieList down in the setDiceList function. Don't know why
    private var dieList: ArrayList<DieModel> = ArrayList()
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = HomeFragmentBinding.inflate(inflater, container, false )

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        var howMany = 1
        var whatType = "d6"

        binding.btnRollDice.setOnClickListener { rollDice(howMany, whatType) }

//        val howManyStrArray = resources.getStringArray(R.array.saHowManyDice)
//        var spinnerHowMany = binding.spnHowManyDice
//        spinnerHowMany = initSpinners(spinnerHowMany, howManyStrArray)
//        spinnerHowMany.setSelection(0)
//
//        val diceTypeStrArray = resources.getStringArray(R.array.saDiceType)
//        var spinnerDiceType = binding.spnDiceType
//        spinnerDiceType = initSpinners(spinnerDiceType, diceTypeStrArray)
//        spinnerDiceType.setSelection(1)

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
                whatType = getDiceType(itemPosition, spinnerDiceType)
            }

            override fun onNothingSelected(diceTypeAdapterView: AdapterView<*>?) {
            }
        }

    }

    fun getDiceType(p2: Int, spinnerDiceType: Spinner): String {

        return spinnerDiceType.getItemAtPosition(p2).toString()

    }

    private fun rollDice(howMany: Int, whatType: String) {

        dieList = ArrayList()
        binding.tvSum.text = ""

        dieList = getDiceValues(howMany, whatType)
        binding.tvSum.visibility = setSumVisibility(howMany, whatType)
        //Google requireContext()
        binding.rvDieResults.adapter = DieResultAdapter(requireContext(), dieList)

        setRecyclerView(dieList)

    }

    private fun setDiceList(diceValue: Int, diceTypeInt: Int): ArrayList<DieModel> {

        dieList.add(DieModel(resources.getIdentifier("d${diceTypeInt}_$diceValue", "drawable", requireContext().packageName), "d${diceTypeInt}_+$diceValue"))

        return dieList
    }

//    private fun initSpinners(spinner: Spinner, strArray: Array<String>): Spinner {
//
//        spinner.adapter = ArrayAdapter(requireContext(), R.layout.spinner_items, strArray)
//        return spinner
//
//    }

    private fun initHowManySpinner(): Spinner {


        val howManyStrArray = resources.getStringArray(R.array.saHowManyDice)
        val spinnerHowMany = binding.spnHowManyDice
        spinnerHowMany.adapter = ArrayAdapter(requireContext(), R.layout.spinner_items, howManyStrArray)
        spinnerHowMany.setSelection(0)

        return spinnerHowMany
    }

    private fun initWhatTypeSpinner(): Spinner {

        val diceTypeStrArray = resources.getStringArray(R.array.saDiceType)
        val spinnerDiceType = binding.spnDiceType
        spinnerDiceType.adapter = ArrayAdapter(requireContext(), R.layout.spinner_items, diceTypeStrArray)
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

    private fun setSumVisibility(howMany: Int, whatType: String): Int {
        if (howMany == 0 || howMany == 1 || whatType == "") {
            return View.GONE
        } else {
            return View.VISIBLE
        }
    }

    private fun setRecyclerView(dieList: ArrayList<DieModel>){

        val gridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
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