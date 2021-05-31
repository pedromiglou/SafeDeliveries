/* css */
import './Home.css';

function Home() {
    return (
      <>
        <div className="HomeSection">
            <div>
                <h1>SafeDeliveries</h1>
                <i>"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."</i>
            </div>
            <div>
                <img className="background-img" src={process.env.PUBLIC_URL + "/images/background/Delivery.jpg"} alt="background-img"></img>
            </div>
            
        </div>
      </>
      
    );
  }
  
export default Home;