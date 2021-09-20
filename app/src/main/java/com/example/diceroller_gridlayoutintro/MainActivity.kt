package com.example.diceroller_gridlayoutintro

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diceroller_gridlayoutintro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //@@@ktg nice job removing most of your class-level variables!

    //private var howMany = 0

    //**Can save this chat for later*** Originally dieLIst set using the safe call operator and defined as null.
    // Was getting fucky results though when adding results to dieList down in the setDiceList function. Don't know why
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
            // @@@ktg
            // p0, p1, etc are the default names given to these params, but you should almost always
            // give them different names to make your code more readable. What is p0? What does it mean
            // in context of your code?
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                whatType = diceType(p2, spinnerDiceType, whatType)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    // @@@ktg
    // This will come with time, and I can show you some of my code to help with this, but there's a rough
    // set of naming guidelines for things in code that help folks easily understand what's going on.
    // Things that return a boolean, or are boolean varibles, usually start with "is" or "has" or other
    // yay/nay operators (i.e. isVisible, hasShownUserAlert, etc)
    // Functions, for instance, are usually verb phrases, cause functions DO things.
    // So this one might be "getNumberOfDice" or something along those lines.
    // A brief function whose purpose is to return a value usually starts with "get".
    // A function that sets up various UI elements? "setupTextViews" or whatever.
    // A lot of more art than science, but good to at least get in the swing of it.
    fun howManySelect(p2: Int): Int {
        // @@ktg
        // Any reason why this 0 case is being handled separately?
        // Thinking about the UX of your app, do you even need to offer the user the option to select 0?
        if (p2 == 0) {
            dieList = ArrayList()
            //Originally had !! after dieList (because tutorial told me to).
            //I assume there's some big ass conversation here about nullability which we can save for a later date
            binding.gvDieResults.adapter = DieResultAdapter(this, dieList)
            binding.tvSum.text = ""
        }
        return p2
    }

    // @@@ktg
    // getDiceType or similar, for a better name
    // Rename variables as well
    fun diceType(p2: Int, spinnerDiceType: Spinner, whatType: String): String {
        // @@@ktg
        // Can get rid of the variable entirely
        /*
        if (p2 != 0) {
            return spinnerDiceType.getItemAtPosition(p2).toString()
        }
        return ""
        */
        // This works because we only hit the return "" statement if the if statement is false
        // because a return statement ends execution of a function

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

        // @@@ktg
        // We should chat through using adapters. I think we can simplify your usage of the DieResultAdapter
        // significantly. In general, you should submit new lists of items to the adapter rather than creating
        // a new adapter every time the data is changed.
        // The next level of that would be to use a LiveData that we observe - that's for a later date.

        binding.gvDieResults.adapter = DieResultAdapter(this, dieList)

        //images did not show up without these two lines. Why?
        //still fucking with this. dice used to be a gridView. Now recycler view. Does not work how I want yet.
        val gridLayoutManager = GridLayoutManager(applicationContext, 1, GridLayoutManager.VERTICAL, false)
        binding.gvDieResults.layoutManager = gridLayoutManager
        //probably don't need setHasFixedSize(). Leaving for now until I get everything working.
        binding.gvDieResults.setHasFixedSize(false)
    }

    private fun setDiceList(diceValue: Int, diceTypeInt: Int): ArrayList<DieModel> {

        // @@@ktg
        // You can simplify this even further, actually - look at where you use 4/6/8/etc in each of the strings
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


    // @@@ktg
    // In the interest of separation of concerns (big principle of good code design), I would not return
    // the Spinner from this function. It entangles the operations around this Spinner too much.
    // What I would do: create a function that initializes any Spinner. It takes two params,
    // a Spinner and an Array. It will invert the control flow of this code - in your onCreate,
    // you'll get the spinner using the binding, get the array of strings, and then pass both into
    // the new function that you create. Can chat through it more if that's still confusing.
    private fun initHowManySpinner(): Spinner {

        // ***Originally I used the following line to assign a string array in the strings.xml to a variable.
        // then put that variable in the spinner adapter in order to create the spinner. Is there any reason to create the variable? Or is what I have now best practice?****
        // *** LEAVE THIS LINE IN, FIX LATER***
        var howManyStrArray= resources.getStringArray(R.array.saHowManyDice)
        val spinnerHowMany = binding.spnHowManyDice
        spinnerHowMany.adapter = ArrayAdapter(this, R.layout.spinner_items, howManyStrArray)

        return spinnerHowMany
    }

    // @@@ktg
    // Same deal, you'll use the same function that I talked about above
    private fun initWhatTypeSpinner(): Spinner {

        val spinnerDiceType = binding.spnDiceType
        spinnerDiceType.adapter = ArrayAdapter(this, R.layout.spinner_items, resources.getStringArray(R.array.saDiceType))

        return spinnerDiceType
    }

    // @@@ktg
    // looks like this is being reworked already so I'll hold off on thoughts for it
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
        // @@@ktg there's probably a way to not use substring (as in, refactor how some code works
        // so that you don't need to do an operation that grabs part of a different value).
        // Using hard-coded indices should set off warning bells: it's fragile and usually not best practice.
        val whatTypeInt: Int = whatType.substring(1).toInt()
        for (i in 1..howMany) {
            val diceValue = (1..whatTypeInt).random()
            sum += diceValue
            dieList = setDiceList(diceValue, whatTypeInt)
        }
        // @@@ktg look into using strings.xml for your strings
        // Relatedly, use dimens.xml for dimensions you have in your layouts
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


