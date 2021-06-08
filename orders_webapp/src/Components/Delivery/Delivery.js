/* css */
import './Delivery.css';

/* react */
import { useEffect, useState } from 'react';
import { useLocation } from 'react-router';

import * as FaIcons from 'react-icons/fa';
import * as MdIcons from 'react-icons/md';
// import * as BiIcons from 'react-icons/bi';
import * as RiIcons from 'react-icons/ri';
import * as GiIcons from 'react-icons/gi';
import * as BsIcons from 'react-icons/bs';


function Delivery() {
    const [state, setState] = useState("Requesting");
    const [items, setItems] = useState([{name:"book", category:"Education", weight: "1"},
                                        {name:"book", category:"Education", weight: "1"}
                                    ]);
    const [itemEditable, setItemEditable] = useState({key: -2, name: "", editable: false})
    const [newItem, setNewItem] = useState(false);

    const location = useLocation();

    useEffect(() => {
        console.log(location.state);
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


    async function submitFunction(){
        var pickup = document.getElementById("pickup").value;
        var destiny = document.getElementById("destiny").value;
        // await RiderService.changeRider(current_user.id, firstname, lastname, email)
        // window.location.reload()
    }


    async function removeItem(item_id){

    }

    async function editItem(item_id){

    }

    async function addItem(){

    }


    return (
      <>
        {state === "Requesting" && 
            <div className="DeliveriesSection req">
                <div className="DeliveryDetails">
                    <h1>Delivery Details</h1>
                    <div className="div-addresses">
                        <div>
                            <label for="pickup">Pick Up Address</label>
                            <input id="pickup" type="text" placeholder="Ex:Rua dos Clérigos"/>
                        </div>
                        <div>
                            <label for="destiny">Destiny Address</label>
                            <input id="destiny" type="text" placeholder="Ex:Rua Manuel Boa Ventura"/>
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
                                <p>Do not own any vehicle</p>
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