package actors

import akka.actor.{Actor, ActorRef}
import models.{Elevator, Floor, Passenger}

class ElevatorManager extends Actor {
  private var users = List.empty[ActorRef]
  private val elevator = new Elevator(List.range(1, 10).map(Floor(_, List.empty[Passenger])), List.empty[Passenger], 1)

  import ElevatorManager._

  override def receive: Receive = {
    case newUser(user) => users ::= user
    case newPassenger(passenger: Passenger) =>
      elevator.floors.filter(_.id == passenger.fromFloor).foreach(_.passengerList ::= passenger)
      users.foreach(_ ! ElevatorActor.drawElevator(elevator))
    case nextMove() =>
      elevator.move()
      users.foreach(_ ! ElevatorActor.drawElevator(elevator))
    case m => println("Unhandled message in ElevatorManager: " + m)
  }
}

object ElevatorManager {
  case class newPassenger(passenger: Passenger)
  case class newUser(user: ActorRef)
  case class nextMove()
}
