$(document).ready(function(){
	
	$("#loginform").click(function(){
		$("#info-container").load("resources/loginform.html");
	});
	
	$("#registerform").click(function(){
    	$("#info-container").load("resources/registerform.html");
    });

    $("#userpage").click(function(){
    	$("#info-container").load("resources/users.html");
    });

    $("#businessbyidpage").click(function(){
    	$("#info-container").load("resources/businessbyid.html");
    });
	
});
  



  
  
 let map;

function initMap() {
	map = new google.maps.Map(document.getElementById("map-canvas"), {
		center: { lat: 43.4578, lng: -88.8373 },
		zoom: 15,
		mapTypeId: google.maps.MapTypeId.SATELLITE //roadmap - satellite - hybrid - terrain
	});
	createButtons();
}


function handleLocationError(browserHasGeolocation, infoWindow, pos) {
	infoWindow.setPosition(pos);
	infoWindow.setContent(
		browserHasGeolocation
		? "Error: The Geolocation service failed."
		: "Error: Your browser doesn't support geolocation."
	);
	infoWindow.open(map);
}




/*
function initMap() {} //global

$(() => {
  initMap = function() {
    var myLatLng = new google.maps.LatLng(43.4578, -88.8373);
        var mapOptions = {
            zoom: 15,
            center: myLatLng,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
		
        var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
        
		var marker = new google.maps.Marker({
            position: myLatLng,
            map: map
        });
  }
});
*/


function createButtons(){
	createButtonCurrentLocation();
	addMarker();
	addInfo();
}




function createButtonCurrentLocation(){
	let iw_currentLocation = new google.maps.InfoWindow();
	const locationButton = document.getElementById("btn-current-location");
	locationButton.textContent = "Pan to Current Location";
	locationButton.classList.add("custom-map-control-button");
	locationButton.addEventListener("click", () => {
	// Try HTML5 geolocation.
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(
			(position) => {
				const pos = {
					lat: position.coords.latitude,
					lng: position.coords.longitude,
				};
				iw_currentLocation.setPosition(pos);
				iw_currentLocation.setContent("You Are Here.");
				iw_currentLocation.open(map);
				map.setCenter(pos);
        },
        () => {
          handleLocationError(true, iw_currentLocation, map.getCenter());
        }
      );
    } else {
      // Browser doesn't support Geolocation
      handleLocationError(false, iw_currentLocation, map.getCenter());
    }
  });
}




function addInfo(){
	const myLatlng = { lat: -25.363, lng: 131.044 };
	let infoWindow = new google.maps.InfoWindow({
    content: "Click the map to get Lat/Lng!",
    position: myLatlng,
  });
  //infoWindow.open(map);
  // Configure the click listener.
  map.addListener("click", (mapsMouseEvent) => {
    
    infoWindow.close();// Close the current InfoWindow.
    infoWindow = new google.maps.InfoWindow({// Create a new InfoWindow.
      position: mapsMouseEvent.latLng,
    });
    infoWindow.setContent(
      JSON.stringify(mapsMouseEvent.latLng.toJSON(), null, 2)
    );
    infoWindow.open(map);
  });

}











function addMarker(){

    const image1 = {
    //url:"https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png",
	url:"resources/images/shirtIcon.png",
    size: new google.maps.Size(20, 20),// This marker is 20 pixels wide by 20 pixels high.
    origin: new google.maps.Point(0, 0),// The origin for this image is (0, 0).-- top-left   
    anchor: new google.maps.Point(0, 20)// The anchor for this image is the base.
  };
	  const image0 = {
    //url:"https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png",
	url:"resources/images/burgerIcon.png",
    size: new google.maps.Size(20, 20),// This marker is 20 pixels wide by 20 pixels high.
    origin: new google.maps.Point(0, 0),// The origin for this image is (0, 0).-- top-left   
    anchor: new google.maps.Point(0, 20)// The anchor for this image is the base.
  };

const business = [
  ["Willie's House of Debauchery", 43.455678, -88.84455373, 1,image0],
  ["Piggly Wiggly", 43.4542445738, -88.83662373, 1,image0],
  ["Drowned Rat: Pest Control", 43.459878, -88.8485085373, 1,image1],
  ["Daily Thrift", 43.4578, -88.83, 2,image1],
  ["Berries Baked Goods", 43.458, -88.8373, 1,image0]
];
  
  const shape = {// Shapes define the clickable region of the icon. The type defines an HTML
    coords: [1, 1, 1, 20, 20, 20, 20, 1],//[top-left(x,y),top-right(x,y),bottom-right(x,y),bottom-left(x,y)]
    type: "poly"
  };

  for (let i = 0; i < business.length; i++) {
    new google.maps.Marker({
      position: { lat: business[i][1], lng: business[i][2] },
      map,
      icon: business[i][4],
      shape: shape,
      title: business[i][0],
      zIndex: business[i][3]
    });
  }
}


  
  
  
  
function moveMarker(map, marker) {

    //delayed so you can see it move
    setTimeout(function () {

        marker.setPosition(new google.maps.LatLng(43.4578, -88.8373));
        map.panTo(new google.maps.LatLng(43.4578, -88.8373));

    }, 1500);

};


























