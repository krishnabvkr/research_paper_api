package papers

import javax.inject.Inject
import papers.repository.ResearchPaperResource
import papers.utils.DateUtil
import play.api.libs.json.{Format, Json}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}


/**
  * Takes HTTP requests and produces JSON.
  */
class ResearchPaperController @Inject()(cc: PostControllerComponents)(
    implicit ec: ExecutionContext)
    extends PostBaseController(cc) {

  implicit val format: Format[ResearchPaperResource] = Json.format

  def papersList:Action[AnyContent] = AuthorizedAction.async{
    paperResourceRepo.list().map(paperList=>{
      Ok(Json.toJson(paperList.map(_.toResource)))

    })
  }

  def paper(id:Int):Action[AnyContent] = AuthorizedAction.async{
    paperResourceRepo.get(id).map(paper=>{
      Ok(Json.toJson(paper.map(_.toResource)))
    })
  }



  def addPaper():Action[AnyContent] = AuthorizedAction.async{ implicit request=>
    println(request.body.asText)
    println(request.body.asJson)
    request.body.asJson.map(ele => {
      println(ele)
      paperResourceRepo.add(ele.as[ResearchPaperResource].toPaper)
     })
    Future.successful(Ok("added"))

   /* paperResourceRepo.get(id).map(paper=>{
      Ok(Json.toJson(paper.map(_.toResource)))
    })*/
  }

  def searchByTitle(title:String):Action[AnyContent] = AuthorizedAction.async{ implicit request =>
        paperResourceRepo.fetchByTitle(title)
        .map(papersList=> Ok(Json.toJson(papersList.map(_.toResource))))
  }
  def searchByAuthor(author:String):Action[AnyContent] = AuthorizedAction.async{ implicit request =>
    paperResourceRepo.fetchByAuthor(author)
      .map(papersList=> Ok(Json.toJson(papersList.map(_.toResource))))
  }
  def searchByContent(text:String):Action[AnyContent] = AuthorizedAction.async{ implicit request =>
    paperResourceRepo.fetchByContent(text)
      .map(papersList=> Ok(Json.toJson(papersList.map(_.toResource))))
  }

  def searchByPublishedDate(fromDate:Option[String],toDate:Option[String]):Action[AnyContent] = AuthorizedAction.async{ implicit request =>
    (fromDate.flatMap(DateUtil.stringToDate(_)), toDate.flatMap(DateUtil.stringToDate(_))) match{
      case (Some(from), Some(to)) => paperResourceRepo.fetchByPublishedDate(from,to)
        .map(papersList => Ok(Json.toJson(papersList.map(_.toResource))))
      case (_,_) => Future.successful(Ok(" from and to dates should be provided in a valid format [yyyy/MM/dd]"))
    }
  }
}
