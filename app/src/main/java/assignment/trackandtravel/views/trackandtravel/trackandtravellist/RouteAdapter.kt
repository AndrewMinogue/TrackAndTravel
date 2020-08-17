package assignment.trackandtravel.views.trackandtravel.trackandtravellist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import assignment.trackandtravel.R
import com.bumptech.glide.Glide
import assignment.trackandtravel.models.RouteModel
import kotlinx.android.synthetic.main.card_placemark.view.*

interface RouteListener {
    fun onRouteClick(route: RouteModel)
}

class RouteAdapter constructor(private var routes: List<RouteModel>, private val listener: RouteListener) :
    RecyclerView.Adapter<RouteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_placemark,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val route = routes[holder.adapterPosition]
        holder.bind(route, listener)
    }

    override fun getItemCount(): Int = routes.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(route: RouteModel, listener: RouteListener) {

            if(route.favourite == true){
                itemView.favourited.setChecked(true)
                route.favourite = true
            }else if(route.favourite == false){
                route.favourite = false
                itemView.favourited.setChecked(false)
            }

            itemView.routeTitle.text = route.busnumber
            itemView.routeStart.text = route.busstopstart
            itemView.routeEnd.text = route.busstopend
            Glide.with(itemView.context).load(route.image).into(itemView.imageIcon)
            itemView.setOnClickListener { listener.onRouteClick(route) }
        }
    }


}