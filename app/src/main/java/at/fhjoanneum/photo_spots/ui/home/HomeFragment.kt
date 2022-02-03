package at.fhjoanneum.photo_spots.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhjoanneum.photo_spots.*
import at.fhjoanneum.photo_spots.databinding.FragmentHomeBinding
import at.fhjoanneum.photo_spots.ui.dashboard.DashboardFragment
import at.fhjoanneum.photo_spots.ui.dashboard.DashboardFragment.Companion.TAG_ID

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val postAdapter = PostAdapter() {
        val intent = Intent(context, EditPostActivity::class.java)
        intent.putExtra(HomeFragment.TAG_ID, it.Id.toString())

        intent.putExtra(HomeFragment.TAG_BOOL, false)
        startActivity(intent)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.homeButtonTakepic.setOnClickListener() {
            val intent = Intent(activity, CameraActivity::class.java)
            activity?.startActivity(intent)

        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity?.isDestroyed == false && this.isAdded && view != null){
            updateList(view?.context!!)


            val recyclerView = view?.findViewById<RecyclerView>(R.id.home_recyclerview)
            recyclerView?.layoutManager = LinearLayoutManager(view?.context)
            recyclerView?.adapter = postAdapter
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateList(context: Context){
        PostRepository.getMyPhotoList(context,
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
    companion object {
        const val TAG_ID = "TAG_ID"
        const val TAG_BOOL = "TAG_BOOL"
    }
}
