import javax.inject._
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import papers.repository.{ResearchPaperRepository, ResearchPaperRepositoryImpl}
import play.api.{Configuration, Environment}

class Module(environment: Environment, configuration: Configuration)
  extends AbstractModule
    with ScalaModule {

  override def configure() = {
    bind[ResearchPaperRepository].to[ResearchPaperRepositoryImpl].in[Singleton]
  }
}
