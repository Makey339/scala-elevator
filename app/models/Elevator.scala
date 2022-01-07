package models

import akka.http.scaladsl.model.Uri.Empty

import scala.runtime.Nothing$

class Elevator(var floors: List[Floor], var passengers: List[Passenger], var currentFloor: Int) {

  var direction:Boolean = true

  private def getDestinations: List[Int] = {
    passengers.map(_.toFloor).appendedAll(floors.filter(_.passengerList.nonEmpty).flatMap(_.passengerList).map(_.toFloor))
  }

  def move() = {

    if (currentFloor==1)
      getPassengersFromFloor()

    val destinations = getDestinations
    if (destinations.nonEmpty) {
      checkDirecton()
      if (direction)
        currentFloor += 1
      else
        currentFloor -= 1
    }
    println("пассажиры" + passengers)
    println("этажи" + floors)
    ejectPassengers()
    getPassengersFromFloor()
  }

  private def getPassengersFromFloor() = {
    passengers = passengers.appendedAll(floors.find(_.id==currentFloor).getOrElse(floors(1)).passengerList)
    floors.foreach(f => if (f.id==currentFloor) f.cleanPassengers())
  }

  private def ejectPassengers() = {
    passengers = passengers.filterNot(_.toFloor==currentFloor)
  }

  private def checkDirecton() = {
    if ((direction&&(getDestinations.max<currentFloor))||(currentFloor==9))
      direction = false
    if ((!direction)&&(getDestinations.min>currentFloor)||(currentFloor==1))
      direction = true
  }
}
