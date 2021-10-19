package com.example.dice_Roller

import android.os.*
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dice_Roller.databinding.HomeFragmentBinding

class HomeFragment: Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var homeViewModel: HomeFragViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        initViewModel()
        setHasOptionsMenu(true)

        val darkModeValue = homeViewModel.checkThemeMode()
        homeViewModel.setThemeMode(darkModeValue)

        binding.homeviewmodel = homeViewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var howMany = homeViewModel.howMany
        var whatType = homeViewModel.whatType

        binding.btnRollDice.setOnClickListener {
            homeViewModel.wasRollBtnPressed.postValue(homeViewModel.wasRollBtnPressed.value?.let {!it})
            homeViewModel.populateDiceList(howMany, whatType)
            homeViewModel.vibratePhone()
        }

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

        homeViewModel.dieListLive.observe(viewLifecycleOwner, {
            binding.rvDieResults.adapter = DieResultAdapter(it)
            setRecyclerView(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.topbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.settings -> findNavController().navigate(R.id.navigateToSettingsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViewModel(){
        val factory = activity?.let { HomeFragInjector.Injector.provideHomeFragViewModelFactory(it.application, resources) }
        homeViewModel =
            factory?.let { ViewModelProvider(this, it) }?.get(HomeFragViewModel::class.java) as HomeFragViewModel
    }

    private fun setSpinnerSelections(spinnerHowMany: Spinner, spinnerDiceType: Spinner) {
        spinnerHowMany.setSelection(SPN_HOW_MANY_SELECTION)
        spinnerDiceType.setSelection(SPN_WHAT_TYPE_SELECTION)
    }

       fun getDiceType(position: Int, spinnerDiceType: Spinner): String {

           return spinnerDiceType.getItemAtPosition(position).toString()

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

