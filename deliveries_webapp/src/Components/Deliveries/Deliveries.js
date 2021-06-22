/* css */
import './Deliveries.css';

/* react */
import { useEffect, useState } from 'react';

//import MapSection from '../map/Map.js'
import Map from '../map/Map.js'

/* Geocode */
import Geocode from "react-geocode";
import OrdersService from "../../Services/order.service";

import { withScriptjs } from "react-google-maps";

function Deliveries() {
    Geocode.setApiKey("AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco");
    Geocode.setLanguage("en");
    const [orderInfo, setOrderInfo] = useState({});

    const MapLoader = withScriptjs(Map);

    async function getAddressCoord(lat,long){
        console.log(lat);
        console.log(long);
        const response = await Geocode.fromLatLng(parseFloat(lat), parseFloat(long));

        let address_components = response.results[0].formatted_address.split(",");
        
        let address = address_components[0];
        let zip;
        let city;
        if (address_components[1] !== undefined) {
            zip = address_components[1].split(" ")[1]
            city = address_components[1].split(" ")[2]
        }
        let country = address_components[2];

        return [address, zip, city, country];
    }

    useEffect(() => {
        async function getOrderById(order_id) {
            let orderInformation = await OrdersService.getOrderById(order_id);
            if (!orderInformation["error"]) {
                let pickup = await getAddressCoord(orderInformation.pick_up_lat, orderInformation.pick_up_lng);
                let deliver = await getAddressCoord(orderInformation.deliver_lat, orderInformation.deliver_lng);
                pickup = pickup[0] + ", " + pickup[3];
                deliver = deliver[0] + ", " + deliver[3];
                setOrderInfo({...orderInformation, pickup: pickup, deliver: deliver})
            }
        }
        const url = new URLSearchParams(window.location.search);
	    let order_id = url.get("id");

        if (order_id !== undefined && order_id !== null) {
            getOrderById(order_id);
        }
    }, []);
    return (
      <>
        <div className="DeliveriesSection del">
            <div>
                {/*<img src={process.env.PUBLIC_URL + "/images/mapexample.png"} alt="map"></img>*/}
                {<MapLoader 
                googleMapURL="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco"
                loadingElement={<div style={{ height: "100%"}}/>}
                pick_up_lat={orderInfo.pick_up_lat}
                pick_up_lng={orderInfo.pick_up_lng}
                deliver_lat={orderInfo.deliver_lat}
                deliver_lng={orderInfo.deliver_lng}
                />
                }
            </div>
            <div className="DeliveryDetails">
                <h1>Delivery Details</h1>
                <h4>Pick Up Address</h4>
                <i>{orderInfo.pickup}</i>
                <h4>Delivery Address</h4>
                <i>{orderInfo.deliver}</i>
                <div className="ItemDetails">
                    <h4>Items total weight</h4>
                    <p id="total_weight">{orderInfo.weight} kg</p>
                </div>
                <h4>Rating</h4>
                <i id="rating">{orderInfo.rating}</i>
            </div>
        </div>
        
      </>
      
    );
  }
  
  export default Deliveries;