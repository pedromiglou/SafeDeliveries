/* css */
import { useState } from 'react';
import './Login.css';
import AuthService from "../../Services/auth.service";

import Geocode from "react-geocode";

// set Google Maps Geocoding API for purposes of quota management. Its optional but recommended.
Geocode.setApiKey("AIzaSyCrtpEJj-sxKhggyLM3ms_tdEdh7XJNEco");

// set response language. Defaults to english.
Geocode.setLanguage("en");

function Login() {
    const [state, setState] = useState("Login");
    const [sucessRegister, setSucessRegister] = useState(false);
    const [errorRegister, setErrorRegister] = useState(false);
    const [errorLogin, setErrorLogin] = useState(false);

    async function login() {
      setSucessRegister(false);
      setErrorRegister(false);
      var response = await AuthService.login(
        document.getElementById("login_email_input").value,
        document.getElementById("login_password_input").value
      )   

      if (response.error !== true) {
        window.location.assign("http://localhost:3000/");
      } else {
        setErrorLogin(true);
      }
    }

    async function registerFunction(event) {
      event.preventDefault();
      await register();
    }

    async function loginFunction(event) {
      event.preventDefault();
      await login();
    }

    async function register() {
      setSucessRegister(false);
      setErrorRegister(false);
      setErrorLogin(false);
      var city_name = document.getElementById("register_city_input").value;

      // Get latitude & longitude from address.
      var coords = await Geocode.fromAddress(city_name);
      const { lat, lng } = coords.results[0].geometry.location;

      var response = await AuthService.register(
        document.getElementById("register_firstname_input").value,
        document.getElementById("register_lastname_input").value,
        document.getElementById("register_email_input").value,
        document.getElementById("register_password_input").value,
        lat,
        lng
      )   

      if (response.error !== true) {
        setSucessRegister(true);
        setState("Login")
      } else {
        setSucessRegister(false);
        setErrorRegister(true);
      }
    }

    function validatePassword(){
      var password = document.getElementById("register_password_input")
        , confirm_password = document.getElementById("register_confirmpass_input");
      if(password.value !== confirm_password.value) {
        confirm_password.setCustomValidity("Passwords Don't Match");
      } else {
        confirm_password.setCustomValidity('');
      }
    }

    

    return (
      <>

        {sucessRegister === true 
          ? <div className="alert alert-success" role="alert" style={{margin:"10px auto", width: "90%", textAlign:"center", fontSize:"22px"}}>
          Your account has been created successfully! 
          </div> : null}

        {errorRegister === true 
          ? <div className="alert alert-alert" role="alert" style={{margin:"10px auto", width: "90%", textAlign:"center", fontSize:"22px"}}>
          There was an error creating your account.
          </div> : null}

        {errorLogin === true 
          ? <div className="alert alert-alert" role="alert" style={{margin:"10px auto", width: "90%", textAlign:"center", fontSize:"22px"}}>
          Invalid credentials.
          </div> : null}

        <div className="LoginSection">
            { state === "Login" && 
            <form onSubmit={loginFunction}>
              <div className="Login_Register">
                  <h2>Login</h2>
                  <hr></hr>
                  <h3>Email</h3>
                  <input type="text" id="login_email_input" placeholder="Email"></input>
                  <h3>Password</h3>
                  <input type="password" id="login_password_input" placeholder="Password"></input>
                  <i id="register_icon" onClick={() => setState("Register") }>Don't have an account? Regist here</i>
                  <button type="submit" className="button-entrar">Enter</button>
              </div>
            </form>
            }
            { state === "Register" && 
            <form onSubmit={registerFunction}>
              <div className="Login_Register">
                  <h2>Register</h2>
                  <hr></hr>
                  <h3>First Name</h3>
                  <input type="text" id="register_firstname_input" placeholder="First name" required></input>
                  <h3>Last Name</h3>
                  <input type="text" id="register_lastname_input" placeholder="Last name" required></input>
                  <h3>Email</h3>
                  <input type="text" id="register_email_input" placeholder="Email" required></input>
                  <h3>Password</h3>
                  <input onChange={validatePassword} type="password" id="register_password_input" placeholder="Password" required></input>
                  <h3>Confirm Password</h3>
                  <input onKeyUp={validatePassword} type="password" id="register_confirmpass_input" placeholder="Confirm Password" required></input>
                  <h3>City</h3>
                  <input type="text" id="register_city_input" placeholder="City name" required></input>
                  <i onClick={() => setState("Login")}>Already have an account? Click here</i>
                  <button type="submit" className="button-entrar">Create</button>
              </div>
            </form>
            }
            
        </div>
      </>
      
    );
  }
  
export default Login;