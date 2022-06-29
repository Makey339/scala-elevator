package controllers

import actors.{ElevatorActor, ElevatorManager}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc._

import javax.inject._
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

@Singleton
class ElevatorController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends AbstractController(cc) {
  val manager: ActorRef = system.actorOf(Props[ElevatorManager], "Manager")

  import system.dispatcher
  system.scheduler.scheduleWithFixedDelay(10 seconds, 2 seconds, manager, ElevatorManager.nextMove())

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
