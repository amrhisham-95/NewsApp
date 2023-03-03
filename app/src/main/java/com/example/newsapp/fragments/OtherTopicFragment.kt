package com.example.newsapp.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.OnRecyclerViewClick
import com.example.newsapp.adapters.RecyclerAdapter
import com.example.newsapp.databinding.FragmentOtherTopicBinding
import com.example.newsapp.models.News
import com.example.newsapp.ui.Constants
import com.example.newsapp.ui.DrawerLocker
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.viewModels.RetrofitViewModel
import com.example.newsapp.viewModels.RoomDatabaseViewModel


class OtherTopicFragment : Fragment(), OnRecyclerViewClick {
    private lateinit var binding: FragmentOtherTopicBinding
    private val viewModel: RetrofitViewModel by viewModels()
    private val roomViewModel: RoomDatabaseViewModel by viewModels()
    private lateinit var adapterTopics: RecyclerAdapter
    var lang = ""
    var country = ""
    var category = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_other_topic, container, false)

        //to hide navigation drawer from fragment you want to hide
        (activity as DrawerLocker?)!!.setDrawerLocked(true)

        //to remove toggle from the layout toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.roomViewModel = roomViewModel

        languagesSpinnerOtherTopics()
        countriesSpinnerOtherTopics()
        categoriesSpinnerOtherTopics()


        binding.ShowNewsBtnTopics.setOnClickListener {
            if (binding.startEnterDateOtherTopics.text.isEmpty() || binding.endEnterDateOtherTopics.text.isEmpty()) {
                Toast.makeText(
                    requireContext(), "Please Enter The Empty Field And Press Show Button Again",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                binding.ShowNewsBtnTopics.visibility = View.INVISIBLE
                binding.backBtnTopics.visibility = View.VISIBLE

                //to delete all news that stored in database
                roomViewModel.deleteAllNewsDetails()

                //get the data of (today) from the service and put it into the xml (for Egypt , Saudi Arabia , Emirates , Morocco)
                getDataOtherTopics(
                    Constants.API_KEY,
                    category,
                    lang,
                    country,
                    Constants.MAX_NUMBER_NEWS,
                    binding.startEnterDateOtherTopics.text.toString(),
                    binding.endEnterDateOtherTopics.text.toString(),
                    "title,description,content"
                )
                viewModel.mutableLiveData.observe(viewLifecycleOwner) {
                    it.articles
                    roomViewModel.addNewsDataViewModel(it.articles)
                }

                roomViewModel.readAllNewsDataViewModel.observe(viewLifecycleOwner) {
                    binding.apply {
                        if (it.isEmpty()) {
                            binding.noDataTextDetaiTopics.visibility = View.VISIBLE
                        } else {
                            binding.noDataTextDetaiTopics.visibility = View.INVISIBLE

                            //PassingArrayListThroughRecyclerAdapterForUrlOfTheImage
                            val arrayList :ArrayList<String> =createArrayToPassingItThroughRecyclerAdapterForUrlOfTheImage(it)

                            recyclerViewDetailTopics.setHasFixedSize(true)
                            recyclerViewDetailTopics.layoutManager =
                                LinearLayoutManager(requireContext())

                            adapterTopics =
                                RecyclerAdapter(
                                    it,
                                    this@OtherTopicFragment,
                                    arrayList
                                )

                            recyclerViewDetailTopics.adapter = adapterTopics

                            roomViewModel?.readAllNewsDataViewModel?.observe(viewLifecycleOwner) {
                                adapterTopics.updateItem(it)
                            }
                        }
                    }

                }
            }
        }


        binding.backBtnTopics.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        binding.instructionsFloatingOtherTopics.setOnClickListener {
            showMeDialog()
        }

    }

    //for make Dialog
    private fun showMeDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.apply {

            setTitle("Instructions")
            setMessage(
                R.string.instructionsText
            )
            setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(requireContext(), "Look At The Shortcuts", Toast.LENGTH_LONG)
                    .show()

                findNavController().navigate(OtherTopicFragmentDirections.actionOtherTopicFragmentToShortcutsFragment(otherTopicsVariable = 3))

            })

            setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                val dialog = builder.create()
                dialog.show()
            })
        }

        val dialog = builder.create()
        dialog.show()
    }


    private fun getDataOtherTopics(
        apikey: String,
        category : String,
        lang :String,
        country :String,
        max :Int,
        from : String,
        to :String,
        nullable :String
    ) {
        viewModel.getDataRetrofitViewModel(apikey,category,lang,country,max,from,to,nullable)
    }


    //click on recycler view
    override fun onClicked(position: Int) {


    }

    private fun languagesSpinnerOtherTopics(){

        val mySpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.languagesSpinner,android.R.layout.simple_spinner_item)

        binding.spinnerLanguagesOtherTopics.apply {
            adapter=mySpinnerAdapter
            onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when(position){
                        0 -> {Toast.makeText(requireContext(),"PLEASE CHOOSE THE LANGUAGE",Toast.LENGTH_SHORT).show()}
                        1 -> {lang="ar"}
                        2 -> {lang="en"}
                        3 -> {lang="de"}
                        4 -> {lang="fr"}
                        5 -> {lang="zh"}
                        6 -> {lang="it"}
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }

    private fun countriesSpinnerOtherTopics(){

        val mySpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.countriesSpinner,android.R.layout.simple_spinner_item)

        binding.countriesSpinnerOtherTopics.apply {
            adapter=mySpinnerAdapter
            onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            Toast.makeText(requireContext(),"PLEASE CHOOSE THE COUNTRY",Toast.LENGTH_SHORT).show()
                        }
                        1 -> {
                            country = "eg"
                        }
                        2 -> {
                            country = "us"
                        }
                        3 -> {
                            country = "gb"
                        }
                        4 -> {
                            country = "it"
                        }
                        5 -> {
                            country = "de"
                        }
                        6 -> {
                            country = "cn"
                        }
                        7 -> {
                            country = "fr"
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }

    private fun categoriesSpinnerOtherTopics(){

        val mySpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.languagesSpinner,android.R.layout.simple_spinner_item)

        binding.spinnerLanguagesOtherTopics.apply {
            adapter=mySpinnerAdapter
            onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {Toast.makeText(requireContext(),"PLEASE CHOOSE THE TOPIC",Toast.LENGTH_SHORT).show()}

                        1 -> {
                            category = Constants.SPORTS
                        }
                        2 -> {
                            category = Constants.SCIENCES
                        }
                        3 -> {
                            category = Constants.GENERAL
                        }
                        4 -> {
                            category = Constants.BUSINESS
                        }
                        5 -> {
                            category = Constants.ENTERTAINMENT
                        }
                        6 -> {
                            category = Constants.HEALTH
                        }
                        7 -> {
                            category = Constants.TECHNOLOGY
                        }
                        8 -> {
                            category = Constants.NATION
                        }
                        9 -> {
                            category = Constants.WORLD
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }

    private fun createArrayToPassingItThroughRecyclerAdapterForUrlOfTheImage(newsList: List<News>) :ArrayList<String> {
        var i = 0
        var mediaTypeArray = ArrayList<String>()
        for (i in newsList.indices) {
            val mediaType = newsList[i].image
            mediaTypeArray.add(mediaType)
        }
        i++
        return mediaTypeArray
    }

    //to hide navigation drawer from fragment you want to hide
    override fun onDestroy() {
        super.onDestroy()
        (activity as DrawerLocker?)!!.setDrawerLocked(false)
    }
}