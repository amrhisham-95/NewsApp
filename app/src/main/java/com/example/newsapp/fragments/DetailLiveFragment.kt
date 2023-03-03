package com.example.newsapp.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.OnRecyclerViewClick
import com.example.newsapp.adapters.RecyclerAdapter
import com.example.newsapp.databinding.FragmentDetailLiveBinding
import com.example.newsapp.models.News
import com.example.newsapp.ui.Constants
import com.example.newsapp.ui.DrawerLocker
import com.example.newsapp.ui.MainActivity
import com.example.newsapp.viewModels.RetrofitViewModel
import com.example.newsapp.viewModels.RoomDatabaseViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList


class DetailLiveFragment : Fragment(), OnRecyclerViewClick {

    private lateinit var binding: FragmentDetailLiveBinding
    private val viewModel: RetrofitViewModel by viewModels()
    private val roomViewModel: RoomDatabaseViewModel by viewModels()
    private lateinit var adapterLive: RecyclerAdapter
    var lang = ""
    var country = ""
    var category = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_live, container, false)

        //to hide navigation drawer from fragment you want to hide
        (activity as DrawerLocker?)!!.setDrawerLocked(true)

        //to remove toggle from the layout toolbar
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.roomViewModel = roomViewModel


        //to add languagesSpinner
        languagesSpinner()
        //to add countriesSpinner
        countriesSpinner()


        binding.ShowNewsBtnLive.setOnClickListener {

            if (binding.startEnterDateLive.text.isEmpty() || binding.endEnterDateLive.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please Enter The Empty Field And Press Show Button Again",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                binding.ShowNewsBtnLive.visibility = View.INVISIBLE
                binding.backBtnLive.visibility = View.VISIBLE


                //to delete all news that stored in database
                roomViewModel.deleteAllNewsDetails()

                //get the data of (today) from the service and put it into the xml (for Egypt , Saudi Arabia , Emirates , Morocco)
                getData(
                    Constants.API_KEY,
                    Constants.GENERAL,
                    lang,
                    country,
                    Constants.MAX_NUMBER_NEWS,
                    binding.startEnterDateLive.text.toString(),
                    binding.endEnterDateLive.text.toString(),
                    "title,description,content"
                )

                viewModel.mutableLiveData.observe(viewLifecycleOwner) {
                    it.articles
                    roomViewModel.addNewsDataViewModel(it.articles)
                }

                roomViewModel.readAllNewsDataViewModel.observe(viewLifecycleOwner) {
                    binding.apply {
                        if (it.isEmpty()) {
                            binding.noDataTextDetailLive.visibility = View.VISIBLE
                        } else {
                            binding.noDataTextDetailLive.visibility = View.INVISIBLE

                            //PassingArrayListThroughRecyclerAdapterForUrlOfTheImage
                            val arrayList :ArrayList<String> =createArrayToPassingItThroughRecyclerAdapterForUrlOfTheImage(it)

                            recyclerViewDetailLiveLive.setHasFixedSize(true)
                            recyclerViewDetailLiveLive.layoutManager =
                                LinearLayoutManager(requireContext())

                            adapterLive =
                                RecyclerAdapter(
                                    it,
                                    this@DetailLiveFragment,
                                    arrayList
                                )
                            recyclerViewDetailLiveLive.adapter= adapterLive

                            roomViewModel?.readAllNewsDataViewModel?.observe(viewLifecycleOwner) {
                                adapterLive.updateItem(it)
                            }
                        }
                    }

                }
            }

            }



        binding.backBtnLive.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }



        binding.instructionsFloatingLive.setOnClickListener {
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
                findNavController().navigate(DetailLiveFragmentDirections.actionDetailLiveFragmentToShortcutsFragment(liveVariable = 0))

            })

            setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                val dialog = builder.create()
                dialog.show()
            })
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun createArrayToPassingItThroughRecyclerAdapterForUrlOfTheImage(newsList: List<News>) :ArrayList<String> {
        var i = 0
        var mediaTypeArray = ArrayList<String>()
        for (i in newsList.indices) {
            var mediaType = newsList[i].image
            if (mediaType == null) {
                mediaTypeArray.add("https://kajafax.files.wordpress.com/2012/09/news_image.jpg")
            } else {
                mediaTypeArray.add(mediaType)
            }
        }
        i++
        return mediaTypeArray
    }

    override fun onClicked(position: Int) {

    }


    private fun languagesSpinner(){

        val mySpinnerAdapter = ArrayAdapter.createFromResource(requireContext(),R.array.languagesSpinner,android.R.layout.simple_spinner_item)

        binding.spinnerLanguagesLive.apply {
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

        binding.countriesSpinnerLive.apply {
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



    private fun getData(
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

    //to hide navigation drawer from fragment you want to hide
    override fun onDestroy() {
        super.onDestroy()
        (activity as DrawerLocker?)!!.setDrawerLocked(false)
    }
}