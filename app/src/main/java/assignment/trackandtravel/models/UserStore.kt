package assignment.trackandtravel.models


interface UserStore {
    fun createUser(user: UserModel)
    fun findAllUsers(): List<UserModel>
//    fun findByEmail(emailBus:String) : UserModel?
      fun findByEmail(email:String): ArrayList<UserModel>?
}