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
        val darkModeValue = checkThemeMode()
        homeViewModel.setThemeMode(darkModeValue)

        binding = HomeFragmentBinding.inflate(inflater, container, false )

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        var howMany = homeViewModel.howMany
        var whatType = homeViewModel.whatType

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
                howMany = homeViewModel.getHowMany(itemPosition)
            }

            override fun onNothingSelected(howManyAdapterView: AdapterView<*>?) {
            }
        }
        spinnerDiceType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(diceTypeAdapterview: AdapterView<*>?, view: View?, itemPosition: Int, rowId: Long) {
                //can't put any of this in view model because it's taking spinnerDiceType correct?
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

    private fun checkThemeMode(): Boolean {

        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getBoolean("dark_mode", true)

    }

    fun getDiceType(p2: Int, spinnerDiceType: Spinner): String {

        return spinnerDiceType.getItemAtPosition(p2).toString()

    }

    private fun rollDice(howMany: Int, whatType: String) {

        binding.tvSum.text = homeViewModel.resetSumText()

        dieList = homeViewModel.getDiceValues(resources, howMany, whatType)
        binding.tvSum.append(homeViewModel.returnSumText())
        binding.tvSum.visibility = homeViewModel.setSumVisibility(howMany, whatType)
        binding.rvDieResults.adapter = DieResultAdapter(requireContext(), dieList)

        setRecyclerView(dieList)

    }

    private fun initSpinners(spinner: Spinner, strArray: Array<String>): Spinner {

        spinner.adapter = ArrayAdapter(requireContext(), R.layout.spinner_items, strArray)
        return spinner

    }

    private fun setRecyclerView(dieList: ArrayList<DieModel>){

        val gridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        //can I put DiceSpanLookup() into the view model? How does referencing a class from another class work?
        gridLayoutManager.spanSizeLookup = DiceSpanLookup(dieList)
        binding.rvDieResults.layoutManager = gridLayoutManager
    }

  //should this go in view model? I feel like no because it's dependent on the vibrator object.
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

    //Should this go in the view model? I feel like no.
    private fun loadVibrateSetting(): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        return sp.getBoolean("vibrate", true)
    }

    //Should this go in the view Model? If I move the "when(dieListSize)" chunk to the view model, then I would have to pass homeViewModel to the class and getSpanSize, which would mean it's no longer override the abstract function
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