/* css */
import './App.css';

/* icons */
import * as FiIcons from 'react-icons/fi';

/* router */
import {Link, BrowserRouter, Route, Switch, withRouter} from 'react-router-dom';

/* Components */
import Home from './Components/Home/Home';
import Delivery from './Components/Delivery/Delivery';
import History from './Components/History/History';
import Login from './Components/Login/Login';

function App() {
  return (
    <>
    <BrowserRouter>
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
            <li className="nav-item">
              <Link to="/delivery">
                Delivery
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/history">
                Deliveries History
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/aboutus">
                About us
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/login">
                Login
              </Link>
            </li>
          </ul>
      </navbar>

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
    </BrowserRouter>
      
    </>
  );
}

export default App;
