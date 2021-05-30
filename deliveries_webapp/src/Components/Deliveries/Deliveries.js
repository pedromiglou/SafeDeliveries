/* css */
import './Deliveries.css';

/* react */
import { useEffect, useState } from 'react';
import { useLocation } from 'react-router';

function Deliveries() {
    const [state, setState] = useState("");

    const location = useLocation();

    useEffect(() => {
        console.log(location.state);
        if (location.state === undefined) {
            setState("offline");
        } else if (location.state.is_History){
            setState("delivering");
        }
    }, []);
    return (
      <>
        { state === "offline" && 
            <div className="DeliveriesSection off">
                <h2> You are currently offline. Tap the button to be available to attend deliveries</h2>
                <button onClick={() => setState("waiting")}>online</button>
            </div>
        }
        { state === "waiting" && 
            <div className="DeliveriesSection wait">
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

                    <button onClick={() => setState("delivering")}>Test- Get a delivery</button>
                </div>
                
            </div>
        }
        { state === "delivering" && 
            <div className="DeliveriesSection del">
                <div>
                    <img src={process.env.PUBLIC_URL + "/images/mapexample.png"} alt="map"></img>
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