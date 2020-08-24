package assignment.trackandtravel.models.firebase

import android.content.Context
import android.graphics.Bitmap
import assignment.trackandtravel.helpers.readImageFromPath
import assignment.trackandtravel.models.RouteModel
import assignment.trackandtravel.models.RouteStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import java.io.ByteArrayOutputStream
import java.io.File

class RouteFireStore(val context: Context): RouteStore, AnkoLogger {

    val routes = ArrayList<RouteModel>()
    val route = RouteModel()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    var totalRoutes: Long = 0


    override fun findALL(): List<RouteModel>
    {
        return routes
    }

    override fun findById(id:Long) : RouteModel?{
        val foundRoute: RouteModel? = routes.find { p -> p.id == id }
        return foundRoute
    }

    override fun create(route: RouteModel) {
        db = FirebaseDatabase.getInstance().reference
        val key = db.child("routes").push().key
        key?.let {
            route.fbId = key
            routes.add(route)
            db.child("routes").child(key).setValue(route)
        }
    }

    override fun update(route: RouteModel) {
        var foundRoute: RouteModel? = routes.find { p -> p.fbId == route.fbId }
        if (foundRoute != null) {
            foundRoute.busnumber = route.busnumber
            foundRoute.busstopstart = route.busstopstart
            foundRoute.busstopend = route.busstopend
            foundRoute.image = route.image
            foundRoute.location = route.location
            foundRoute.favourite = route.favourite
        }

        db.child("routes").child(route.fbId).setValue(route)
        if ((route.image.length) > 0 && (route.image[0] != 'h')) {
            updateImage(route)
        }

    }

    override fun delete(route: RouteModel) {
        db.child("routes").child(route.fbId).removeValue()
        routes.remove(route)
    }

    override fun clear() {
        routes.clear()
    }

    fun fetchRoutes(routesReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(routes) { it.getValue<RouteModel>(RouteModel::class.java)
                }
                for(count in dataSnapshot.children) {
                    totalRoutes += count.child("routes").childrenCount
                }
                routesReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        routes.clear()
        db.child("routes").addListenerForSingleValueEvent(valueEventListener)
    }

    override fun sortedByFavourite(): List<RouteModel>? {
        return routes.filter { it.favourite }
    }

    fun updateImage(route: RouteModel) {
        if (route.image != "") {
            val fileName = File(route.image)
            val imageName = fileName.getName()
            st = FirebaseStorage.getInstance().reference

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, route.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        route.image = it.toString()
                        db.child("routes").child(route.fbId).setValue(route)
                    }
                }
            }
        }
    }







}