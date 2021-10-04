package com.example.Dice_Roller

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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.diceroller_gridlayoutintro.R
import com.example.diceroller_gridlayoutintro.databinding.HomeFragmentBinding
import kotlin.collections.ArrayList


class HomeFragment: Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var homeViewModel: HomeFragViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        initViewModel()

        val darkModeValue = homeViewModel.checkThemeMode()
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
            homeViewModel.vibratePhone()
        }

        //worth putting these into a function?
        var spinnerHowMany = binding.spnHowManyDice
        spinnerHowMany = initSpinners(spinnerHowMany, homeViewModel.getHowManyStrArray())

        var spinnerDiceType = binding.spnDiceType
        spinnerDiceType = initSpinners(spinnerDiceType, homeViewModel.getDiceTypeStrArray())

        setSpinnerSelections(spinnerHowMany, spinnerDiceType)

        spinnerHowMany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(howManyAdapterView: AdapterView<*>?, view: View?, itemPosition: Int, rowId: Long) {
                howMany = homeViewModel.getHowMany(itemPosition)
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

    private fun initViewModel(){
        //requireActivity() can result in null pointer exceptions I believe.
        // Using activity.application led to a cascade of null checks that needed to be made.
        // Need to understand how to properly navigate that
        val factory = HomeFragInjector.Injector.provideHomeFragViewModelFactory(requireActivity().application, resources)
        homeViewModel = ViewModelProvider(this, factory)[HomeFragViewModel::class.java]
    }

    private fun setSpinnerSelections(spinnerHowMany: Spinner, spinnerDiceType: Spinner) {
        spinnerHowMany.setSelection(SPN_HOW_MANY_SELECTION)
        spinnerDiceType.setSelection(SPN_WHAT_TYPE_SELECTION)
    }

    fun getDiceType(position: Int, spinnerDiceType: Spinner): String {

        return spinnerDiceType.getItemAtPosition(position).toString()

    }

    private fun rollDice(howMany: Int, whatType: String) {

        binding.tvSum.text = homeViewModel.resetSumText()

        val dieList: ArrayList<DieModel> = homeViewModel.populateDiceList(howMany, whatType)
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
        gridLayoutManager.spanSizeLookup = DiceSpanLookup(dieList)
        binding.rvDieResults.layoutManager = gridLayoutManager
    }

    companion object{
        const val SPN_HOW_MANY_SELECTION = 0
        const val SPN_WHAT_TYPE_SELECTION = 1
    }
}

