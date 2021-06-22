/* css */
import './Home.css';

function Home() {
    return (
      <>
        <div className="HomeSection">
            <div>
                <h1>SafeDeliveries</h1>
                <i>Welcome to SafeDeliveries Client side webapp! Here you are able to create your own orders, adding the necessary items to them and selecting a pick up and deliver address of your own choice. <br />
                  You just need to have one thing in your mind. Since we are SafeDeliveries, Receive your orders safely!</i>
            </div>
            <div>
                <img className="background-img" src={process.env.PUBLIC_URL + "/images/background/Delivery.jpg"} alt="background-img"></img>
            </div>
            
        </div>
      </>
      
    );
  }
  
export default Home;