package at.fhjoanneum.photo_spots.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhjoanneum.photo_spots.PostAdapter
import at.fhjoanneum.photo_spots.PostRepository
import at.fhjoanneum.photo_spots.CameraActivity
import at.fhjoanneum.photo_spots.MapsActivity
import at.fhjoanneum.photo_spots.R
import at.fhjoanneum.photo_spots.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val postAdapter = PostAdapter() {

        //val intent = Intent(context, LessonRatingActivity::class.java)
        //intent.putExtra(EXTRA_LESSON_ID, it.id)
        //startActivityForResult(intent, ADD_OR_EDIT_RATING_REQUEST)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.dashboardButtonViewmap.setOnClickListener() {
            val intent = Intent(getActivity(), MapsActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        //super.onCreate(savedInstanceState)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity?.isDestroyed == false && this.isAdded && view != null){
            updateList(view?.context!!)


            val recyclerView = view?.findViewById<RecyclerView>(R.id.dashboard_recyclerview)
            recyclerView?.layoutManager = LinearLayoutManager(view?.context)
            recyclerView?.adapter = postAdapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun updateList(context: Context){
        PostRepository.getphotoList(context,
            success = {
                // handle success
                postAdapter.updateList(it)
            },
            error = {
                // handle error
                Log.e("API ERROR",it)
            }
        )
    }
}