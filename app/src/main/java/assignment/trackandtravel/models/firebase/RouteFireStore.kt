package assignment.trackandtravel.models.firebase

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import assignment.trackandtravel.helpers.readImageFromPath
import assignment.trackandtravel.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.ByteArrayOutputStream
import java.io.File

class RouteFireStore(val context: Context): RouteStore,UserStore,AdminStore, AnkoLogger {

    val routes = ArrayList<RouteModel>()
    val route = RouteModel()

    val users = ArrayList<UserModel>()
    val admins = ArrayList<AdminModel>()

    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference
    var totalRoutes: Long = 0
    var totalUsers: Long = 0
    var totalAdmins: Long = 0



    override fun findALL(): List<RouteModel>
    {
        return routes
    }

    override fun findAllUsers(): List<UserModel>
    {
        return users
    }

    override fun findAllAdmins(): List<AdminModel>
    {
        return admins
    }

    @SuppressLint("DefaultLocale")
    override fun findAdminByEmail(email: String): ArrayList<AdminModel>? {
        val foundAdmins: ArrayList<AdminModel>? = arrayListOf()
        admins.forEach {
            if (it.emailAdmin.toLowerCase().contains(email.toLowerCase())) {
                foundAdmins?.add(it)
            }
        }
        return foundAdmins
    }


    @SuppressLint("DefaultLocale")
    override fun findByEmail(email: String): ArrayList<UserModel>? {
        val foundHillforts: ArrayList<UserModel>? = arrayListOf()
        users.forEach {
            if (it.emailBus.toLowerCase().contains(email.toLowerCase())) {
                foundHillforts?.add(it)
            }
        }
        return foundHillforts
    }



    override fun findById(id:Long) : RouteModel?{
        val foundRoute: RouteModel? = routes.find { p -> p.id == id }
        return foundRoute
    }

    override fun createAdmin(admin: AdminModel) {
        db = FirebaseDatabase.getInstance().reference
        val key = db.child("admins").push().key
        key?.let {
            admin.fbId = key
            admins.add(admin)
            db.child("admins").child(key).setValue(admin)
        }
    }

    override fun createUser(user: UserModel) {
        db = FirebaseDatabase.getInstance().reference
        val key = db.child("users").push().key
        key?.let {
            user.fbId = key
            users.add(user)
            db.child("users").child(key).setValue(user)
        }
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
            foundRoute.stop1 = route.stop1
            foundRoute.stop2= route.stop2
            foundRoute.stop3= route.stop3
            foundRoute.stop4= route.stop4
            foundRoute.stop5= route.stop5
            foundRoute.stop6= route.stop6
            foundRoute.stop7= route.stop7
            foundRoute.stop8= route.stop8
            foundRoute.stop9= route.stop9
            foundRoute.stop10= route.stop10
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


    fun fetchAdmins(adminsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(admins) { it.getValue<AdminModel>(AdminModel::class.java)
                }
                for(count in dataSnapshot.children) {
                    totalAdmins += count.child("admins").childrenCount
                }
                adminsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        db.child("admins").addListenerForSingleValueEvent(valueEventListener)
    }

    fun fetchUsers(usersReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(users) { it.getValue<UserModel>(UserModel::class.java)
                }
                for(count in dataSnapshot.children) {
                    totalUsers += count.child("users").childrenCount
                }
                usersReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        db.child("users").addListenerForSingleValueEvent(valueEventListener)
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