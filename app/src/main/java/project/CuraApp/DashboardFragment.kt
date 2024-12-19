package project.CuraApp


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import project.CuraApp.RetrofitInstance
import java.io.IOException
import project.CuraApp.ProblemItem
import project.CuraApp.User
import kotlin.math.log


class DashboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProblemAdapter
    private lateinit var searchBar: EditText
    private val problems = mutableListOf<ProblemItem>()
    private val loadedPages = mutableMapOf<Int, List<ProblemItem>>()
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private val pageSize = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        searchBar = view.findViewById(R.id.search_bar)

        setupRecyclerView()
        setupSearchBar()

        fetchProblems(currentPage)

        return view
    }

    private fun setupRecyclerView() {
        adapter = ProblemAdapter(problems) { problem ->
            Log.d("DashboardFragment", "Sending Problem ID: ${problem.id}")
            val intent = Intent(activity, DetailActivity::class.java).apply {
                putExtra("PROBLEM_ID", problem.id)
            }
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (dy > 0 && !isLoading && !isLastPage && lastVisibleItemPosition >= totalItemCount - 1) {
                    currentPage++
                    fetchProblems(currentPage, isUpward = false)
                } else if (dy < 0 && !isLoading && currentPage > 1 && firstVisibleItemPosition <= 1) {
                    currentPage--
                    fetchProblems(currentPage, isUpward = true)
                }
            }
        })
    }

    private fun setupSearchBar() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterProblems(s?.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchProblems(page: Int, isUpward: Boolean = false) {
        if (isLoading || loadedPages.containsKey(page)) return

        isLoading = true
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getProblems(page, pageSize)

                if (response.isSuccessful) {
                    val problemsList = response.body()?.problems ?: emptyList()

                    if (problemsList.isNotEmpty()) {
                        loadedPages[page] = problemsList

                        if (isUpward) {
                            problems.addAll(0, problemsList) // Prepend new data
                        } else {
                            problems.addAll(problemsList) // Append new data
                        }
                        adapter.notifyDataSetChanged()
                    } else {
                        isLastPage = !isUpward // Mark end of pagination only for downward scrolling
                    }
                } else {
                    Log.e("DashboardFragment", "Failed to fetch page: $page")
                }
            } catch (e: IOException) {
                Log.e("DashboardFragment", "Network Error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    private fun filterProblems(query: String?) {
        if (query.isNullOrBlank()) {
            adapter.updateProblems(problems)
        } else {
            val filteredList = problems.filter { problem ->
                problem.title.contains(query, ignoreCase = true) ||
                        problem.user?.name?.contains(query, ignoreCase = true) == true
            }
            adapter.updateProblems(filteredList)
        }
    }
}

