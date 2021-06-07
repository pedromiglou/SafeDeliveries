/* css */
import './Profile.css';

import * as FaIcons from 'react-icons/fa';
import * as MdIcons from 'react-icons/md';
import * as BiIcons from 'react-icons/bi';
import * as RiIcons from 'react-icons/ri';
import * as GiIcons from 'react-icons/gi';


import { useEffect, useState } from 'react';


function Profile() {
    const [editable, setEditable] = useState(false);
    const [carEditable, setCarEditable] = useState({key: -2, car: "", editable: false})
    const [newVehicle, setNewVehicle] = useState(false);

    const user_vehicles = [{registration:"GC-27-39", maker:"BMW", model:"M4", type:"car", capacity:"20"},
                           {registration:"XC-22-01", maker:"Toyota", model:"Supra", type:"car", capacity:"10"},
                           {registration:"25-LM-39", maker:"Honda", model:"PCX", type:"motorcycle", capacity:"5"},
                           {registration:"25-LM-39", maker:"Honda", model:"PCX", type:"motorcycle", capacity:"5"},
                           {registration:"25-LM-39", maker:"Honda", model:"PCX", type:"motorcycle", capacity:"5"},
                           {registration:"25-LM-39", maker:"Honda", model:"PCX", type:"motorcycle", capacity:"5"},
                           {registration:"25-LM-39", maker:"Honda", model:"PCX", type:"motorcycle", capacity:"5"},
                           {registration:"25-LM-39", maker:"Honda", model:"PCX", type:"motorcycle", capacity:"5"}
                          ]
    
    useEffect(() => {
    
        let email_inp = document.getElementById("email");
        let fname_inp = document.getElementById("fname");
        let lname_inp = document.getElementById("lname");

        if(editable){
            email_inp.readOnly = false;
            fname_inp.readOnly = false;
            lname_inp.readOnly = false;

            email_inp.style.borderBottom = "1px solid green"
            fname_inp.style.borderBottom = "1px solid green"
            lname_inp.style.borderBottom = "1px solid green"
        } else {
            email_inp.style.borderBottom = "1px solid lightcoral"
            fname_inp.style.borderBottom = "1px solid lightcoral"
            lname_inp.style.borderBottom = "1px solid lightcoral"
        }
    }, [editable]);

    useEffect(() => {
    
        if (carEditable["key"] === -2){
            return
        }

        let reg_inp = document.getElementById("registration_" + carEditable["key"]);
        let model_inp = document.getElementById("model_" + carEditable["key"]);
        let maker_inp = document.getElementById("maker_" + carEditable["key"]);
        let type_inp = document.getElementById("type_" + carEditable["key"]);
        let capacity_inp = document.getElementById("capacity_" + carEditable["key"]);

        let icons_ed = document.getElementById("icons_ed" + carEditable["key"]);
        let icons_cc = document.getElementById("icons_cc" + carEditable["key"]);

        if(carEditable["editable"]){
            reg_inp.readOnly = false;
            model_inp.readOnly = false;
            maker_inp.readOnly = false;
            type_inp.readOnly = false;
            capacity_inp.readOnly = false;

            reg_inp.classList.add("editable");
            model_inp.classList.add("editable");
            maker_inp.classList.add("editable");
            type_inp.classList.add("editable");
            capacity_inp.classList.add("editable");

            icons_ed.style.display = "none";
            icons_cc.style.display = "flex";
        } else {
            reg_inp.readOnly = true;
            model_inp.readOnly = true;
            maker_inp.readOnly = true;
            type_inp.readOnly = true;
            capacity_inp.readOnly = true;

            reg_inp.classList.remove("editable");
            model_inp.classList.remove("editable");
            maker_inp.classList.remove("editable");
            type_inp.classList.remove("editable");
            capacity_inp.classList.remove("editable");

            icons_ed.style.display = "flex";
            icons_cc.style.display = "none";
        }
    }, [carEditable]);

    useEffect(() => {
    
        let both_buttons = document.getElementById("both_buttons");
        let add_button = document.getElementById("button-add");
        let new_vehicle_form = document.getElementById("new-vehicle");

        if(newVehicle){
            both_buttons.style.display = "flex";
            add_button.style.display = "none";
            new_vehicle_form.style.display = "block";
        } else {
            both_buttons.style.display = "none";
            add_button.style.display = "block";
            new_vehicle_form.style.display = "none";
        }
    }, [newVehicle]);

    function submitFunction(){

    }

    return (
      <>
      <div className="profile-container">
        <div className="profile-section">
            <div className="image-area">
                <FaIcons.FaUserCircle className="profile-picture"/>
                <BiIcons.BiImageAdd/>
                <h3>Nome Utilizador</h3>
            </div>
            <div className="form-area">
                <div className="details-edit">
                    <h2>Profile Details</h2>
                    <MdIcons.MdModeEdit size={35} className="edit-icon" title="edit" onClick={() => setEditable(true)}/>
                </div>
                
                <form onSubmit={() => submitFunction()}>
                    <div className="form-details">
                        <div className="email-div">
                            <label for="email">Email</label>
                            <input type="email" readOnly id="email"></input>
                        </div>
                        <div className="fname-div">
                            <label for="name">First name</label>
                            <input type="text" readOnly id="fname"></input>
                        </div>
                        <div className="lname-div">
                            <label for="name">Last name</label>
                            <input type="text" readOnly id="lname"></input>
                        </div>
                    </div>
                    <div className="button-div">
                        <div className="both-buttons">
                            <button onClick={() => setEditable(false)} type="button" className="button-details">Confirm</button>
                            <button onClick={() => setEditable(false)} className="button-details cancelar">Cancel</button>
                        </div>
                    </div>
                    
                </form>
                <div className="profile-Table">
                    <h4>My Vehicles</h4>
                    <ul className="listP-group">
                        <li className="listP-item">
                            <div>
                                Registration
                            </div>
                            <div>
                                Maker
                            </div>

                            <div>
                                Model
                            </div>
                            
                            <div>
                                Type
                            </div>

                            <div>
                                Capacity
                            </div>

                            <div>
                                Actions
                            </div>
                        </li>

                        {Object.entries(user_vehicles).map(([key,value]) => (
                            <li className="listP-item" id={"id_" + value["registration"]}>
                                <div>
                                    <input type="text" id={"registration_" + key}  readOnly placeholder={value["registration"]}></input>
                                </div>

                                <div>
                                    <input type="text" id={"maker_" + key} readOnly placeholder={value["maker"]}></input>
                                </div>
                                
                                <div>
                                    <input type="text" id={"model_" + key} readOnly placeholder={value["model"]}></input>
                                </div>

                                <div>
                                    <input type="text" id={"type_" + key} readOnly placeholder={value["type"]}></input>
                                </div>

                                <div>
                                    <input type="text" id={"capacity_" + key} readOnly placeholder={value["capacity"] + "kg"}></input>
                                </div>

                                <div className="actions">
                                    
                                    <div id={"icons_ed" + key}>
                                        <MdIcons.MdModeEdit size={20} title="edit" onClick={() => setCarEditable({key: key, car: value["registration"], editable: true})} />
                                        <MdIcons.MdDelete size={20} title="delete"/>
                                    </div>
                                        
                                    
                                    <div id={"icons_cc" + key} style={{display:"none"}}>
                                        <GiIcons.GiConfirmed size={20} title="confirm" color={"green"} onClick={() => setCarEditable({key: key, car: value["registration"], editable: false})}/>
                                        <GiIcons.GiCancel size={20} title="cancel" color={"red"} onClick={() => setCarEditable({key: key, car: value["registration"], editable: false})}/>
                                    </div>
                                        
                                    
                                    
                                </div>
                            </li>
                        )) }
                    </ul>
                    <div id="new-vehicle" className="new-container" style={{display:"none"}}>
                        <h6>New Vehicle Details</h6>
                        <div className="new-vehicle">
                            <input type="text" id="n_registration" placeholder="registration"></input>
                            <input type="text" id="n_maker" placeholder="maker"></input>
                            <input type="text" id="n_model" placeholder="model"></input>
                            <input type="text" id="n_type" placeholder="type"></input>
                            <input type="text" id="n_capacity" placeholder="capacity..kg"></input>
                        </div>
                    </div>
                    
                    <div className="button-div" >
                        <button className="button-add" id="button-add" onClick={() => setNewVehicle(true)}>Add <RiIcons.RiAddFill/></button>

                        <div className="both-buttons" id="both_buttons" style={{display:"none"}}>
                            <button onClick={() => setNewVehicle(false)} type="button" className="button-details">Confirm</button>
                            <button onClick={() => setNewVehicle(false)} className="button-details cancelar">Cancel</button>
                        </div>
                    </div>
                    
                </div>
            </div>
        </div>
      </div>
        
      </>
      
    );
  }
  
export default Profile;