/* css */
import './App.css';

/* icons */
import * as FiIcons from 'react-icons/fi';

/* router */
import {Link, Route, Switch, withRouter, useHistory} from 'react-router-dom';

/* Components */
import Home from './Components/Home/Home';
import Deliveries from './Components/Deliveries/Deliveries';
import History from './Components/History/History';
import Login from './Components/Login/Login';

import { useEffect, useState } from 'react';

import * as FaIcons from 'react-icons/fa';
import * as BsIcons from 'react-icons/bs';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';

function App() {
  const [user, setUser] = useState({user_type: "logged", username: ""});
  const [state, setState] = useState("online");

  useEffect(() => {
    if (user.user_type === "not_logged"){
      return
    }
    let perfil = document.getElementById("perfil-dropdown");

    if (state === "online"){
      perfil.classList.add("online");
      perfil.classList.remove("offline");
      perfil.classList.remove("delivering");
    } else if (state === "offline"){
      perfil.classList.add("offline");
      perfil.classList.remove("online");
      perfil.classList.remove("delivering");
    } else if (state === "delivering"){
      perfil.classList.add("delivering");
      perfil.classList.remove("offline");
      perfil.classList.remove("online");
    }
  }, [state, user.user_type]);

  const history = useHistory();

  function routeChange(path){ 
      let new_url = '/' + path; 
      history.push(new_url);
  }

  function logout(){
    setUser({user_type: "not_logged", username: ""});
  }

  return (
    <>
    
    <navbar>
        <ul className="nav-list">
          <li className="nav-item">
            <Link to="/">
              <FiIcons.FiPackage/><span>SafeDeliveries</span>
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/">
              Home
            </Link>
          </li>

          {user.user_type === "logged" && 
          <>
            <li className="nav-item">
              <Link to="/deliveries">
                My Deliveries
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/history">
                Deliveries History
              </Link>
            </li>
          </>
          }
          
          <li className="nav-item">
            <Link to="/aboutus">
              About us
            </Link>
          </li>

          {user.user_type === "not_logged" && 
            <li className="nav-item">
              <Link to="/login">
                Login
              </Link>
            </li>
          }

          {user.user_type === "logged" && 
            <li className="nav-item">
              <DropdownButton title={<FaIcons.FaUserCircle size="60"/>} id="perfil-dropdown" className="navbar-dropdown">
                <Dropdown.ItemText>
                  <h5>Status</h5>
                  <hr></hr>
                  <div className="Status">
                    <div className="Status-item" onClick={() => setState("online")}>
                      <BsIcons.BsCircleFill className="state-icon online"/>
                      <span>Online</span>
                      
                    </div>
                    <div className="Status-item" onClick={() => setState("delivering")}>
                      <BsIcons.BsCircleFill className="state-icon delivering"/>
                      <span>Delivering</span>
                      
                    </div>
                    <div className="Status-item" onClick={() => setState("offline")}>
                      <BsIcons.BsCircle className="state-icon off"/>
                      <span>Offline</span>
                      
                    </div>
                  </div>
                </Dropdown.ItemText>
                <Dropdown.Divider/>
                <Dropdown.ItemText>
                  <div onClick={() => routeChange("profile")} className="modal-item">
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
                  <div onClick={() => logout()} className="modal-item">
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
        </Switch>
    </div>

    <footer className="footer">
        <h6>2021 - Rights reserved to SafeDeliveries</h6>
    </footer>
    
      
    </>
    
  );
}

export default App;
