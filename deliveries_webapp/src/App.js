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

/* React */
import { useEffect, useState } from 'react';

/* Services */
import AuthService from './Services/auth.service';
import RiderService from './Services/rider.service';

function App() {
    
  var current_user = AuthService.getCurrentUser();

  var [state, setState] = useState(current_user === null ? "Online": current_user.status);

  useEffect(() => {

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
    } 
  }, [state, current_user]);

  const history = useHistory();

  function routeChange(path){ 
      let new_url = '/' + path; 
      history.push(new_url);
  }

  async function changeStatus(id, status) {
    await RiderService.changeStatus(id, status);
    current_user.status = status
    sessionStorage.setItem("user", JSON.stringify(current_user));
  }

  async function logout(){
    await RiderService.changeStatus(current_user.id, "Offline")
    sessionStorage.removeItem("user");
    window.location.assign("http://localhost:3000/");
  }

  return (
    <>
    
    <navbar>
        <ul className="nav-list">
          <li className="nav-item">
            <Link to="/" id="logo">
              <FiIcons.FiPackage/><span>SafeDeliveries</span>
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/" id="home-tab">
              Home
            </Link>
          </li>

          {current_user !== null && 
          <>
            <li className="nav-item">
              <Link to="/deliveries" id="search-tab">
                Search Delivery
              </Link>
            </li>
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
              {/*<button id="login" onClick={() => setUser({user_type: "logged", username: ""})}>Login</button>*/}
            </li>
          }

          {current_user !== null && 
            <li className="nav-item">
              <DropdownButton title={<FaIcons.FaUserCircle size="60"/>} id="perfil-dropdown" className="navbar-dropdown">
                <Dropdown.ItemText>
                  <h5>Status</h5>
                  <hr></hr>
                  <div className="Status">
                    <div id="state-online" className="Status-item" onClick={() => {changeStatus(current_user.id, "Online"); setState("Online")}}>
                      <BsIcons.BsCircleFill className="state-icon online"/>
                      <span>Online</span>
                      
                    </div>
                    <div id="state-delivering" className="Status-item" onClick={() => {changeStatus(current_user.id, "Delivering"); setState("Delivering")}}>
                      <BsIcons.BsCircleFill className="state-icon delivering"/>
                      <span>Delivering</span>
                      
                    </div>
                    <div id="state-off" className="Status-item" onClick={() => {changeStatus(current_user.id, "Offline"); setState("Offline")}}>
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
            <Route exact path='/deliveries' component={withRouter(Deliveries)} />
            <Route exact path='/history' component={withRouter(History)} />
            {/* <Route exact path='/aboutus' component={withRouter(AboutUs)} /> */}
            <Route exact path='/login' component={withRouter(Login)} />
            <Route exact path='/profile' component={withRouter(Profile)} />
        </Switch>
    </div>

    <footer className="footer">
        <h6>2021 - Rights reserved to SafeDeliveries</h6>
    </footer>
    
      
    </>
    
  );
}

export default App;
