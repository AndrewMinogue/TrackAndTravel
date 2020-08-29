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
                      var fbId : String = "",

                      var busnumber : String = "",
                      var busstopstart : String = "",
                      var busstopend : String = "",
                      var image: String = "",
                      var favourite: Boolean = false,
                      @Embedded var location : Location = Location(),
                      @Embedded var stop1 : Location = Location(),
                      @Embedded var stop2 : Location = Location(),
                      @Embedded var stop3 : Location = Location(),
                      @Embedded var stop4 : Location = Location(),
                      @Embedded var stop5 : Location = Location(),
                      @Embedded var stop6 : Location = Location(),
                      @Embedded var stop7 : Location = Location(),
                      @Embedded var stop8 : Location = Location(),
                      @Embedded var stop9 : Location = Location(),
                      @Embedded var stop10 : Location = Location()): Parcelable


@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable