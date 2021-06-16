/* css */
import './Statistics.css';

/* Charts */
import Bar from './Charts/Charts';


function Statistics() {
    return (
      <>
      <div className="admin-statistics">
        <div className="admin-orders">
            <div className="admin-collumn">
                <div className="collumn-item">
                    <h4>Total Orders</h4>
                    <h5>540</h5>
                </div>
                <div className="collumn-item">
                    <h4>Delivering Orders</h4>
                    <h5>539</h5>
                </div>
                <div className="collumn-item">
                    <h4>Waiting Orders</h4>
                    <h5>1</h5>
                </div>
            </div>
            <div className="graph">
                <Bar chart="Line"/>
            </div>
            
            
            
            
        </div>
        <div className="admin-riders">

        </div>
        <div className="admin-vehicles">
            <Bar chart="Bar"/>
        </div>
        <div className="admin-capacity">
            <Bar chart="Bar"/>
        </div>
      </div>
      </>
      
    );
  }
  
  export default Statistics;