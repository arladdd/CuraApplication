package project.CuraApp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup

class HistoryAdapter(
    private val problems: List<ProblemItem>,
    private val onUploadClick: (Int) -> Unit,
    private val onMarkCompleteClick: (Int) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val problemImage: ImageView = view.findViewById(R.id.problemImage)
        val title: TextView = view.findViewById(R.id.problemTitle)
        val uploadButton: Button = view.findViewById(R.id.uploadButton)
        val markCompleteButton: Button = view.findViewById(R.id.markCompleteButton)
        val activityPhotosGroup: ChipGroup = view.findViewById(R.id.activityPhotosGroup)
        val completedBadge: TextView = view.findViewById(R.id.completedBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val problem = problems[position]

        holder.problemImage.setImageResource(problem.imageResourceId)
        holder.title.text = problem.title

        // Show/hide completed badge
        holder.completedBadge.visibility = if (problem.isCompleted) View.VISIBLE else View.GONE

        // Show/hide buttons based on completion status
        if (problem.isCompleted) {
            holder.uploadButton.visibility = View.GONE
            holder.markCompleteButton.visibility = View.GONE
        } else {
            holder.uploadButton.visibility = View.VISIBLE
            holder.markCompleteButton.visibility = View.VISIBLE

            holder.uploadButton.setOnClickListener {
                onUploadClick(position)
            }
            holder.markCompleteButton.setOnClickListener {
                onMarkCompleteClick(position)
            }
        }

        // Display activity photos
        holder.activityPhotosGroup.removeAllViews()
        problem.activityPhotos.forEach { photoUri ->
            val imageView = ImageView(holder.itemView.context).apply {
                setImageURI(Uri.parse(photoUri))
                layoutParams = ViewGroup.LayoutParams(100, 100)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
            holder.activityPhotosGroup.addView(imageView)
        }
    }

    override fun getItemCount() = problems.size
}

