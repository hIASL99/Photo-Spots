package at.fhjoanneum.photo_spots

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostAdapter (val clickListener: (post: PostModel) -> Unit): RecyclerView.Adapter<PostViewHolder>(){
    private var postList = listOf<PostModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val photoItemView = inflater.inflate(R.layout.item_photo, parent, false)
        return PostViewHolder(photoItemView, clickListener)
    }

    override fun getItemCount(): Int {
        return postList.size;
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindItem(postList[position])

    }

    fun updateList(newList: List<PostModel>) {
        postList = newList
        notifyDataSetChanged()
    }

}

class PostViewHolder(itemView: View, val clickListener: (post: PostModel) -> Unit): RecyclerView.ViewHolder(itemView){
    fun bindItem(post: PostModel) {
        itemView.findViewById<TextView>(R.id.item_photo_txt_title).text = post.Title
        itemView.findViewById<TextView>(R.id.item_photo_txt_username).text = post.UserName
        itemView.findViewById<TextView>(R.id.item_photo_txt_address).text = post.Location
        //itemView.findViewById<TextView>(R.id.item_photo_rating_count).text = post.Rating.toString()
        //itemView.findViewById<RatingBar>(R.id.item_photo_avg_rating_bar).rating = post.Rating
        val imageView = itemView.findViewById<ImageView>(R.id.item_photo_imageView)
        Glide.with(itemView)
            .load(post.Photo)
            .into(imageView)
        itemView.setOnClickListener {
            clickListener(post)
        }
    }
}