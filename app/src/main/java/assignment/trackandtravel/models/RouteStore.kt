package assignment.trackandtravel.models

interface RouteStore {
    fun findALL(): List<RouteModel>
    fun create(route: RouteModel)
    fun update(route: RouteModel)
    fun delete(route: RouteModel)
    fun findById(id:Long) : RouteModel?
    fun clear()
    fun sortedByFavourite(): List<RouteModel>?
}