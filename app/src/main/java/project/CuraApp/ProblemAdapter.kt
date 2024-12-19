package project.CuraApp


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale
import project.CuraApp.ProblemItem
import project.CuraApp.User


class ProblemAdapter(
    private val problems: MutableList<ProblemItem>,
    private val onItemClick: (ProblemItem) -> Unit
) : RecyclerView.Adapter<ProblemAdapter.ProblemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_problem_card, parent, false)
        return ProblemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProblemViewHolder, position: Int) {
        val problem = problems[position]
        Log.d("ProblemAdapter", "Binding problem ID: ${problem.id}")
        holder.bind(problem)
        holder.itemView.setOnClickListener {
            Log.d("ProblemAdapter", "Clicked problem ID: ${problem.id}")
            onItemClick(problem)
        }
    }

    override fun getItemCount() = problems.size

    fun updateProblems(newProblems: List<ProblemItem>) {
        problems.clear()
        problems.addAll(newProblems)
        Log.d("ProblemAdapter", "Updated problems list size: ${problems.size}")
        notifyDataSetChanged()
    }

    class ProblemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.problemImage)
        private val titleView: TextView = itemView.findViewById(R.id.problemTitle)
        private val makerView: TextView = itemView.findViewById(R.id.problemMaker)
        private val donationText: TextView = itemView.findViewById(R.id.donationText)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val volunteerText: TextView = itemView.findViewById(R.id.viewCount)

        fun bind(problem: ProblemItem) {
            // Load image using Glide
            Glide.with(itemView.context)
                .load(problem.imageUrl)
                .into(imageView)

            // Set the title
            titleView.text = problem.title

            // Safely display user name
            makerView.text = problem.user?.name ?: "Unknown"

            // Format and display donation progress
            val donationFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            donationText.text = "${donationFormat.format(problem.donationReceived)} / ${donationFormat.format(problem.donationNeeded)}"

            progressBar.progress = problem.donationPercentage()

            // Display volunteer statistics
            volunteerText.text = "Volunteers: ${problem.volunteerReceived} / ${problem.volunteerNeeded}"
            val volunteerPercentage = if (problem.volunteerNeeded > 0) {
                (problem.volunteerReceived.toFloat() / problem.volunteerNeeded * 100).toInt()
            } else 0
        }
    }
}

