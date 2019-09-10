package papers


import javax.inject.Inject
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
  * Routes and URLs to the PostResource controller.
  */
class ResearchPaperRouter @Inject()(controller: ResearchPaperController) extends SimpleRouter {

  override def routes: Routes = {
    case GET(p"/") =>
      controller.papersList
    case POST(p"/") =>
      controller.addPaper()
    case GET(p"/${int(id)}") =>
      controller.paper(id)
    case GET(p"/search" ? q"title=$title") =>
    controller.searchByTitle(title)
    case GET(p"/search" ? q"author=$author") =>
      controller.searchByAuthor(author)
    case GET(p"/search" ? q"text=$text") =>
      controller.searchByContent(text)
    case GET(p"/search" ? q_o"from=$fromDate" & q_o"to=$toDate") =>
      controller.searchByPublishedDate(fromDate,toDate)
      }



}
