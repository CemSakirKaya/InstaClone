package view

import adapter.PostAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instaclone.R
import com.example.instaclone.databinding.ActivityFeedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import model.Post

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private lateinit var adapter: PostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        postArrayList= ArrayList<Post>()
        binding.toolbar.title = "İnstagram"
        setSupportActionBar(binding.toolbar)
        db= FirebaseFirestore.getInstance()

        getData()
        val adapter =PostAdapter(this,postArrayList)

        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)

        binding.rv.adapter = adapter

    }

    fun  getData(){
        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_SHORT).show()
            }
            else{
                if(value!=null){
                    postArrayList.clear()
                    if (!value.isEmpty){
                        val documents = value.documents

                        for(document in documents){
                            val comment = document.get("comment") as String
                            val email = document.get("e-mail") as String
                            val downloadurl = document.get("downloadurl") as String
                            val post = Post(email,downloadurl,comment)
                            postArrayList.add(post)
                        }


                    }
                }
            }

        }


    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.insta_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.Post ->{
                val intent = Intent(this, UploadActivity::class.java)
                startActivity(intent)

                return true
            }
            R.id.Signout ->{
                auth.signOut()
                val intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)

                return true
            }
            else->{
                return false
            }

        }


    }

}