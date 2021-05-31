/* css */
import { useState } from 'react';
import './Login.css';

function Login() {
    const [state, setState] = useState("Login");

    return (
      <>
        <div className="LoginSection">
            { state === "Login" && 
            <div className="Login_Register">
                <h2>Login</h2>
                <hr></hr>
                <h3>Name</h3>
                <input type="text" placeholder="Name"></input>
                <h3>Password</h3>
                <input type="password" placeholder="Password"></input>
                <i onClick={() => setState("Register")}>Don't have an account? Regist here</i>
                <button className="button-entrar">Enter</button>
            </div>
            }
            { state === "Register" && 
            <div className="Login_Register">
                <h2>Register</h2>
                <hr></hr>
                <h3>Name</h3>
                <input type="text" placeholder="Name"></input>
                <h3>Email</h3>
                <input type="text" placeholder="Email"></input>
                <h3>Password</h3>
                <input type="password" placeholder="Password"></input>
                <h3>Confirm Password</h3>
                <input type="password" placeholder="Confirm Password"></input>
                <i onClick={() => setState("Login")}>Already have an account? Click here</i>
                <button className="button-entrar">Create</button>
            </div>
            }
            
        </div>
      </>
      
    );
  }
  
export default Login;