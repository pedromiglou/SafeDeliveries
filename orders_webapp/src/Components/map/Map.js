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
          name: "pick_up_position",
          position: {
            lat: this.props.state.pick_up_lat,
            lng: this.props.state.pick_up_lng
          }
        }, 
        {
          name: "delivery_position",
          position: {
            lat: this.props.state.del_lat,
            lng: this.props.state.del_lng
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

  changePos = (t,marker) => {
    console.log(this.props.state)
    var lat = t.latLng.lat();
    var lng = t.latLng.lng();
    this.props.parentCallback({lat: lat, lng: lng, marker_id: marker});
  }

  render() {
    const GoogleMapExample = withGoogleMap(props => (
      <GoogleMap
        defaultCenter={{ lat: this.props.state.pick_up_lat, lng: this.props.state.pick_up_lng }}
        defaultZoom={13}
      >
        
        {this.state.markers.map((marker, index) => (
          <Marker 
            key={index}
            position={marker.position}
            draggable={true}
            onDragEnd={(t, map, coord) => {this.changePos(t, marker.name)}}
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
          containerElement={<div style={{ height: `600px`}} />}
          mapElement={<div style={{ height: `100%`, width: '70vw', minWidth: '200px' }} />}
        />
      </div>
    );
  }
}

export default Map;