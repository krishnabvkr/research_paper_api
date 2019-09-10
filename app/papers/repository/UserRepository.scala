package papers.repository

case class User(userName:String)

object UserRepository {
  val verifiedUsers=List(User("oracle"),User("admin"))

  def isAuthorizedUser(userName:String)=verifiedUsers.contains(User(userName))
}
