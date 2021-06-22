/* css */
import './History.css';

/* react */
import { useHistory } from "react-router-dom";
import React, { useState, useEffect } from 'react';

import AuthService from "../../Services/auth.service";
import OrdersService from "../../Services/orders.service";

import * as BsIcons from 'react-icons/bs';

/* Geocode */
import Geocode from "react-geocode";

function History() {
    Geocode.setApiKey("AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco");
    Geocode.setLanguage("en");

    const history = useHistory();
    const [userOrders, setUserOrders] = useState([]);

    const routeChange = (order_id) =>{ 
        let path = '/delivery?id=' + order_id; 
        history.push(path, {is_History:false});
    }

    async function getAddressCoord(lat,long){
        const response = await Geocode.fromLatLng(lat, long);

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
        var current_user = AuthService.getCurrentUser();
        async function getOrdersByUser() {
          let res = await OrdersService.getOrdersByUser(current_user.id);
          if (!res.error) {
            for (var i = 0; i < res.length; i++) {
                let address_components = await getAddressCoord(res[i].pick_up_lat, res[i].pick_up_lng);
                let pick_up_address = address_components[0];
                res[i].pick_up = pick_up_address;
                address_components = await getAddressCoord(res[i].deliver_lat, res[i].deliver_lng);
                let destiny_address = address_components[0];
                res[i].destiny = destiny_address;
            }
            
            setUserOrders(res);
            console.log(res);
          }
        }
        getOrdersByUser();
      }, []);


    return (
      <>
        <div className="HistorySection">
            <div className="historyTable">
                <h1 id="title"> Last Deliveries</h1>
                <ul className="list-group">
                    <li className="list-item">
                        <div>
                            Date
                        </div>
                        <div>
                            Weight
                        </div>
                        <div>
                            Your address
                        </div>
                        <div>
                            Destiny Address
                        </div>
                        <div>
                            Time
                        </div>
                        <div>
                            Status
                        </div>
                        <div>
                            Rating
                        </div>
                    </li>

                    {userOrders.map(function(order, index) {
                        var date = order.creation_date.split("T")[0];
                        var time = order.creation_date.split("T")[1].split(".")[0];
                        var sumweight = 0;
                        for (var i = 0; i < order.items.length; i++) {
                            sumweight = sumweight + order.items[i].weight;
                        }
                        return (
                            <li id={"order-" + index} className="list-item" onClick={() => routeChange(order.deliver_id)}>
                                <div>
                                    {date}
                                </div>
                                <div>
                                    {sumweight} kg
                                </div>
                                <div>
                                    {order.pick_up}
                                </div>
                                <div>
                                    {order.destiny}
                                </div>
                                <div>
                                    {time}
                                </div>
                                <div>
                                    {order.status}
                                </div>
                                <div>
                                    {order.rating}
                                    <BsIcons.BsStarFill className="user-rating historic-star"/>
                                </div>
                            </li>

                        )
                    })}
                    
                </ul>
            </div>            
        </div>
      </>
      
    );
  }
  
export default History;