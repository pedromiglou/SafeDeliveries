/* css */
import './Delivery.css';

/* react */
import { useEffect, useState } from 'react';
import { useLocation } from 'react-router';

function Delivery() {
    const [state, setState] = useState("Requesting");

    const location = useLocation();

    useEffect(() => {
        console.log(location.state);
        if (location.state === undefined) {
            setState("Requesting");
        } else if (location.state.is_History){
            setState("confirmed");
        }
    }, [location.state]);

    return (
      <>
        {state === "Requesting" && 
            <div className="DeliveriesSection req">
                <div className="DeliveryDetails">
                    <h1>Fill the Details</h1>
                    <h2>Pick Up Address</h2>
                    <input type="text" placeholder="Address"/>
                    <h2>Destin Address</h2>
                    <input type="text" placeholder="Address"/>
                    <div className="ItemDetails">
                        <h1>Item details</h1>
                        <h2>Item type</h2>
                        <select>
                            <option selected defaultValue>Choose item type</option>
                        </select>
                        <h2>Estimated Weight</h2>
                        <input type="number" placeholder="weigth"/>
                    </div>
                </div>
                <div className="confirm">
                    <div className="image-confirm">
                        <img src={process.env.PUBLIC_URL + "/images/mapexample.png"} alt="map"></img>
                    </div>
                    <div className="button-confirm">
                        <button onClick={() => setState("waiting_rider")}>Confirm</button>
                    </div>
                </div>
                
            </div>
        }
        { state === "waiting_rider" && 
            <div onClick={() => setState("confirmed")} className="DeliveriesSection wait">
                <div>
                    <div className="bouncer">
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                    </div>
                </div>
                
                <div>
                    <h1>Waiting for a rider</h1>
                </div>
                
            </div>
        }
        {state === "confirmed" && 
            <div className="DeliveriesSection conf">
                <div className="current_image">
                    <img src={process.env.PUBLIC_URL + "/images/mapexample.png"} alt="map"></img>
                </div>
                <h1>Order details</h1>
                <div className="ConfirmDeliveryDetails">
                    <div>
                        <h2>Pick Up Address</h2>
                        <h3>
                            xxxx
                        </h3>
                    </div>
                    <div>
                        <h2>Destin Address</h2>
                        <h3>
                            xxxx
                        </h3>
                    </div>
                    
                    <div>
                        <h2>Item type</h2>
                        <h3>
                            xxxx
                        </h3>
                    </div>
                    
                    <div>
                        <h2>Estimated Weight</h2>
                        <h3>
                            xxxx
                        </h3>
                    </div>
                    
                </div>
            </div>
        }
        
        
      </>
      
    );
  }
  
  export default Delivery;