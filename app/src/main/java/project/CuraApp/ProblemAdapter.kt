package project.CuraApp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class ProblemAdapter(
    private var problems: List<ProblemItem>,
    private val onItemClick: (ProblemItem) -> Unit
) : RecyclerView.Adapter<ProblemAdapter.ProblemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_problem_card, parent, false)
        return ProblemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProblemViewHolder, position: Int) {
        val problem = problems[position]
        holder.bind(problem)
        holder.itemView.setOnClickListener { onItemClick(problem) }
    }

    override fun getItemCount() = problems.size

    fun updateProblems(newProblems: List<ProblemItem>) {
        problems = newProblems
        notifyDataSetChanged()
    }

    class ProblemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.problemImage)
        private val dateChip: TextView = itemView.findViewById(R.id.dateChip)
        private val makerView: TextView = itemView.findViewById(R.id.problemMaker)
        private val verifiedBadge: TextView = itemView.findViewById(R.id.verifiedBadge)
        private val titleView: TextView = itemView.findViewById(R.id.problemTitle)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val priceText: TextView = itemView.findViewById(R.id.priceText)
        private val viewCount: TextView = itemView.findViewById(R.id.viewCount)

        fun bind(problem: ProblemItem) {
            imageView.setImageResource(problem.imageResourceId)

            dateChip.text = "${problem.date} ${problem.month}"
            makerView.text = problem.maker
            verifiedBadge.visibility = if (problem.isVerified) View.VISIBLE else View.GONE
            titleView.text = problem.title
            progressBar.progress = problem.progress

            // Format price with Indonesian Rupiah
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            priceText.text = numberFormat.format(problem.price)

            // Set view count
            viewCount.text = "${problem.currentViews}/${problem.maxViews}"
        }
    }
}

