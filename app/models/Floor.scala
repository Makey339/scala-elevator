package models

case class Floor(id: Int,var passengerList: List[Passenger]) {

  def cleanPassengers(): Unit = {
    passengerList = List.empty[Passenger]
  }

}
