package adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.R
import com.example.instaclone.databinding.PostcardtasarimBinding
import com.squareup.picasso.Picasso
import model.Post

class PostAdapter(private val context:Context,private val postlist:ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.Tutucu>() {


     inner class Tutucu(view: View): RecyclerView.ViewHolder(view){
         var emailtv : TextView
         var commenttv: TextView
         var postresmi :ImageView
         init {
             emailtv= view.findViewById(R.id.useremail)
             commenttv=view.findViewById(R.id.comment)
             postresmi=view.findViewById(R.id.postresmi)
         }
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Tutucu {
      val tasarim = LayoutInflater.from(parent.context).inflate(R.layout.postcardtasarim,parent,false)
        return Tutucu(tasarim)
    }

    override fun getItemCount(): Int {
      return  postlist.size
    }

    override fun onBindViewHolder(holder: Tutucu, position: Int) {
            val post = postlist.get(position)
       holder.commenttv.text = post.comment
        holder.emailtv.text= post.email
        Picasso.get().load(post.downloadurl).into(holder.postresmi)
    }

}