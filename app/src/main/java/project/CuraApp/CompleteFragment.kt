package project.CuraApp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CompleteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CompleteAdapter
    private var completedProblems = mutableListOf<ProblemItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_complete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        loadCompletedProblems()
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = CompleteAdapter(completedProblems)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun loadCompletedProblems() {
        // In a real app, this would load from a database or API
        // For now, we'll use sample data
        completedProblems.clear()
        completedProblems.addAll(
            listOf(
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
                    progress = 100,
                    organizerImageResourceId = R.drawable.gibran,
                    backers = listOf(R.drawable.gibran),
                    story = "Sample story text",
                    isCompleted = true,
                    activityPhotos = mutableListOf("sample_activity_photo_uri")
                )
            )
        )
        adapter.notifyDataSetChanged()
    }
}

