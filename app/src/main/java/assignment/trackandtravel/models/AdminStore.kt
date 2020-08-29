package assignment.trackandtravel.models


interface AdminStore {
    fun createAdmin(user: AdminModel)
    fun findAllAdmins(): List<AdminModel>
    fun findAdminByEmail(email:String): ArrayList<AdminModel>?
}