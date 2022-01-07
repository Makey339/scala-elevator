package actors

import actors.ElevatorActor.drawElevator
import akka.actor.{Actor, ActorRef, Props}
import models.{Elevator, Passenger}
import play.libs.Json

class ElevatorActor(out: ActorRef, manager: ActorRef) extends Actor {
  manager ! ElevatorManager.newUser(self)

  override def receive: Receive = {
    case s: String => if (s.contains(' ')) manager ! ElevatorManager.newPassenger(Passenger(s.split(' ')(0).toInt, s.split(' ')(1).toInt))
      else manager ! ElevatorManager.nextMove()
    case drawElevator(elevator: Elevator) => out ! Json.toJson(elevator).toString
    case m => println("Unhandled message in ElevatorActor " + m)
  }
}

object ElevatorActor {
  def props(out: ActorRef, manager: ActorRef) = Props(new ElevatorActor(out, manager))

  case class drawElevator(elevator: Elevator)
}
