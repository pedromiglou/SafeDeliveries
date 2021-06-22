/* css */
import './Home.css';

function Home() {
    return (
      <>
        <div className="HomeSection">
            <div>
                <h1>SafeDeliveries</h1>
                <i>Welcome to SafeDeliveries Riders side webapp! Here you are able to earn some money by working for us delivering some products in a city of your own wish. <br /> 
                You just need to have one thing in your mind. Since we are SafeDeliveries, Drive Safe!
                </i>
            </div>
            <div>
                <img className="background-img" src={process.env.PUBLIC_URL + "/images/background/Delivery.jpg"} alt="background-img"></img>
            </div>
            
        </div>
      </>
      
    );
  }
  
export default Home;