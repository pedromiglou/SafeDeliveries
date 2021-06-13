import React, { Component } from "react";

import {
  withGoogleMap,
  GoogleMap,
  Marker
} from "react-google-maps";

class Map extends Component {

  constructor(props) {
    super(props);
    this.state = {
      directions: null,
      markers: [
        {
          name: "Current position",
          position: {
            lat: this.props.state.lat,
            lng: this.props.state.lng
          }
        }
      ]
    }
  }


/*  onMarkerDragEnd = (coord, index) => {
    console.log("tou markerdrag")
    const { latLng } = coord;
    const lat = latLng.lat();
    const lng = latLng.lng();

    this.setState(prevState => {
      console.log("im here")
      const markers = [...this.state.markers];
      console.log(markers)
      markers[index] = { ...markers[index], position: { lat, lng } };
      console.log(markers)
      return { markers };
    });
  };*/

  changePos = (t) => {
    console.log(this.props.state)
    var lat = t.latLng.lat();
    var lng = t.latLng.lng();
    this.props.parentCallback({lat: lat, lng: lng});
  }

  render() {
    const GoogleMapExample = withGoogleMap(props => (
      <GoogleMap
        defaultCenter={{ lat: this.props.state.lat, lng: this.props.state.lng }}
        defaultZoom={13}
      >
        
        {this.state.markers.map((marker, index) => (
          <Marker 
            key={index}
            position={marker.position}
            draggable={true}
            onDragEnd={(t, map, coord) => {this.changePos(t)}}
            //onDragEnd={(t, map, coord) => {this.changePos(t, map, coord, index); this.onMarkerDragEnd(coord, index);}}
            //onDrag={(t, map, coord) => {this.changePos(coord, index)}}
            name={marker.name}
          />
        ))}
      </GoogleMap>
    ));

    return (
      <div>
        <GoogleMapExample
          containerElement={<div style={{ height: `500px`}} />}
          mapElement={<div style={{ height: `100%`, width: '45vw', minWidth: '350px' }} />}
        />
      </div>
    );
  }
}

export default Map;