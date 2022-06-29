/**
 * Code for websocket
 */

const fromFloor = document.getElementById("fromFloor");
const toFloor = document.getElementById("toFloor");
const addBtn = document.getElementById("addButton")
const floorsDiv = document.getElementById("floors")


const socketRoute = document.getElementById("ws-route").value;
const socket = new WebSocket(socketRoute.replace("http", "ws"));

addBtn.onclick = () => {
    socket.send(fromFloor.value + " " + toFloor.value)
}

// bad practice
//setInterval(function move() {
//    socket.send("move")
//}, 10000)


socket.onmessage = (event) => {
    console.log(event.data)

    drawElevator(event.data)
}

function drawElevator(data) {
    floorsDiv.innerHTML = '';
    const jsonData = JSON.parse(data);
    const floors = jsonData.floors;
    for (let i = floors.length; i > 0; i--) {
        let floorContainer = document.createElement('li');
        // floorContainer.style.display="inline-block";

        let elevatorDiv = document.createElement('div');
        elevatorDiv.classList.add("elevatorFloor");
        if (i === jsonData.currentFloor)
            elevatorDiv.classList.add("current");

        let passengerDiv = document.createElement('div');
        passengerDiv.classList.add("passengerFloor");
            console.log(floors[i-1]);
            addPasengers(passengerDiv,floors[i-1])

        floorContainer.appendChild(elevatorDiv)
        floorContainer.appendChild(passengerDiv)
        floorsDiv.appendChild(floorContainer);
    }
}

function addPasengers(passengerDiv, floor) {
    const passengers = floor.passengerList;
    console.log(passengers)
    for (let i = 0; i < passengers.length; i++) {
        let passenger = document.createElement('div')
        passenger.classList.add("passenger")
        passenger.setAttribute("background", getRandomColor())
        passenger.textContent = passengers[i].toFloor;
        passengerDiv.appendChild(passenger);
    }

}

function getRandomColor() {
    var letters = '0123456789ABCDE';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}