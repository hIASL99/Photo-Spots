package at.fhjoanneum.photo_spots.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.fhjoanneum.photo_spots.*
import at.fhjoanneum.photo_spots.databinding.FragmentDashboardBinding
import org.w3c.dom.Text
import java.util.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // needed for searchView
    private var postList = listOf<PostModel>()
    private var filteredList = mutableListOf<PostModel>()

    val postAdapter = PostAdapter() {
        val db = LoginModelDatabase.getDatabase(requireContext())
        val username = db.loginModelDao.getUserName().toString()

        if(it.UserName == username) {
            val intent = Intent(context, EditPostActivity::class.java)
            intent.putExtra(TAG_ID, it.Id.toString())

            intent.putExtra(TAG_BOOL, false)
            startActivity(intent)

        }else {
            val intent = Intent(context, ViewLocationActivity::class.java)
            intent.putExtra(TAG_ID, it.Id.toString())

            intent.putExtra(TAG_BOOL, false)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //super.onCreate(savedInstanceState)

        return root
    }

    override fun onResume() {
        super.onResume()
        if (activity?.isDestroyed == false && this.isAdded && view != null) {
            updateList(requireContext())
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity?.isDestroyed == false && this.isAdded && view != null){
            updateList(view.context!!)

            val recyclerView = view.findViewById<RecyclerView>(R.id.dashboard_recyclerview)
            recyclerView?.layoutManager = LinearLayoutManager(view.context)
            recyclerView?.adapter = postAdapter
        }

        // searching for categories or title
        val searchView = view.findViewById<SearchView>(R.id.dashboard_search)
        searchView.setOnClickListener () { searchView.isIconified = false }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query:String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText!!.isNotEmpty()){
                    filteredList.clear()
                    val search = newText.lowercase(Locale.getDefault()) // instead of lowercase() also toLowerCase(Locale) possible

                    for (post in postList) {
                        if (post.Title.lowercase(Locale.getDefault()).contains(search)) {
                            filteredList.add(post)
                        } else {
                            for (category in post.Categories) {
                                if (category.lowercase(Locale.getDefault()).contains(search)) {
                                    filteredList.add(post)
                                }
                            }
                        }
                    }
                    postAdapter.updateList(filteredList)
                } else {
                    postAdapter.updateList(postList)
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateList(context: Context){
        PostRepository.getphotoList(context,
            success = {
                // handle success
                postAdapter.updateList(it.reversed())
                postList = it.reversed()
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