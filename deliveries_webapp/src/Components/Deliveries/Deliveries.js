/* css */
import './Deliveries.css';

/* react */
import { useEffect, useState } from 'react';
import { useLocation } from 'react-router';

//import MapSection from '../map/Map.js'
import Map from '../map/Map.js'

import { withScriptjs } from "react-google-maps";

function Deliveries() {
    const [state, setState] = useState("");
    
    const location = useLocation();

    const MapLoader = withScriptjs(Map);

    useEffect(() => {
        console.log(location.state);
        if (location.state === undefined) {
            setState("offline");
        } else if (location.state.is_History){
            setState("delivering");
        }
    }, [location.state]);
    return (
      <>
        { state === "offline" && 
            <div className="DeliveriesSection off">
                <h2> You are currently offline. Tap the button to be available to attend deliveries</h2>
                <button onClick={() => setState("waiting")}>online</button>
            </div>
        }
        { state === "waiting" && 
            <div onClick={() => setState("delivering")} className="DeliveriesSection wait">
                <div>
                    <div className="bouncer">
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                    </div>
                </div>
                
                <div>
                    <h1>Waiting for a delivery</h1>
                </div>
                
            </div>
        }
        { state === "delivering" && 
            <div className="DeliveriesSection del">
                <div>
                    {/*<img src={process.env.PUBLIC_URL + "/images/mapexample.png"} alt="map"></img>*/}
                    {<MapLoader 
                    googleMapURL="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco"
                    loadingElement={<div style={{ height: "100%"}}/>}
                    />
                    }
                </div>
                <div className="DeliveryDetails">
                    <h1>Delivery Details</h1>
                    <h2>Pick Up Address</h2>
                    <i>xxxxxxxxxxxxxxxxxxxxxxx</i>
                    <h2>Delivery Address</h2>
                    <i>xxxxxxxxxxxxxxxxxxxxxxx</i>
                    <div className="ItemDetails">
                        <h1>Item details</h1>
                        <h2>Item</h2>
                        <i>xxxxxxxxxxxxxxxxxxxxxxx</i>
                        <h2>Estimated Weight</h2>
                        <i>xxxxxxxxxxxxxxxxxxxxxxx</i>
                    </div>
                </div>
            </div>
        }
        
      </>
      
    );
  }
  
  export default Deliveries;