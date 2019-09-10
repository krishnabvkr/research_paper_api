package papers

import javax.inject.Inject
import papers.repository.{ResearchPaperRepository, UserRepository}
import play.api.http.FileMimeTypes
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class AuthorizedActionBuilder @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext)
  extends ActionBuilderImpl(parser) {

  private val logger = play.api.Logger(this.getClass)

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    logger.debug("ENTERED AuthenticatedUserAction::invokeBlock")
    val userName = request.headers.get("username").fold("")(identity)
    if (UserRepository.isAuthorizedUser(userName)) {
      block(request)
    } else {
      Future.successful(Results.Unauthorized("Unauthorized access !!"))
    }
  }
}

/**
  * Packages up the component dependencies for the   ResearchPaperController.
  *
  * This is a good way to minimize the surface area exposed to the controller, so the
  * controller only has to have one thing injected.
  */
case class PostControllerComponents @Inject()(
    authorizedActionBuilder: AuthorizedActionBuilder,
    researchPaperRepository: ResearchPaperRepository,
    actionBuilder: DefaultActionBuilder,
    parsers: PlayBodyParsers,
    messagesApi: MessagesApi,
    langs: Langs,
    fileMimeTypes: FileMimeTypes,
    executionContext: scala.concurrent.ExecutionContext)
    extends ControllerComponents

/**
  * Exposes actions and handler to the ResearchPaperController by wiring the injected state into the base class.
  */
class PostBaseController @Inject()(pcc: PostControllerComponents)
    extends BaseController {
  override protected def controllerComponents: ControllerComponents = pcc
  def AuthorizedAction: AuthorizedActionBuilder = pcc.authorizedActionBuilder
  def paperResourceRepo: ResearchPaperRepository = pcc.researchPaperRepository
}
