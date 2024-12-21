package project.CuraApp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup

class CompleteAdapter(
    private val problems: List<ProblemItem>
) : RecyclerView.Adapter<CompleteAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val problemImage: ImageView = view.findViewById(R.id.problemImage)
        val title: TextView = view.findViewById(R.id.problemTitle)
        val activityPhotosGroup: ChipGroup = view.findViewById(R.id.activityPhotosGroup)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_complete, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val problem = problems[position]

        holder.problemImage.setImageResource(problem.imageResourceId)
        holder.title.text = problem.title

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

