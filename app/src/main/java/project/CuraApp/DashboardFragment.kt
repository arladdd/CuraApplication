package project.CuraApp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText

class DashboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProblemAdapter
    private lateinit var searchBar: EditText
    private var problems = listOf<ProblemItem>()

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
        return view
    }

    private fun setupRecyclerView() {
        problems = listOf(
            ProblemItem(
                id = 1,
                imageResourceId = R.drawable.contoh,
                date = "17",
                month = "Dec",
                title = "Program menanam 1000 pohon untuk daerah rawan banjir",
                maker = "A'rafi Laksmana",
                isVerified = true,
                price = 20000000,
                targetPrice = 70500000,
                currentViews = 27,
                maxViews = 50,
                progress = 20,
                organizerImageResourceId = R.drawable.gibran,
                backers = listOf(R.drawable.gibran, R.drawable.gibran, R.drawable.gibran),
                story = "Gus Mifta, seorang tokoh agama yang viral karena mengolok-olok penjual es teh, kini menghilang dari sorotan publik. Masyarakat bertanya-tanya tentang keberadaannya dan apakah dia telah merefleksikan tindakannya. Kampanye ini bertujuan untuk menemukan Gus Mifta dan mendorongnya untuk meminta maaf kepada penjual es teh serta memberikan kompensasi atas kerugian moral yang dialami."
            ),
            ProblemItem(
                id = 1,
                imageResourceId = R.drawable.contoh,
                date = "17",
                month = "Dec",
                title = "Program menanam 1000 pohon untuk daerah rawan banjir",
                maker = "A'rafi Laksmana",
                isVerified = true,
                price = 20000000,
                targetPrice = 70500000,
                currentViews = 27,
                maxViews = 50,
                progress = 20,
                organizerImageResourceId = R.drawable.gibran,
                backers = listOf(R.drawable.gibran, R.drawable.gibran, R.drawable.gibran),
                story = "Gus Mifta, seorang tokoh agama yang viral karena mengolok-olok penjual es teh, kini menghilang dari sorotan publik. Masyarakat bertanya-tanya tentang keberadaannya dan apakah dia telah merefleksikan tindakannya. Kampanye ini bertujuan untuk menemukan Gus Mifta dan mendorongnya untuk meminta maaf kepada penjual es teh serta memberikan kompensasi atas kerugian moral yang dialami."
            ),
            ProblemItem(
                id = 1,
                imageResourceId = R.drawable.contoh,
                date = "17",
                month = "Dec",
                title = "Program menanam 1000 pohon untuk daerah rawan banjir",
                maker = "A'rafi Laksmana",
                isVerified = true,
                price = 20000000,
                targetPrice = 70500000,
                currentViews = 27,
                maxViews = 50,
                progress = 20,
                organizerImageResourceId = R.drawable.gibran,
                backers = listOf(R.drawable.gibran, R.drawable.gibran, R.drawable.gibran),
                story = "Gus Mifta, seorang tokoh agama yang viral karena mengolok-olok penjual es teh, kini menghilang dari sorotan publik. Masyarakat bertanya-tanya tentang keberadaannya dan apakah dia telah merefleksikan tindakannya. Kampanye ini bertujuan untuk menemukan Gus Mifta dan mendorongnya untuk meminta maaf kepada penjual es teh serta memberikan kompensasi atas kerugian moral yang dialami."
            )
            // Tambahkan item lainnya jika perlu
        )

        adapter = ProblemAdapter(problems) { problem ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra("PROBLEM_ITEM", problem)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
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

    private fun filterProblems(query: String?) {
        if (query.isNullOrBlank()) {
            adapter.updateProblems(problems)
        } else {
            val filteredList = problems.filter {
                it.title.contains(query, ignoreCase = true) || it.maker.contains(query, ignoreCase = true)
            }
            adapter.updateProblems(filteredList)
        }
    }
}
