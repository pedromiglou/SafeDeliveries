/* css */
import './Profile.css';

import * as FaIcons from 'react-icons/fa';
import * as MdIcons from 'react-icons/md';
// import * as BiIcons from 'react-icons/bi';
import * as RiIcons from 'react-icons/ri';
import * as GiIcons from 'react-icons/gi';
import * as BsIcons from 'react-icons/bs';


import { useEffect, useState } from 'react';

/* Services */
import AuthService from '../../Services/auth.service';
import RiderService from '../../Services/rider.service';
import VehicleService from '../../Services/vehicle.service';

function Profile() {
    const [carEditable, setCarEditable] = useState({key: -2, car: "", editable: false})
    const [newVehicle, setNewVehicle] = useState(false);
    const [updated, setUpdated] = useState(false);

    var current_user = AuthService.getCurrentUser();

    if (current_user === null) {
        window.location.assign("/")
    } else {
        if (!updated)
            updateSessionStorageData()
    }
    
                          
    async function updateSessionStorageData() {
        var response = await RiderService.getRiderById(current_user.id)
        var vehicles = await VehicleService.getVehiclesByRiderId(current_user.id)
        current_user.firstname = response.firstname;
        current_user.lastname = response.lastname;
        current_user.email = response.email;
        current_user.rating = response.rating;
        current_user.status = response.status;
        current_user.vehicles = vehicles
        sessionStorage.setItem("user", JSON.stringify(current_user));
        current_user = AuthService.getCurrentUser();
        setUpdated(true);
    }
    

    function make_fields_editable() {
        let email_inp = document.getElementById("email");
        let fname_inp = document.getElementById("fname");
        let lname_inp = document.getElementById("lname");
        let both_buttons = document.getElementById("buttons-details");

        email_inp.readOnly = false;
        fname_inp.readOnly = false;
        lname_inp.readOnly = false;
        both_buttons.style.display = "flex";

        email_inp.style.borderBottom = "1px solid green"
        fname_inp.style.borderBottom = "1px solid green"
        lname_inp.style.borderBottom = "1px solid green"
    }

    function make_fields_uneditable() {
        let email_inp = document.getElementById("email");
        let fname_inp = document.getElementById("fname");
        let lname_inp = document.getElementById("lname");
        let both_buttons = document.getElementById("buttons-details");

        email_inp.readOnly = true;
        fname_inp.readOnly = true;
        lname_inp.readOnly = true;
        both_buttons.style.display = "none";

        email_inp.style.borderBottom = "1px solid lightcoral"
        fname_inp.style.borderBottom = "1px solid lightcoral"
        lname_inp.style.borderBottom = "1px solid lightcoral"
    }

    useEffect(() => {
    
        if (carEditable["key"] === -2){
            return
        }

        let reg_inp = document.getElementById("registration_" + carEditable["key"]);
        let model_inp = document.getElementById("model_" + carEditable["key"]);
        let brand_inp = document.getElementById("brand_" + carEditable["key"]);
        let category_inp = document.getElementById("category_" + carEditable["key"]);
        let capacity_inp = document.getElementById("capacity_" + carEditable["key"]);

        let icons_ed = document.getElementById("icons_ed" + carEditable["key"]);
        let icons_cc = document.getElementById("icons_cc" + carEditable["key"]);

        if(carEditable["editable"]){
            reg_inp.readOnly = false;
            model_inp.readOnly = false;
            brand_inp.readOnly = false;
            category_inp.readOnly = false;
            capacity_inp.readOnly = false;

            reg_inp.classList.add("editable");
            model_inp.classList.add("editable");
            brand_inp.classList.add("editable");
            category_inp.classList.add("editable");
            capacity_inp.classList.add("editable");

            icons_ed.style.display = "none";
            icons_cc.style.display = "flex";
        } else {
            reg_inp.readOnly = true;
            model_inp.readOnly = true;
            brand_inp.readOnly = true;
            category_inp.readOnly = true;
            capacity_inp.readOnly = true;

            reg_inp.classList.remove("editable");
            model_inp.classList.remove("editable");
            brand_inp.classList.remove("editable");
            category_inp.classList.remove("editable");
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

    async function submitFunction(){
        var email = document.getElementById("email").value;
        var firstname = document.getElementById("fname").value;
        var lastname = document.getElementById("lname").value;
        await RiderService.changeRider(current_user.id, firstname, lastname, email)
        window.location.reload()

    }


    async function addVehicle(){
        var registration = document.getElementById("n_registration").value;
        var brand = document.getElementById("n_brand").value;
        var model = document.getElementById("n_model").value;
        var category = document.getElementById("n_category").value;
        var capacity = document.getElementById("n_capacity").value;
        await VehicleService.createVehicle(registration, brand, model, category, capacity, current_user.id)
        window.location.reload()
    }

    async function editVehicle(id, key){
        var registration = document.getElementById("registration_"+key).value;
        var brand = document.getElementById("brand_"+key).value;
        var model = document.getElementById("model_"+key).value;
        var category = document.getElementById("category_"+key).value;
        var capacity = document.getElementById("capacity_"+key).value;
        if (registration === "") registration = document.getElementById("registration_"+key).placeholder;
        if (brand === "") brand = document.getElementById("brand_"+key).placeholder;
        if (model === "") model = document.getElementById("model_"+key).placeholder;
        if (category === "") category = document.getElementById("category_"+key).placeholder;
        if (capacity === "") capacity = current_user.vehicles[key]["capacity"];
        await VehicleService.editVehicle(id, registration, brand, model, category, capacity)
        window.location.reload()
    }

    async function removeVehicle(id) {
        await VehicleService.removeVehicle(id)
        window.location.reload()
    }

    return (
      <>
      <div className="profile-container">
        <div className="profile-section">
            <div className="image-area">
                
                <div className="profile-picture">
                    <FaIcons.FaUserCircle className="user-img"/>
                    <div className="below-pic"> 
                        <div className="rating-section">
                            <BsIcons.BsStarFill className="user-rating"/>
                            <span>{current_user.rating}/5.0</span>
                        </div>
                        
                        {/* <BiIcons.BiImageAdd className="user-change-pic"/> */}
                    </div>
                </div> 
                <h3>{current_user.firstname} {current_user.lastname}</h3>
            </div>
            <div className="form-area">
                <div className="details-edit">
                    <h2>Profile Details</h2>
                    <MdIcons.MdModeEdit size={35} id="edit-icon" className="edit-icon" title="edit" onClick={() => make_fields_editable()}/>
                </div>
                
                <form>
                    <div className="form-details">
                        <div className="email-div">
                            <label for="email">Email</label>
                            <input type="email" readOnly id="email" placeholder={current_user.email}></input>
                        </div>
                        <div className="fname-div">
                            <label for="name">FirstName</label>
                            <input type="text" readOnly id="fname" placeholder={current_user.firstname}></input>
                        </div>
                        <div className="lname-div">
                            <label for="name">LastName</label>
                            <input type="text" readOnly id="lname" placeholder={current_user.lastname}></input>
                        </div>
                    </div>
                    <div className="button-div">
                        <div id="buttons-details" style={{display:"none"}} className="both-buttons">
                            <button id="confirm-detail" onClick={() => {make_fields_uneditable(); submitFunction();}} type="button" className="button-details">Confirm</button>
                            <button id="cancel-detail" onClick={() => make_fields_uneditable()} type="button" className="button-details cancelar">Cancel</button>
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
                                Brand
                            </div>

                            <div>
                                Model
                            </div>
                            
                            <div>
                                Category
                            </div>

                            <div>
                                Capacity(kg)
                            </div>

                            <div>
                                Actions
                            </div>
                        </li>

                        {  ( (current_user.vehicles === null) || (current_user.vehicles && current_user.vehicles.length === 0) ) && 
                            <p>Do not own any vehicle</p>
                        }
                        
                        { current_user.vehicles && current_user.vehicles.length > 0 && Object.entries(current_user.vehicles).map(([key,value]) => (
                            <li key={value["registration"]} className="listP-item" id={"id_" + value["registration"]}>
                                <div>
                                    <input type="text" id={"registration_" + key} name={"registration_" + value["registration"]}  readOnly placeholder={value["registration"]}></input>
                                </div>

                                <div>
                                    <input type="text" id={"brand_" + key} name={"brand_" + value["registration"]} readOnly placeholder={value["brand"]}></input>
                                </div>
                                
                                <div>
                                    <input type="text" id={"model_" + key} name={"model_" + value["registration"]} readOnly placeholder={value["model"]}></input>
                                </div>

                                <div>
                                    <input type="text" id={"category_" + key} name={"category_" + value["registration"]} readOnly placeholder={value["category"]}></input>
                                </div>

                                <div>
                                    <input type="text" id={"capacity_" + key} name={"capacity_" + value["registration"]} readOnly placeholder={value["capacity"]}></input>
                                </div>

                                <div className="actions">
                                    
                                    <div id={"icons_ed" + key}>
                                        <MdIcons.MdModeEdit size={20} title="edit" id={"edit_" + value["registration"]} onClick={() => setCarEditable({key: key, car: value["registration"], editable: true})} />
                                        <MdIcons.MdDelete size={20} title="delete" id={"del_" + value["registration"]} onClick={() => removeVehicle(value["id"])}/>
                                    </div>
                                        
                                    
                                    <div id={"icons_cc" + key} style={{display:"none"}}>
                                        <GiIcons.GiConfirmed size={20} title="confirm" id={"edit_c" + value["registration"]} color={"green"} onClick={() => {setCarEditable({key: key, car: value["registration"], editable: false}); editVehicle(value["id"], key)}}/>
                                        <GiIcons.GiCancel size={20} title="cancel" id={"del_c" + value["registration"]} color={"red"} onClick={() => setCarEditable({key: key, car: value["registration"], editable: false})}/>
                                    </div>
                                        
                                    
                                    
                                </div>
                            </li>
                        )) }
                    </ul>
                    <div id="new-vehicle" className="new-container" style={{display:"none"}}>
                        <h6>New Vehicle Details</h6>
                        <div className="new-vehicle">
                            <input type="text" id="n_registration" placeholder="registration"></input>
                            <input type="text" id="n_brand" placeholder="brand"></input>
                            <input type="text" id="n_model" placeholder="model"></input>
                            <input type="text" id="n_category" placeholder="category"></input>
                            <input type="text" id="n_capacity" placeholder="capacity..kg"></input>
                        </div>
                    </div>
                    
                    <div className="button-div" >
                        <button className="button-add" id="button-add" onClick={() => setNewVehicle(true)}>Add <RiIcons.RiAddFill/></button>

                        <div className="both-buttons" id="both_buttons" style={{display:"none"}}>
                            <button onClick={() => {setNewVehicle(false); addVehicle(); }} type="button" className="button-details" id="button-nconfirm">Confirm</button>
                            <button onClick={() => setNewVehicle(false)} className="button-details cancelar" id="button-ncancel">Cancel</button>
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