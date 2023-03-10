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
import com.example.newsapp.databinding.FragmentGeneralNewsBinding
import com.example.newsapp.models.News
import com.example.newsapp.ui.Constants
import com.example.newsapp.viewModels.RetrofitViewModel
import com.example.newsapp.viewModels.RoomDatabaseViewModel

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GeneralNewsFragment : Fragment(), OnRecyclerViewClick {

    private lateinit var binding: FragmentGeneralNewsBinding
    private val viewModel: RetrofitViewModel by viewModels()
    private val roomViewModel: RoomDatabaseViewModel by viewModels()
    private lateinit var adapter: RecyclerAdapter
    private val startDateWeek = getDaysFormattedDates()[6]
    private val todayDate = getDaysFormattedDates()[0]
    //val previousDay = getPreviousDayDate()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_general_news, container, false)
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
                menuInflater.inflate(R.menu.menugeneral, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    //Default English News From The Service (English Languages) - (USA & UNITED_KINGDOM Countries) - (One Week) - (general Category) - (No Sources)
                    R.id.englishNews -> {
                        binding.mainTitleCardView.visibility = View.INVISIBLE
                        binding.mainContentCardView.visibility = View.INVISIBLE

                        //to delete data in database
                        roomViewModel.deleteAllNewsDetails()

                        //for getting the default USA and UNITED_KINGDOM Countries and ENGLISH Languages
                        getData(
                            Constants.API_KEY,
                            Constants.GENERAL,
                            Constants.ENGLISH,
                            Constants.USA + "," + Constants.UNITED_KINGDOM,
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
                                    binding.noDataTextLive.visibility = View.VISIBLE
                                } else {
                                    binding.noDataTextLive.visibility = View.INVISIBLE

                                    //PassingArrayListThroughRecyclerAdapterForUrlOfTheImage
                                    val arrayList: ArrayList<String> =
                                        createArrayToPassingItThroughRecyclerAdapterForUrlOfTheImage(
                                            it
                                        )
                                    recyclerViewGeneral.setHasFixedSize(true)
                                    //vertical recycler view
                                 recyclerViewGeneral.layoutManager =
                                        LinearLayoutManager(requireContext())

                                    //HORIZONTAL recycler view
                                 /* val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,true)
                                    recyclerViewGeneral.layoutManager = layoutManager */

                                    adapter =
                                        RecyclerAdapter(
                                            it,
                                            this@GeneralNewsFragment,
                                            arrayList
                                        )

                                    recyclerViewGeneral.adapter = adapter

                                  refreshBtnLive.setOnClickListener {
                                      roomViewModel?.readAllNewsDataViewModel?.observe(
                                          viewLifecycleOwner
                                      ) {
                                          adapter.updateItem(it)
                                          recyclerViewGeneral.smoothScrollToPosition(adapter.itemCount - 1)
                                      }
                                  }
                                }
                            }

                        }

                        return true
                    }


                    //Arabic News From The Service (Arabic Languages) - (EGYPT & SAUDI & EMIRATES & MOROCCO COUNTRIES) - (One Week) - (general Category) - (No Sources)
                    R.id.arabicNews -> {
                        binding.mainTitleCardView.visibility = View.INVISIBLE
                        binding.mainContentCardView.visibility = View.INVISIBLE

                        //to delete data in database
                        roomViewModel.deleteAllNewsDetails()

                        getData(
                            Constants.API_KEY,
                            Constants.GENERAL,
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
                                    binding.noDataTextLive.visibility = View.VISIBLE
                                } else {
                                    binding.noDataTextLive.visibility = View.INVISIBLE

                                    //PassingArrayListThroughRecyclerAdapterForUrlOfTheImage
                                    val arrayList: ArrayList<String> =
                                        createArrayToPassingItThroughRecyclerAdapterForUrlOfTheImage(
                                            it
                                        )

                                    recyclerViewGeneral.setHasFixedSize(true)
                                    recyclerViewGeneral.layoutManager =
                                        LinearLayoutManager(requireContext())


                                    adapter =
                                        RecyclerAdapter(
                                            it,
                                            this@GeneralNewsFragment,
                                            arrayList
                                        )

                                    recyclerViewGeneral.adapter = adapter

                                    refreshBtnLive.setOnClickListener {
                                        roomViewModel?.readAllNewsDataViewModel?.observe(
                                            viewLifecycleOwner
                                        ) {
                                            adapter.updateItem(it)
                                            recyclerViewGeneral.smoothScrollToPosition(adapter.itemCount - 1)
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


    private fun createArrayToPassingItThroughRecyclerAdapterForUrlOfTheImage(newsList: List<News>): ArrayList<String> {
        var i = 0
        val mediaTypeArray = ArrayList<String>()
        for (i in newsList.indices) {
            val mediaType = newsList[i].image
            mediaTypeArray.add(mediaType)
        }
        i++
        return mediaTypeArray
    }

    private fun getData(
        apikey: String,
        category: String,
        lang: String,
        country: String,
        max: Int,
        from: String,
        to: String,
        nullable: String
    ) {
        viewModel.getDataRetrofitViewModel(apikey, category, lang, country, max, from, to, nullable)
    }

    override fun onClicked(position: Int) {

        binding.mainContentCardView.visibility = View.VISIBLE
        binding.mainTitleCardView.visibility = View.VISIBLE


        roomViewModel.readAllNewsDataViewModel.observe(viewLifecycleOwner) {
            binding.mainContent.text = it[position].content
            binding.mainTitle.text = it[position].title
        }

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


