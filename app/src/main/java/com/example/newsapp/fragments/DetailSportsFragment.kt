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
import com.example.newsapp.databinding.FragmentDetailSportsBinding
import com.example.newsapp.models.News
import com.example.newsapp.ui.Constants
import com.example.newsapp.ui.DrawerLocker
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.viewModels.RetrofitViewModel
import com.example.newsapp.viewModels.RoomDatabaseViewModel



class DetailSportsFragment : Fragment(), OnRecyclerViewClick {
    private lateinit var binding: FragmentDetailSportsBinding
    private val viewModel: RetrofitViewModel by viewModels()
    private val roomViewModel: RoomDatabaseViewModel by viewModels()
    private lateinit var adapterSports: RecyclerAdapter
    var lang = ""
    var country = ""
    var category = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_detail_sports, container, false)

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

        //to add languagesSpinner
        languagesSpinner()
        //to add countriesSpinner
        countriesSpinner()


        binding.ShowNewsBtnSports.setOnClickListener {
            if (binding.startEnterDateSports.text.isEmpty() || binding.endEnterDateSports.text.isEmpty()) {
                Toast.makeText(
                    requireContext(), "Please Enter The Empty Field And Press Show Button Again",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                binding.ShowNewsBtnSports.visibility = View.INVISIBLE
                binding.backBtnSports.visibility = View.VISIBLE


                //to delete all news that stored in database
                roomViewModel.deleteAllNewsDetails()

                //get the data of (today) from the service and put it into the xml (for Egypt , Saudi Arabia , Emirates , Morocco)
                getDataSports(
                    Constants.API_KEY,
                    Constants.SPORTS,
                  lang,
                    country,
                    Constants.MAX_NUMBER_NEWS,
                    binding.startEnterDateSports.text.toString(),
                    binding.endEnterDateSports.text.toString(),
                    "title,description,content"
                )

                viewModel.mutableLiveData.observe(viewLifecycleOwner) {
                    it.articles
                    roomViewModel.addNewsDataViewModel(it.articles)
                }

                roomViewModel.readAllNewsDataViewModel.observe(viewLifecycleOwner) {
                    binding.apply {
                        if (it.isEmpty()) {
                            binding.noDataTextDetailSports.visibility = View.VISIBLE
                        } else {
                            binding.noDataTextDetailSports.visibility = View.INVISIBLE

                            //PassingArrayListThroughRecyclerAdapterForUrlOfTheImage
                            val arrayList :ArrayList<String> =createArrayToPassingItThroughRecyclerAdapterForUrlOfTheImage(it)

                            recyclerViewDetailSports.setHasFixedSize(true)
                            recyclerViewDetailSports.layoutManager =
                                LinearLayoutManager(requireContext())

                            adapterSports =
                                RecyclerAdapter(
                                    it,
                                    this@DetailSportsFragment,
                                    arrayList
                                )
                            recyclerViewDetailSports.adapter= adapterSports

                            roomViewModel?.readAllNewsDataViewModel?.observe(viewLifecycleOwner) {
                                adapterSports.updateItem(it)
                            }
                        }
                    }

                }
            }
        }

        binding.backBtnSports.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            //to remove toggle from the layout toolbar
            //(activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


        binding.instructionsFloatingSports.setOnClickListener {
            showMeDialog()
        }


    }


    private fun getDataSports(
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

    //for make Dialog
    private fun showMeDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.apply {

            setTitle("Instructions")
            setMessage(
                getString(R.string.instructionsText)
            )
            setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(requireContext(), "Look At The Shortcuts", Toast.LENGTH_LONG)
                    .show()
                findNavController().navigate(DetailSportsFragmentDirections.actionDetailSportsFragmentToShortcutsFragment(sportsVariable = 1))

            })

            setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                val dialog = builder.create()
                dialog.show()
            })
        }

        val dialog = builder.create()
        dialog.show()
    }


    //click on recycler view
    override fun onClicked(position: Int) {


    }

    private fun languagesSpinner(){

        val mySpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.languagesSpinner,android.R.layout.simple_spinner_item)

        binding.spinnerLanguagesSports.apply {
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

    private fun countriesSpinner(){

        val mySpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.countriesSpinner,android.R.layout.simple_spinner_item)

        binding.countriesSpinnerSports.apply {
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


    private fun createArrayToPassingItThroughRecyclerAdapterForUrlOfTheImage(newsList: List<News>) :ArrayList<String> {
        var i = 0
        val mediaTypeArray = ArrayList<String>()
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