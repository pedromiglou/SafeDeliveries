/* css */
import './App.css';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';

/* icons */
import * as FiIcons from 'react-icons/fi';
import * as FaIcons from 'react-icons/fa';
import * as BsIcons from 'react-icons/bs';

/* router */
import {Link, Route, Switch, withRouter, useHistory} from 'react-router-dom';

/* Components */
import Home from './Components/Home/Home';
import Deliveries from './Components/Deliveries/Deliveries';
import History from './Components/History/History';
import Login from './Components/Login/Login';
import Profile from './Components/Profile/Profile';
import Statistics from './Components/Statistics/Statistics';

/* React */
import { useEffect, useState } from 'react';

/* Services */
import AuthService from './Services/auth.service';
import RiderService from './Services/rider.service';
import NotificationService from './Services/notification.service';
import OrderService from './Services/order.service';

import { Modal } from "react-bootstrap";

import Map from './Components/map/Map.js'

import { withScriptjs } from "react-google-maps";
import {urlWeb} from "./data/data";

import Geocode from "react-geocode";

// set Google Maps Geocoding API for purposes of quota management. Its optional but recommended.
Geocode.setApiKey("AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco");

// set response language. Defaults to english.
Geocode.setLanguage("en");



function App() {
    
  var current_user = AuthService.getCurrentUser();

  var [state, setState] = useState(current_user === null ? "Online": current_user.status);

  const [modalNotificationShow, setNotificationModalShow] = useState(false);
                                                                    
  const [notificationInfo, setNotificationInfo] = useState(null);

  const [modalErrorStatusShow, setModalErrorStatusShow] = useState(false);

  const MapLoader = withScriptjs(Map);

  async function acceptOrder(order_id) {
    await OrderService.acceptOrder(order_id, current_user.id);
    current_user.status = "Delivering"
    sessionStorage.setItem("user", JSON.stringify(current_user));
    history.push("/deliveries?id=" + order_id);
    window.location.reload();
  }

  async function declineOrder(order_id) {
    await OrderService.declineOrder(order_id, current_user.id);
    window.location.reload();
  }

	function NotificationModal(props) {
    let pick_up_lat = props.pick_up_lat;
    let pick_up_lng = props.pick_up_lng;
    let deliver_lat = props.deliver_lat;
    let deliver_lng = props.deliver_lng;

    let weight = props.weight;
        return (
          <Modal
            {...props}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            centered
          >
            <Modal.Header>
              <Modal.Title id="contained-modal-title-vcenter" >
                Order request!
              </Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <p>Order information</p>
              <p>Weight: {weight} kg</p>
              {<MapLoader 
                googleMapURL="https://maps.googleapis.com/maps/api/js?key=AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco"
                loadingElement={<div style={{ height: "100%"}}/>}
                pick_up_lat={pick_up_lat}
                pick_up_lng={pick_up_lng}
                deliver_lat={deliver_lat}
                deliver_lng={deliver_lng}
                />
              }
            </Modal.Body>
            <Modal.Footer>
              <button id="accept_order_button" onClick={() => {acceptOrder(props.order_id); props.onHide();}} className="btn">Accept</button>
              <button id="decline_order_button" onClick={() => {declineOrder(props.order_id); props.onHide();}} className="btn">Decline</button>
            </Modal.Footer>
          </Modal>
        );
      }

      function ErrorStatusModal(props) {
        return (
          <Modal
            {...props}
            size="lg"
            aria-labelledby="contained-modal-title-vcenter"
            centered
          >
            <Modal.Header>
              <Modal.Title id="contained-modal-title-vcenter" >
                Cannot change status!
              </Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <p id="error_status_message">You are currently delivering a order.</p>
            </Modal.Body>
            <Modal.Footer>
              <button id="modal_error_ok_button" onClick={props.onHide} className="btn">Ok</button>
            </Modal.Footer>
          </Modal>
        );
      }
    

  useEffect(() => {

    async function getNotification(userid) {
      let notification = await NotificationService.getNotificationByUserId(current_user.id);
      console.log(notification);
      if (notification.length !== 0) {
        setNotificationInfo(notification);
        setNotificationModalShow(true);
      }
    }

    if (current_user !== null) {

      let perfil = document.getElementById("perfil-dropdown");

      if (state === "Online"){
        perfil.classList.add("online");
        perfil.classList.remove("offline");
        perfil.classList.remove("delivering");
      } else if (state === "Offline"){
        perfil.classList.add("offline");
        perfil.classList.remove("online");
        perfil.classList.remove("delivering");
      } else if (state === "Delivering"){
        perfil.classList.add("delivering");
        perfil.classList.remove("offline");
        perfil.classList.remove("online");
      }
      
      if (notificationInfo === null) {
        getNotification(current_user.id);
        const interval = setInterval(() => {
          getNotification(current_user.id);
        }, 8000);

        return () => clearInterval(interval);
      }
    
    } 

  }, [state, current_user, notificationInfo]);

  const history = useHistory();

  function routeChange(path){ 
      let new_url = '/' + path; 
      history.push(new_url);
  }

  async function changeStatus(id, status) {
    var res = await RiderService.changeStatus(id, status);
    if (!res.error) {
      current_user.status = status
      sessionStorage.setItem("user", JSON.stringify(current_user));
      setState(status);
    } else {
      setModalErrorStatusShow(true);
    }
  }

  async function logout(){
    await RiderService.changeStatus(current_user.id, "Offline")
    sessionStorage.removeItem("user");
    window.location.assign(urlWeb);
  }

  return (
    <>
    { notificationInfo !== null &&
      <NotificationModal
        show={modalNotificationShow}
        onHide={() => setNotificationModalShow(false)}
        pick_up_lat={notificationInfo.pick_up_lat}
        pick_up_lng={notificationInfo.pick_up_lng}
        deliver_lat={notificationInfo.deliver_lat}
        deliver_lng={notificationInfo.deliver_lng}
        weight={notificationInfo.weight}
        order_id={notificationInfo.order_id}
      />
    }
    <ErrorStatusModal
      show={modalErrorStatusShow}
      onHide={() => setModalErrorStatusShow(false)}
    />
    <navbar>
        <ul className="nav-list">
          <li className="nav-item">
            <Link to="/" id="logo">
              {current_user !== null && current_user["accountType"] === "Admin" ? 
              <>
              <FiIcons.FiPackage/><span id="admin_text">SafeDeliveries - Admin</span>
              </>
              :
              <>
              <FiIcons.FiPackage/><span>SafeDeliveries - Riders</span>
              </>
              }
            </Link>
            
          </li>
          <li className="nav-item">
            <Link to="/" id="home-tab">
              Home
            </Link>
          </li>

          {current_user !== null && current_user["accountType"] === "Admin" && 
            <li className="nav-item">
              <Link to="/statistics" id="statistics-tab">
                Statistics
              </Link>
            </li>
          }

          {current_user !== null && 
          <>
            <li className="nav-item">
              <Link to="/history" id="history-tab">
                Deliveries History
              </Link>
            </li>
          </>
          }
          
          <li className="nav-item">
            <Link to="/aboutus" id="aboutus-tab">
              About us
            </Link>
          </li>

          {current_user === null && 
            <li className="nav-item">
              <Link to="/login" id="login" >
                Login
              </Link> 
            </li>
          }

          {current_user !== null && 
            <li className="nav-item">
              <DropdownButton title={<FaIcons.FaUserCircle size="60"/>} id="perfil-dropdown" className="navbar-dropdown">
                <Dropdown.ItemText>
                  <h5>Status</h5>
                  <hr></hr>
                  <div className="Status">
                    <div id="state-online" className="Status-item" onClick={() => {changeStatus(current_user.id, "Online");}}>
                      <BsIcons.BsCircleFill className="state-icon online"/>
                      <span>Online</span>
                      
                    </div>
                    <div id="state-delivering" className="Status-item" onClick={() => {changeStatus(current_user.id, "Delivering");}}>
                      <BsIcons.BsCircleFill className="state-icon delivering"/>
                      <span>Delivering</span>
                      
                    </div>
                    <div id="state-off" className="Status-item" onClick={() => {changeStatus(current_user.id, "Offline");}}>
                      <BsIcons.BsCircle className="state-icon off"/>
                      <span>Offline</span>
                      
                    </div>
                  </div>
                </Dropdown.ItemText>
                <Dropdown.Divider/>
                <Dropdown.ItemText>
                  <div onClick={() => routeChange("profile")} className="modal-item" id="profile-div">
                    <h5>Profile</h5>
                  </div>
                  
                </Dropdown.ItemText>
                
                {/* <Dropdown.ItemText>
                  <div onClick={() => routeChange("settings")} className="modal-item">
                    <h5>Settings</h5>
                  </div>
                </Dropdown.ItemText> */}
                <Dropdown.Divider/>
                <Dropdown.ItemText>
                  <div onClick={() => logout()} className="modal-item" id="logout-div">
                    <h5>Logout</h5>
                  </div>
                </Dropdown.ItemText>
              </DropdownButton>
                
            </li>
          }
          
        </ul>
    </navbar>

    <div className="content">
        <Switch>
            <Route exact path='/' component={withRouter(Home)} />
            <Route path='/deliveries' component={withRouter(Deliveries)} />
            <Route exact path='/history' component={withRouter(History)} />
            {/* <Route exact path='/aboutus' component={withRouter(AboutUs)} /> */}
            <Route exact path='/login' component={withRouter(Login)} />
            <Route exact path='/profile' component={withRouter(Profile)} />
            <Route exact path='/statistics' component={withRouter(Statistics)} />
        </Switch>
    </div>

    <footer className="footer">
        <h6>2021 - Rights reserved to SafeDeliveries</h6>
    </footer>
    
      
    </>
    
  );
}

export default App;
