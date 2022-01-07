package controllers

import actors.{ElevatorActor, ElevatorManager}
import akka.actor.{ActorSystem, Props}
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc._

import javax.inject._

@Singleton
class ElevatorController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {
  val manager = system.actorOf(Props[ElevatorManager], "Manager")

  def index = Action {
    implicit request => Ok(views.html.index())
  }

  def socket = WebSocket.accept[String, String] { request =>
    println("Getting socket")
    ActorFlow.actorRef { out =>
      ElevatorActor.props(out, manager)
    }
  }
}
