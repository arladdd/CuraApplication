package project.CuraApp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ReportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Start ReportActivity immediately
        startActivity(Intent(activity, ReportActivity::class.java))
        return inflater.inflate(R.layout.fragment_report, container, false)
    }
}

