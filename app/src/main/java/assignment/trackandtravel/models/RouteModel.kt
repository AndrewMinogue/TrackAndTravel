package assignment.trackandtravel.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class RouteModel(@PrimaryKey(autoGenerate = true)
                      var id: Long= 0,

                      var busnumber : String = "",
                      var busstopstart : String = "",
                      var image: String = "",
                      var favourite: Boolean = false,
                      @Embedded var locationm : Location = Location()): Parcelable


@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable