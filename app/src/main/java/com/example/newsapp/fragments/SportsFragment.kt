package com.example.newsapp.fragments

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.adapters.OnRecyclerViewClick
import com.example.newsapp.adapters.RecyclerAdapter
import com.example.newsapp.databinding.FragmentSportsBinding
import com.example.newsapp.models.News
import com.example.newsapp.ui.Constants
import com.example.newsapp.viewModels.RetrofitViewModel
import com.example.newsapp.viewModels.RoomDatabaseViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SportsFragment : Fragment(), OnRecyclerViewClick {

    private lateinit var binding: FragmentSportsBinding
    private val viewModel: RetrofitViewModel by viewModels()
    private val roomViewModel: RoomDatabaseViewModel by viewModels()
    private lateinit var adapterSports: RecyclerAdapter

    private val startDateWeek = getDaysFormattedDates()[6]
    private val todayDate = getDaysFormattedDates()[0]
    //val previousDay = getPreviousDayDate()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sports, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.roomViewModel = roomViewModel


        //Put the date of first day and end day of the week in the correct formula "YYYY-MM-DDThh:mm:ssTZD" Z(universal time)
        val formulaStartDayOfWeekDate = "{$startDateWeek}T13:00:00Z"
        val formulaEndDayOfWeekDate = "{$todayDate}T13:00:00Z"


        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menusports, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    //English Sports News From The Service (English Languages) - (USA & UNITED_KINGDOM Countries) - (One Week) - (sports Category) - (No Sources)
                    R.id.englishNewsSports -> {
                        binding.mainTitleCardViewSports.visibility = View.INVISIBLE
                        binding.mainContentCardViewSports.visibility = View.INVISIBLE

                        //to delete data in database
                        roomViewModel.deleteAllNewsDetails()

                        getDataSports( Constants.API_KEY,
                            Constants.SPORTS,
                            Constants.ENGLISH,
                            Constants.USA+","+Constants.UNITED_KINGDOM,
                            Constants.MAX_NUMBER_NEWS,
                            formulaStartDayOfWeekDate,
                            formulaEndDayOfWeekDate,
                            "title,description,content")

                        viewModel.mutableLiveData.observe(viewLifecycleOwner) {
                            it.articles
                            roomViewModel.addNewsDataViewModel(it.articles)
                        }

                        roomViewModel.readAllNewsDataViewModel.observe(viewLifecycleOwner) {
                            binding.apply {
                                if (it.isEmpty()) {
                                    binding.noDataTextSports.visibility = View.VISIBLE
                                } else {
                                    binding.noDataTextSports.visibility = View.INVISIBLE

                                    //PassingArrayListThroughRecyclerAdapterForUrlOfTheImage
                                    val arrayList :ArrayList<String> =createArrayToPassingItThroughRecyclerAdapterForUrlOfTheImage(it)

                                    recyclerViewSports.setHasFixedSize(true)
                                    recyclerViewSports.layoutManager =
                                        LinearLayoutManager(requireContext())

                                    adapterSports =
                                        RecyclerAdapter(
                                            it,
                                            this@SportsFragment,
                                            arrayList
                                        )

                                    recyclerViewSports.adapter = adapterSports

                                    refreshBtnSports.setOnClickListener {
                                        roomViewModel?.readAllNewsDataViewModel?.observe(viewLifecycleOwner) {
                                            adapterSports.updateItem(it)
                                            recyclerViewSports.smoothScrollToPosition(adapterSports.itemCount - 1)
                                        }
                                    }
                                }
                            }

                        }


                        return true
                    }


                    //Arabic News From The Service (Arabic Languages) - (EGYPT & SAUDI & EMIRATES & MOROCCO COUNTRIES) - (One Week) - (general Category) - (No Sources)
                    R.id.arabicNewsSports -> {
                        binding.mainTitleCardViewSports.visibility = View.INVISIBLE
                        binding.mainContentCardViewSports.visibility = View.INVISIBLE


                        //to delete data in database
                        roomViewModel.deleteAllNewsDetails()

                        getDataSports(
                            Constants.API_KEY,
                            Constants.SPORTS,
                            Constants.ARABIC,
                            Constants.EGYPT,
                            Constants.MAX_NUMBER_NEWS,
                            formulaStartDayOfWeekDate,
                            formulaEndDayOfWeekDate,
                            "title,description,content"
                        )
                        viewModel.mutableLiveData.observe(viewLifecycleOwner) {
                            it.articles
                            roomViewModel.addNewsDataViewModel(it.articles)
                        }

                        roomViewModel.readAllNewsDataViewModel.observe(viewLifecycleOwner) {
                            binding.apply {
                                if (it.isEmpty()) {
                                    binding.noDataTextSports.visibility = View.VISIBLE
                                } else {
                                    binding.noDataTextSports.visibility = View.INVISIBLE

                                    //PassingArrayListThroughRecyclerAdapterForUrlOfTheImage
                                    val arrayList :ArrayList<String> =createArrayToPassingItThroughRecyclerAdapterForUrlOfTheImage(it)

                                    recyclerViewSports.setHasFixedSize(true)
                                    recyclerViewSports.layoutManager =
                                        LinearLayoutManager(requireContext())

                                    adapterSports =
                                        RecyclerAdapter(
                                            it,
                                            this@SportsFragment,
                                            arrayList
                                        )

                                    recyclerViewSports.adapter = adapterSports

                                    refreshBtnSports.setOnClickListener {
                                        roomViewModel?.readAllNewsDataViewModel?.observe(viewLifecycleOwner) {
                                            adapterSports.updateItem(it)
                                            recyclerViewSports.smoothScrollToPosition(adapterSports.itemCount - 1)
                                        }
                                    }
                                }
                            }

                        }
                        return true
                    }

                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

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

    override fun onClicked(position: Int) {

        binding.mainContentCardViewSports.visibility = View.VISIBLE
        binding.mainTitleCardViewSports.visibility = View.VISIBLE


        roomViewModel.readAllNewsDataViewModel.observe(viewLifecycleOwner) {
            binding.mainContentSports.text = it[position].content
            binding.mainTitleSports.text = it[position].title
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


    private fun getDaysFormattedDates(): ArrayList<String> {
        val formattedDateList = ArrayList<String>()

        val calendar = Calendar.getInstance()
        for (i in 0..Constants.DEFAULT_END_DATE_DAYS_WEEK) {
            val currentTime = calendar.time
            val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            } else {
                TODO("VERSION.SDK_INT < N")
            }
            formattedDateList.add(dateFormat.format(currentTime))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return formattedDateList
    }

   /* fun getPreviousDayDate(): String {
        //previous day :
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val previousDayTime = calendar.time
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val previousDayDate = dateFormat.format(previousDayTime)

        return previousDayDate
    }*/

}
