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
                    <h5>Total Orders</h5>
                    <h6>540</h6>
                </div>
                <div className="collumn-item">
                    <h5>Delivering Orders</h5>
                    <h6>539</h6>
                </div>
                <div className="collumn-item">
                    <h5>Waiting Orders</h5>
                    <h6>1</h6>
                </div>
            </div>
            <div className="graph">
                <Bar chart="Line"/>
            </div>
            
            
            
            
        </div>
        <div className="admin-riders">
            <div className="admin-collumn">
                <div className="collumn-item">
                    <h4>Total Number of Riders</h4>
                    <h5>540</h5>
                </div>
                <div className="riders-subsection">
                    <div className="subsection-item">
                        <h4>Riders Online</h4>
                        <h5>540</h5>
                    </div>
                    <div className="subsection-item">
                        <h4>Riders Offline</h4>
                        <h5>540</h5>
                    </div>
                    <div className="subsection-item">
                        <h4>Riders delivering</h4>
                        <h5>540</h5>
                    </div>
                </div>
            </div>
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