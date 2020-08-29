package assignment.trackandtravel.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

    @Parcelize
    @Entity
    data class AdminModel(@PrimaryKey(autoGenerate = true)
                         var id: Long= 0,
                         var fbId : String = "",

                         var emailAdmin : String = "",
                         var admin : Boolean = false): Parcelable
