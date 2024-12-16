package project.CuraApp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView

class DashboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProblemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        setupRecyclerView()
        return view
    }

    private fun setupRecyclerView() {
        val problems = listOf(
            ProblemItem(
                id = 1,
                imageResourceId = R.drawable.contoh,
                date = "17",
                month = "Dec",
                title = "DIMANAKAH DIA?! Setelah Viral dimanakah sosok gus mifta yang mengolok tukang es teh?",
                maker = "Arafi Laksmana",
                isVerified = true,
                price = 1000,
                targetPrice = 5000,
                currentViews = 27,
                maxViews = 50,
                progress = 20,
                organizerImageResourceId = R.drawable.gibran,
                backers = listOf(R.drawable.gibran, R.drawable.gibran, R.drawable.gibran),
                story = "Gus Mifta, seorang tokoh agama yang viral karena mengolok-olok penjual es teh, kini menghilang dari sorotan publik. Masyarakat bertanya-tanya tentang keberadaannya dan apakah dia telah merefleksikan tindakannya. Kampanye ini bertujuan untuk menemukan Gus Mifta dan mendorongnya untuk meminta maaf kepada penjual es teh serta memberikan kompensasi atas kerugian moral yang dialami."
            ),
            // Add more sample items here if needed
        )

        adapter = ProblemAdapter(problems) { problem ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("PROBLEM_ITEM", problem)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text changes
                return true
            }
        })
    }
}

