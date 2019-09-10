package papers.repository

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import javax.inject.{Inject, Singleton}
import akka.actor.ActorSystem
import papers.utils.DateUtil
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import scala.concurrent.Future




case class ResearchPaper(id:Int, title:String, authors:List[String],
                         publishedDate:LocalDate,
                         content:String){
  def toResource =ResearchPaperResource(id,
    title,authors,DateUtil.dateToString(publishedDate),content.dropRight(20))
}

/**
  * DTO for displaying page information.
  */
case class ResearchPaperResource(id:Int, title:String, authors:List[String],
                                 publishedDate:String,
                                 content:String){
  def toPaper = ResearchPaper(id, title, authors,
    DateUtil.stringToDate(publishedDate).get,
    content)

}




class ResearchPaperExecutionContext @Inject()(actorSystem: ActorSystem)
    extends CustomExecutionContext(actorSystem, "repository.dispatcher")

/**
  * A pure non-blocking interface for the PostRepository.
  */
trait ResearchPaperRepository {

  def list()(implicit mc: MarkerContext): Future[Iterable[ResearchPaper]]

  def get(id: Int)(implicit mc: MarkerContext): Future[Option[ResearchPaper]]
  def add(paper:ResearchPaper)(implicit mc: MarkerContext): Future[Unit]

  def fetchByTitle(title:String)(implicit mc: MarkerContext): Future[Iterable[ResearchPaper]]
  def fetchByAuthor(author:String)(implicit mc: MarkerContext): Future[Iterable[ResearchPaper]]
  def fetchByContent(text:String)(implicit mc: MarkerContext): Future[Iterable[ResearchPaper]]
  def fetchByPublishedDate(fromDate:LocalDate, toDate:LocalDate)(implicit mc: MarkerContext): Future[Iterable[ResearchPaper]]
}


@Singleton
class ResearchPaperRepositoryImpl @Inject()()(implicit ec: ResearchPaperExecutionContext)
    extends ResearchPaperRepository {

  private val logger = Logger(this.getClass)
  val dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd")

  private var researchPaperList = List(
       ResearchPaper(1,"scala",List("MartinOderskey"), LocalDate.parse("2018/06/03",dateFormat), "scala is a Hybrid Programming Language")
      ,ResearchPaper(2,"akka",List("Roobert","downey"), LocalDate.parse("2019/01/03",dateFormat), "akka is a powerful framework for concurrent" +
      " programming and developed on scala")
      ,ResearchPaper(3,"play",List("Lightbend"), LocalDate.parse("2019/03/03",dateFormat), "play is best web application development framework to" +
      "develop applications in scala and this framework is developed on scala and akka ")
  )

  override def list()(
      implicit mc: MarkerContext): Future[Iterable[ResearchPaper]] = {
    Future {
      logger.trace(s"list: ")
      researchPaperList
    }
  }

  override def get(id: Int)(
      implicit mc: MarkerContext): Future[Option[ResearchPaper]] = {
    Future {
      logger.trace(s"get: id = $id")
      researchPaperList.find(paper => paper.id == id)
    }
  }

  override def fetchByTitle(title: String)(implicit mc: MarkerContext): Future[Iterable[ResearchPaper]] =
  {
    Future {
      researchPaperList.filter(_.title == title)
    }
  }

  override def fetchByAuthor(author: String)(implicit mc: MarkerContext): Future[Iterable[ResearchPaper]] = {
    Future {
      researchPaperList.filter(_.authors.contains(author))
    }
  }

  override def fetchByContent(text: String)(implicit mc: MarkerContext): Future[Iterable[ResearchPaper]] = {
    Future {
      researchPaperList.filter(_.content.contains(text))
    }
  }

  def fetchByPublishedDate(fromDate:LocalDate, toDate:LocalDate)(implicit mc: MarkerContext): Future[Iterable[ResearchPaper]]={
    Future {
      researchPaperList.filter(paper => (paper.publishedDate.compareTo(fromDate) >= 0
        && paper.publishedDate.compareTo(toDate) <=0))
    }
  }

  override def add(paper: ResearchPaper)(implicit mc: MarkerContext): Future[Unit] = {
    Future {
      researchPaperList = researchPaperList.+:(paper)
    }
  }
  //Future(123).onComplete(


}
