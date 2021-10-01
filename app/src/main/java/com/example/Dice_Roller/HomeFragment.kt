package com.example.Dice_Roller

import android.content.Context
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.diceroller_gridlayoutintro.R
import com.example.diceroller_gridlayoutintro.databinding.HomeFragmentBinding
import kotlin.collections.ArrayList


class HomeFragment: Fragment() {
    //**Can save this chat for later*** Originally dieLIst set using the safe call operator and defined as null. Was getting fucky results though when adding results to dieList down in the setDiceList function. Don't know why
    private var dieList: ArrayList<DieModel> = ArrayList()
    private lateinit var binding: HomeFragmentBinding
    private val homeViewModel: HomeFragViewModel by lazy{
       ViewModelProvider(this)[HomeFragViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        checkThemeMode()
        binding = HomeFragmentBinding.inflate(inflater, container, false )

       // binding.homeviewmodel = homeViewModel
      //  binding.lifecycleOwner = this

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        var howMany = 1
        var whatType = "d6"

        binding.btnRollDice.setOnClickListener {
            rollDice(howMany, whatType)
            vibratePhone()
        }

        //worth putting these into a function?
        var spinnerHowMany = bindSpinners(1)
        spinnerHowMany = initSpinners(spinnerHowMany, homeViewModel.getHowManyStrArray(resources))

        var spinnerDiceType = bindSpinners(2)
        spinnerDiceType = initSpinners(spinnerDiceType, homeViewModel.getDiceTypeStrArray(resources))

        setSpinnerSelections(spinnerHowMany, spinnerDiceType)

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

    private fun setSpinnerSelections(spinnerHowMany: Spinner, spinnerDiceType: Spinner) {
        spinnerHowMany.setSelection(0)
        spinnerDiceType.setSelection(1)
    }

    private fun bindSpinners(spinnerNumber: Int): Spinner {
        if (spinnerNumber == 1) {
            return binding.spnHowManyDice
        } else {
            return binding.spnDiceType
        }
    }

    private fun checkThemeMode(){
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val darkModeValue = sp.getBoolean("dark_mode", true)

        if(darkModeValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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

    private fun initSpinners(spinner: Spinner, strArray: Array<String>): Spinner {

        spinner.adapter = ArrayAdapter(requireContext(), R.layout.spinner_items, strArray)
        return spinner

    }

    private fun getDiceValues(howMany: Int, whatType: String): ArrayList<DieModel> {

        var sum = 0
        val whatTypeInt: Int = whatType.substring(1).toInt()
        for (i in 1..howMany) {
            val diceValue = (1..whatTypeInt).random()
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

    @RequiresApi(Build.VERSION_CODES.S)
    fun vibratePhone() {
        //Is this best practice to have the function in the if statement? Or should i pull it out and set a variable equal to the return of the function and then use the variable in the if?
        if (loadVibrateSetting()) {
            val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator?.vibrate(100)
            }
        }
    }

    private fun loadVibrateSetting(): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getBoolean("vibrate", true)
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