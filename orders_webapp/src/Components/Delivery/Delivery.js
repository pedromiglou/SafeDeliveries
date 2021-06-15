/* css */
import './Delivery.css';

/* react */
import { useEffect, useState, useCallback } from 'react';
import { useLocation } from 'react-router';

import * as MdIcons from 'react-icons/md';
// import * as BiIcons from 'react-icons/bi';
import * as RiIcons from 'react-icons/ri';
import * as GiIcons from 'react-icons/gi';

/* Geocode */
import Geocode from "react-geocode";

import Map from '../map/Map.js'
import { withScriptjs } from "react-google-maps";

import OrdersService from '../../Services/orders.service';
import AuthService from '../../Services/auth.service';

function Delivery() {
    Geocode.setApiKey("AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco");
    Geocode.setLanguage("en");

    const [state, setState] = useState("Requesting");

    const [items, setItems] = useState([]);

    
    
    const [itemEditable, setItemEditable] = useState({key: -2, name: "", editable: false})



    const [newItem, setNewItem] = useState(false);



    const [errorOrder, setErrorOrder] = useState(false);
    const [sucessOrder, setSucessOrder] = useState(false);

    const [pick_up_lat, setPickUpLat] = useState(40.756795);    
    const [pick_up_lng, setPickUpLng] = useState(-73.954298);
    const [deliver_lat, setDeliverLat] = useState(41.5322699);
    const [deliver_lng, setDeliverLng] = useState(-8.737535200000002);

    const current_user = AuthService.getCurrentUser();

    // const pickUpMapCallBack = useCallback((msg) => { 
    //     setPickUpLat(msg.lat)
    //     setPickUpLng(msg.lng)
    // }, []);

    // const deliverMapCallBack = useCallback((msg) => { 
    //     setDeliverLat(msg.lat)
    //     setDeliverLng(msg.lng)
    // }, []);

    const addressChangeMapCallBack = useCallback(async (msg) => { 
        if (msg.marker_id === "pick_up_position"){
            let address_components = await getAddressCoord(msg.lat, msg.lng);
            
            let address = address_components[0];
            let zip = address_components[1]
            let city = address_components[2]
            let country = address_components[3];

            //PickUp
            let paddress = document.getElementById("paddress");
            let pzip = document.getElementById("pzip");
            let pcity = document.getElementById("pcity");
            let pcountry = document.getElementById("pcountry");

            console.log(paddress);

            paddress.value = address;
            pzip.value = zip;
            pcity.value = city;
            pcountry.value = country;

            setPickUpLat(msg.lat)
            setPickUpLng(msg.lng)
        } else if (msg.marker_id === "delivery_position"){
            let address_components = await getAddressCoord(msg.lat, msg.lng);
            
            let address = address_components[0];
            let zip = address_components[1]
            let city = address_components[2]
            let country = address_components[3];

            //Delivery
            let daddress = document.getElementById("daddress");
            let dzip = document.getElementById("dzip");
            let dcity = document.getElementById("dcity");
            let dcountry = document.getElementById("dcountry");

            daddress.value = address;
            dzip.value = zip;
            dcity.value = city;
            dcountry.value = country;

            setDeliverLat(msg.lat)
            setDeliverLng(msg.lng)
        }
    }, []);

    async function getAddressCoord(lat,long){
        const response = await Geocode.fromLatLng(lat, long);

        
        let address_components = response.results[0].formatted_address.split(",");
        
        let address = address_components[0];
        let zip = address_components[1].split(" ")[1]
        let city = address_components[1].split(" ")[2]
        let country = address_components[2];

        return [address, zip, city, country];

        // Geocode.fromLatLng("48.8583701", "2.2922926").then(
            
        // (response) => {
        //     const address = response.results[0].formatted_address;
        //     console.log(address);
        //     return address;
        // },
        // (error) => {
        // console.error(error);
        // }
        // );
    }

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
        changeEditable(itemEditable["editable"], itemEditable["key"]);
    }, [itemEditable]);
    
    function changeEditable(editable, key){
        let name_inp = document.getElementById("name_" + key);
        let category_inp = document.getElementById("category_" + key);
        let weight_inp = document.getElementById("weight_" + key);

        let icons_ed = document.getElementById("icons_ed" + key);
        let icons_cc = document.getElementById("icons_cc" + key);

        if(editable){
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
            name_inp.value = "";
            category_inp.value = "";
            weight_inp.value = "";

            name_inp.classList.remove("editable");
            category_inp.classList.remove("editable");
            weight_inp.classList.remove("editable");

            icons_ed.style.display = "flex";
            icons_cc.style.display = "none";
        }
    }

    useEffect(() => {
        doNewItem(newItem);
    }, [newItem]);

    function doNewItem(newItem){
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
    }

    async function submitOrder(){
        var res = await OrdersService.create(pick_up_lat, pick_up_lng, deliver_lat, deliver_lng, items, parseInt(current_user.id))
        if (res.error) {
            setErrorOrder(res.message);
            setSucessOrder(false);
        } else {
            setErrorOrder(false);
            setSucessOrder(true);
        }
    }


    function removeItem(item_id) {
        items.splice(item_id, 1);

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

        if (newItem)
            setNewItem(false)
        else
            setNewItem(true)
    }

    function checkMap(){
        //PickUp
        let paddress = document.getElementById("paddress").value;
        let pzip = document.getElementById("pzip").value;
        let pcity = document.getElementById("pcity").value;
        let pcountry = document.getElementById("pcountry").value;

        let pick_up_address = paddress + ", " + pzip + " " + pcity + ", " + pcountry;

        // Get latitude & longitude from address.
        Geocode.fromAddress(pick_up_address).then(
        (response) => {
        const { lat, lng } = response.results[0].geometry.location;
            setPickUpLat(lat)
            setPickUpLng(lng)
        },
        (error) => {
            console.error(error);
        });

        //Delivery
        let daddress = document.getElementById("daddress").value;
        let dzip = document.getElementById("dzip").value;
        let dcity = document.getElementById("dcity").value;
        let dcountry = document.getElementById("dcountry").value;

        let delivery_address = daddress + ", " + dzip + " " + dcity + ", " + dcountry;

        // Get latitude & longitude from address.
        Geocode.fromAddress(delivery_address).then(
        (response) => {
        const { lat, lng } = response.results[0].geometry.location;
            setDeliverLat(lat)
            setDeliverLng(lng)
        },
        (error) => {
            console.error(error);
        });



    }

    return (
      <>

        {sucessOrder === true 
          ? <div className="alert alert-success" role="alert" style={{margin:"10px auto", width: "90%", textAlign:"center", fontSize:"22px"}}>
          Order was created successfully!
          </div> : null}

        {errorOrder !== false 
          ? <div className="alert alert-alert" role="alert" style={{margin:"10px auto", width: "90%", textAlign:"center", fontSize:"22px"}}>
          {errorOrder}
          </div> : null}

        {state === "Requesting" && 
            <div className="DeliveriesSection req">
                
                <div className="DeliveryDetails">
                    <h1>Delivery Details</h1>
                    <hr style={{height:"2px", width:"100%"}}></hr>
                    <div className="div-addresses">
                        <div className="addresses-inp">
                            <div>
                                <h5>Pick Up Address</h5>
                                <label htmlFor="paddress">Address</label>
                                <input id="paddress" type="text" placeholder="Ex: Rua dos Clérigos nº30"/>
                                <label htmlFor="pcountry">Country</label>
                                <input id="pcountry" type="text" placeholder="Ex: Portugal"/>
                                <label htmlFor="pcity">City</label>
                                <input id="pcity" type="text" placeholder="Ex: Esposende"/>
                                <label htmlFor="pzip">Postal Code</label>
                                <input id="pzip" type="text" placeholder="Ex: 4740-120"/>
                            </div>
                            <div>
                                <h5>Delivery Address</h5>
                                <label htmlFor="daddress">Address</label>
                                <input id="daddress" type="text" placeholder="Ex: Rua dos Clérigos nº30"/>
                                <label htmlFor="dcountry">Country</label>
                                <input id="dcountry" type="text" placeholder="Ex: Portugal"/>
                                <label htmlFor="dcity">City</label>
                                <input id="dcity" type="text" placeholder="Ex: Esposende"/>
                                <label htmlFor="dzip">Postal Code</label>
                                <input id="dzip" type="text" placeholder="Ex: 4740-120"/>
                            </div>
                            <div className="button-check-map">
                                <button onClick={() => checkMap()} className="confirm-order">Check In Map</button> 
                            </div>
                        </div>

                        
                        {<MapLoader 
                            googleMapURL="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco"
                            loadingElement={<div style={{ height: "100%"}}/>}
                            state={ {pick_up_lat:pick_up_lat, pick_up_lng: pick_up_lng, del_lat:deliver_lat, del_lng: deliver_lng}}
                            parentCallback = {addressChangeMapCallBack}
                        />
                        }
                        {/* <div>
                            <label htmlFor="destiny" style={{marginBottom: '10px'}}>Choose Destiny Address</label>
                            {<MapLoader 
                                googleMapURL="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco"
                                loadingElement={<div style={{ height: "100%"}}/>}
                                state={ {lat:deliver_lat, lng: deliver_lng}}
                                parentCallback = {deliverMapCallBack}
                                />
                            }
                        </div> */}
                    </div>
                    
                    <hr style={{height:"2px", width:"100%"}}></hr>
                    <div className="item-Table">
                        <h4>Items</h4>
                        <ul className="listP-group">key
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

                            {  ( (items.length === 0) ) && 
                                <p style={{margin: "2em auto", }}>0 items on the list yet.</p>
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
                                            {/* <MdIcons.MdModeEdit size={20} title="edit" onClick={() => setItemEditable({key: key, name: value["name"], editable: true})} /> */}
                                            <MdIcons.MdModeEdit size={20} title="edit" onClick={() => changeEditable(true, key)}/>
                                            <MdIcons.MdDelete size={20} title="delete" onClick={() => removeItem(key)}/>
                                        </div>
                                            
                                        
                                        <div id={"icons_cc" + key} style={{display:"none"}}>
                                            <GiIcons.GiConfirmed size={20} title="confirm" color={"green"} onClick={() => {setItemEditable({key: key, name: value["name"], editable: false}); editItem(key)}}/>
                                            {/* <GiIcons.GiCancel size={20} title="cancel" color={"red"} onClick={() => setItemEditable({key: key, name: value["name"], editable: false})}/> */}
                                            {/* <GiIcons.GiConfirmed size={20} title="confirm" color={"green"} onClick={() => {changeEditable(false, key) ; editItem(key)}}/> */}
                                            <GiIcons.GiCancel size={20} title="cancel" color={"red"} onClick={() => changeEditable(false, key)}/>
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
                            {/* <button className="button-add" id="button-add" onClick={() => setNewItem(true)}>Add <RiIcons.RiAddFill/></button> */}
                            <button className="button-add" id="button-add" onClick={() => doNewItem(true)}>Add <RiIcons.RiAddFill/></button>

                            <div className="both-buttons" id="both_buttons" style={{display:"none"}}>
                                <button onClick={() => {addItem(); }} type="button" className="button-details">Confirm</button>
                                <button onClick={() => doNewItem(false)} className="button-details cancelar">Cancel</button>
                            </div>
                        </div>
                        
                    </div>

                </div>
                
                <div className="button-confirm">
                    <button onClick={() => submitOrder()} className="confirm-order">Place Order</button> 
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