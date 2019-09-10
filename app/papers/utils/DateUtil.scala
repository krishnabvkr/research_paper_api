package papers.utils

import java.time.LocalDate

import scala.util.{Failure, Success, Try}

object DateUtil {

  import java.time.format.DateTimeFormatter

  def dateToString(date:LocalDate)={
    date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
  }

  def stringToDate(date:String)={
    Try(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd"))) match{
      case Success(value) => Some(value)
      case Failure(_) => None
    }
  }

}
