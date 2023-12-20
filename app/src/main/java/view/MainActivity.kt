package view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.instaclone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        if (currentUser!=null){
            val intent = Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

     fun signInOnClicked(view: View){
         val email = binding.editTextTextEmailAddress.text.toString()
         val password = binding.editTextTextPassword.text.toString()

         if(email.isNotEmpty() && password.isNotEmpty() ){
             auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                 val intent = Intent(this@MainActivity, FeedActivity::class.java)
                 startActivity(intent)
                 finish()
             }.addOnFailureListener {
                 Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
             }

         }
         else{
             Toast.makeText(this,"Boş bırakmayınız lütfen ! ",Toast.LENGTH_SHORT).show()
         }


     }

    fun signUpOnClicked(view: View){

        val email = binding.editTextTextEmailAddress.text.toString()
        val password = binding.editTextTextPassword.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty() ){
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
            val intent = Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener {
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
        }

        }
        else{
            Toast.makeText(this,"Boş bırakmayınız lütfen ! ",Toast.LENGTH_SHORT).show()
        }

    }


}