/* css */
import './App.css';
import DropdownButton from 'react-bootstrap/DropdownButton';
import Dropdown from 'react-bootstrap/Dropdown';

/* icons */
import * as FiIcons from 'react-icons/fi';
import * as FaIcons from 'react-icons/fa';

/* router */
import {Link, Route, Switch, withRouter} from 'react-router-dom';

/* Components */
import Home from './Components/Home/Home';
import Delivery from './Components/Delivery/Delivery';
import History from './Components/History/History';
import Login from './Components/Login/Login';

/* React */
import AuthService from './Services/auth.service';

import {urlWeb} from "./data/data";


function App() {
  const current_user = AuthService.getCurrentUser();
  /*
  const history = useHistory();

  
  function routeChange(path){ 
      let new_url = '/' + path; 
      history.push(new_url);
  }
  */

  function logout(){
    sessionStorage.removeItem("user_orders");
    window.location.assign(urlWeb);
  }

  return (
    <>
      <div>
          <ul className="nav-list">
            <li className="nav-item" id="logo">
              <Link to="/">
                <FiIcons.FiPackage/><span>SafeDeliveries</span>
              </Link>
            </li>
            <li className="nav-item" id="home-tab">
              <Link to="/">
                Home
              </Link>
            </li>
            {current_user !== null && 
            <>
              <li className="nav-item">
                <Link to="/delivery" id="request-tab">
                  Request Delivery
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
                <Link to="/login" id="login">
                  Login
                </Link>
              </li>
            }

            {current_user !== null && 
            <li className="nav-item">
              <DropdownButton title={<FaIcons.FaUserCircle size="60"/>} id="perfil-dropdown" className="navbar-dropdown">
                <Dropdown.ItemText>
                  <div onClick={() => logout()} className="modal-item">
                    <h5>Logout</h5>
                  </div>
                </Dropdown.ItemText>
              </DropdownButton>
                
            </li>
            }
            
          </ul>
      </div>

      <div className="content">
          <Switch>
              <Route exact path='/' component={withRouter(Home)} />
              <Route exact path='/delivery' component={withRouter(Delivery)} />
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
