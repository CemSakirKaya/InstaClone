package view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.instaclone.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore:FirebaseFirestore
    private lateinit var storage:FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerLauncher()

        auth= FirebaseAuth.getInstance()
        storage= FirebaseStorage.getInstance()
        firestore= FirebaseFirestore.getInstance()
    }


    fun upload(view: View){
        val uuir = UUID.randomUUID()
        val resimadi = "$uuir.jpg"
        val reference= storage.reference
        val imageReference= reference.child("images").child(resimadi)

        if(selectedPicture!=null){
            imageReference.putFile(selectedPicture!!).addOnSuccessListener{
               val uploadpicture = storage.reference.child("images").child(resimadi)
                uploadpicture.downloadUrl.addOnSuccessListener{
                    val downloadurl = it.toString()
                    if(auth.currentUser!=null) {
                        val postMap = hashMapOf<String, Any>()
                        postMap.put("downloadurl",downloadurl)
                        postMap.put("e-mail", auth.currentUser!!.email.toString())
                        postMap.put("comment",binding.editTextText.text.toString())
                        postMap.put("date",Timestamp.now())

                       firestore.collection("Posts").add(postMap).addOnSuccessListener {

                           finish()
                       }.addOnFailureListener {
                           Toast.makeText(this,it.localizedMessage,Toast.LENGTH_SHORT).show()
                       }
                    }
                }

            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_SHORT).show()
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    fun selectimage(view: View){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view,"The permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }.show()
                }
                else{
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }

            }
            else{
           val intenttoGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intenttoGallery)
            }
    }

    fun registerLauncher(){
        activityResultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode== RESULT_OK){
                val intentfromresult = result.data
                if(intentfromresult!=null){
                    selectedPicture = intentfromresult.data
                    selectedPicture?.let {
                        binding.imageView.setImageURI(it)
                    }
                }

            }

        }

        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if(result){
                val intenttoGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intenttoGallery)

            }else{
                Toast.makeText(this,"Permission needed",Toast.LENGTH_LONG).show()
            }
        }

    }



}