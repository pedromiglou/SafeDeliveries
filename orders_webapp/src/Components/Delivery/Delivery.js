/* css */
import './Delivery.css';

/* react */
import { useEffect, useState, useCallback } from 'react';
import { useLocation } from 'react-router';

import * as MdIcons from 'react-icons/md';
// import * as BiIcons from 'react-icons/bi';
import * as RiIcons from 'react-icons/ri';
import * as GiIcons from 'react-icons/gi';


import Map from '../map/Map.js'
import { withScriptjs } from "react-google-maps";

function Delivery() {
    const [state, setState] = useState("Requesting");

    const [items, setItems] = useState([]);

    const [itemEditable, setItemEditable] = useState({key: -2, name: "", editable: false})
    const [newItem, setNewItem] = useState(false);

    const [pick_up_lat, setPickUpLat] = useState(40.756795);
    const [pick_up_lng, setPickUpLng] = useState(-73.954298);
    const [deliver_lat, setDeliverLat] = useState(40.756795);
    const [deliver_lng, setDeliverLng] = useState(-73.954298);

    const pickUpMapCallBack = useCallback((msg) => { 
        setPickUpLat(msg.lat)
        setPickUpLng(msg.lng)
    }, []);

    const deliverMapCallBack = useCallback((msg) => { 
        setDeliverLat(msg.lat)
        setDeliverLng(msg.lng)
    }, []);

    const location = useLocation();

    const MapLoader = withScriptjs(Map);

    useEffect(() => {
        if (location.state === undefined) {
            setState("Requesting");
        } else if (location.state.is_History){
            setState("confirmed");
        }
    }, [location.state]);

    useEffect(() => {
        if (itemEditable["key"] === -2){
            return
        }

        let name_inp = document.getElementById("name_" + itemEditable["key"]);
        let category_inp = document.getElementById("category_" + itemEditable["key"]);
        let weight_inp = document.getElementById("weight_" + itemEditable["key"]);

        let icons_ed = document.getElementById("icons_ed" + itemEditable["key"]);
        let icons_cc = document.getElementById("icons_cc" + itemEditable["key"]);

        if(itemEditable["editable"]){
            name_inp.readOnly = false;
            category_inp.readOnly = false;
            weight_inp.readOnly = false;

            name_inp.classList.add("editable");
            category_inp.classList.add("editable");
            weight_inp.classList.add("editable");

            icons_ed.style.display = "none";
            icons_cc.style.display = "flex";
        } else {
            name_inp.readOnly = true;
            category_inp.readOnly = true;
            weight_inp.readOnly = true;

            name_inp.classList.remove("editable");
            category_inp.classList.remove("editable");
            weight_inp.classList.remove("editable");

            icons_ed.style.display = "flex";
            icons_cc.style.display = "none";
        }
    }, [itemEditable]);

    useEffect(() => {
    
        let both_buttons = document.getElementById("both_buttons");
        let add_button = document.getElementById("button-add");
        let new_item_form = document.getElementById("new-item");

        if(newItem){
            both_buttons.style.display = "flex";
            add_button.style.display = "none";
            new_item_form.style.display = "block";
        } else {
            both_buttons.style.display = "none";
            add_button.style.display = "block";
            new_item_form.style.display = "none";
        }
    }, [newItem]);


    function submitOrder(){
        const order = { pick_up_lat: pick_up_lat,
                        pick_up_lng: pick_up_lng,
                        deliver_lat: deliver_lat,
                        deliver_lng: deliver_lng,
                        items: items}
        console.log(order)
        // await RiderService.changeRider(current_user.id, firstname, lastname, email)
        //setState("waiting_rider")
    }


    function removeItem(item_id) {
        console.log(items)
        console.log(item_id)
        var newitems = items.splice(item_id, 1);

        if (newItem)
            setNewItem(false)
        else
            setNewItem(true)
    }

    function editItem(key) {
        var name = document.getElementById("name_"+key).value;
        var category = document.getElementById("category_"+key).value;
        var weight = document.getElementById("weight_"+key).value;
        if (name === "") name = document.getElementById("name_"+key).placeholder;
        if (category === "") category = document.getElementById("category_"+key).placeholder;
        if (weight === "") weight = document.getElementById("weight_"+key).placeholder;
        var item = {"name":name, "category":category,"weight": weight}
        items[key] = item
    }

    function addItem(){
        var name = document.getElementById("n_name");
        var category = document.getElementById("n_category");
        var weight = document.getElementById("n_weight");
        var item = {"name":name.value, "category":category.value,"weight": weight.value}
        name.value = "";
        category.value = "";
        weight.value = "";
        items.push(item)
        setItems(items)
    }


    return (
      <>
        {state === "Requesting" && 
            <div className="DeliveriesSection req">
                <div className="DeliveryDetails">
                    <h1>Delivery Details</h1>
                    
                    <div className="div-addresses">
                        <div>
                            <label htmlFor="pickup">Pick Up Address</label>
                            {<MapLoader 
                                googleMapURL="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco"
                                loadingElement={<div style={{ height: "100%"}}/>}
                                state={ {lat:pick_up_lat, lng: pick_up_lng}}
                                parentCallback = {pickUpMapCallBack}
                                />
                            }
                        </div>
                        <div>
                            <label htmlFor="destiny">Destiny Address</label>
                            {<MapLoader 
                                googleMapURL="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco"
                                loadingElement={<div style={{ height: "100%"}}/>}
                                state={ {lat:deliver_lat, lng: deliver_lng}}
                                parentCallback = {deliverMapCallBack}
                                />
                            }
                        </div>
                    </div>
                    
                    <div className="item-Table">
                        <h4>Items</h4>
                        <ul className="listP-group">
                            <li className="listP-item">
                                <div>
                                    Name
                                </div>
                                <div>
                                    Category
                                </div>
                                <div>
                                    Weight
                                </div>
                                <div>
                                    Actions
                                </div>
                            </li>

                            {  ( (items === null) || (items && items === 0) ) && 
                                <p>0 items on the list yet.</p>
                            }
                            
                            {Object.entries(items).map(([key,value]) => (
                                <li key={key} className="listP-item" id={"id_" + value["name"]}>
                                    <div>
                                        <input type="text" id={"name_" + key}  readOnly placeholder={value["name"]}></input>
                                    </div>

                                    <div>
                                        <input type="text" id={"category_" + key} readOnly placeholder={value["category"]}></input>
                                    </div>
                                    
                                    <div>
                                        <input type="text" id={"weight_" + key} readOnly placeholder={value["weight"]}></input>
                                    </div>
                                    <div className="actions">
                                        
                                        <div id={"icons_ed" + key}>
                                            <MdIcons.MdModeEdit size={20} title="edit" onClick={() => setItemEditable({key: key, name: value["name"], editable: true})} />
                                            <MdIcons.MdDelete size={20} title="delete" onClick={() => removeItem(key)}/>
                                        </div>
                                            
                                        
                                        <div id={"icons_cc" + key} style={{display:"none"}}>
                                            <GiIcons.GiConfirmed size={20} title="confirm" color={"green"} onClick={() => {setItemEditable({key: key, name: value["name"], editable: false}); editItem(key)}}/>
                                            <GiIcons.GiCancel size={20} title="cancel" color={"red"} onClick={() => setItemEditable({key: key, name: value["name"], editable: false})}/>
                                        </div>
                                            
                                        
                                        
                                    </div>
                                </li>
                            )) }
                        </ul>
                        <div id="new-item" className="new-container" style={{display:"none"}}>
                            <h6>New Item Details</h6>
                            <div className="new-item">
                                <input type="text" id="n_name" placeholder="name"></input>
                                <input type="text" id="n_category" placeholder="category"></input>
                                <input type="number" min="0" id="n_weight" placeholder="weight"></input>
                            </div>
                        </div>
                        
                        <div className="button-div" >
                            <button className="button-add" id="button-add" onClick={() => setNewItem(true)}>Add <RiIcons.RiAddFill/></button>

                            <div className="both-buttons" id="both_buttons" style={{display:"none"}}>
                                <button onClick={() => {setNewItem(false); addItem(); }} type="button" className="button-details">Confirm</button>
                                <button onClick={() => setNewItem(false)} className="button-details cancelar">Cancel</button>
                            </div>
                        </div>
                        
                    </div>

                </div>
                <div className="confirm">
                    <div className="image-confirm">
                        
                    </div>
                    <div className="button-confirm">
                        <button onClick={() => submitOrder()}>Confirm</button>
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
                    {<MapLoader 
                    googleMapURL="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco"
                    loadingElement={<div style={{ height: "100%"}}/>}
                    />
                    }
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