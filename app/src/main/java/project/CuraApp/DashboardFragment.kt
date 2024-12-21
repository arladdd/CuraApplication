package project.CuraApp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DashboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProblemAdapter
    private lateinit var searchBar: EditText
    private lateinit var historyIcon: ImageView
    private lateinit var profilePhoto: ImageView
    private var problems = listOf<ProblemItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        searchBar = view.findViewById(R.id.search_bar)
        historyIcon = view.findViewById(R.id.history_icon)
        profilePhoto = view.findViewById(R.id.profile_photo)
        setupRecyclerView()
        setupHistoryIcon()
        setupProfilePhoto()
        return view
    }

    private fun setupRecyclerView() {
        problems = listOf(
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

    private fun setupHistoryIcon() {
        historyIcon.setOnClickListener {
            val intent = Intent(activity, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupProfilePhoto() {
        profilePhoto.setOnClickListener {
            val intent = Intent(activity, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
