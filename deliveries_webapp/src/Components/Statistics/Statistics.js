/* css */
import './Statistics.css';

/* Charts */
import Bar from './Charts/Charts';
import { useEffect, useState } from 'react';
import OrderService from '../../Services/order.service';
import RiderService from '../../Services/rider.service';
import VehicleService from '../../Services/vehicle.service';


function Statistics() {
    const [ordersLast7Days, setOrdersLast7Days] = useState([]);
    const [totalOrders, setTotalOrders] = useState(0);
    const [pendingOrders, setPendingOrders] = useState(0);
    const [deliveringOrders, setDeliveringOrders] = useState(0);
    const [ordersWeight, setOrdersWeight] = useState([]);

    const [totalRiders, setTotalRiders] = useState(0);
    const [onlineRiders, setOnlineRiders] = useState(0);
    const [deliveringRiders, setDeliveringRiders] = useState(0);
    const [offlineRiders, setOfflineRiders] = useState(0);

    const [vehiclesCapacity, setVehiclesCapacity] = useState([]);

    useEffect(() => {
        async function getOrderStatistics() {
            var response = await OrderService.getOrderStatistics();
            if (!response.error) {
                setOrdersLast7Days(response.orders_7_days);
                setTotalOrders(response.total_orders);
                setPendingOrders(response.pending_orders);
                setOrdersWeight(response.orders_by_weight);
                setDeliveringOrders(response.delivering_orders);
            }
        }

        async function getRidersStatistics() {
            var response = await RiderService.getRiderStatistics();
            if (!response.error) {
                setTotalRiders(response.total_riders);
                setOnlineRiders(response.online_riders);
                setDeliveringRiders(response.delivering_riders);
                setOfflineRiders(response.offline_riders);
            }
        }

        async function getVehiclesStatistics() {
            var response = await VehicleService.getVehiclesStatistics();
            if (!response.error) {
                setVehiclesCapacity(response.vehicles_by_capacity);
            }
        }

        getRidersStatistics();
        getOrderStatistics();
        getVehiclesStatistics()
    }, [])

    return (
      <>
      <div className="admin-statistics">
        <div className="admin-orders">
            <div className="admin-collumn">
                <div className="collumn-item">
                    <h5 id="total_orders">Total Orders</h5>
                    <h6>{totalOrders}</h6>
                </div>
                <div className="collumn-item">
                    <h5 id="delivering_orders">Delivering Orders</h5>
                    <h6>{deliveringOrders}</h6>
                </div>
                <div className="collumn-item">
                    <h5 id="waiting_orders">Waiting Orders</h5>
                    <h6>{pendingOrders}</h6>
                </div>
            </div>
            <div className="graph">
                <Bar chart="Line" label={["6 days ago", "5 days ago", "4 days ago", "3 days ago", "2 days ago", "yesterday", "today"]} data={ordersLast7Days}/>
            </div>
            
            
            
            
        </div>
        <div className="admin-riders">
            <div className="admin-collumn">
                <div className="collumn-item">
                    <h4 id="total_riders">Total Number of Riders</h4>
                    <h5>{totalRiders}</h5>
                </div>
                <div className="riders-subsection">
                    <div className="subsection-item">
                        <h4 id="online_riders">Riders Online</h4>
                        <h5>{onlineRiders}</h5>
                    </div>
                    <div className="subsection-item">
                        <h4 id="offline_riders">Riders Offline</h4>
                        <h5>{offlineRiders}</h5>
                    </div>
                    <div className="subsection-item">
                        <h4 id="delivering_riders">Riders delivering</h4>
                        <h5>{deliveringRiders}</h5>
                    </div>
                </div>
            </div>
        </div>
        <div className="admin-vehicles">
            <Bar chart="Bar" label={["up to 5 kg", "5 kg - 15 kg", "15 kg - 30 kg", "30 kg - 100 kg", "100 kg and up"]} data={vehiclesCapacity}/>
        </div>
        <div className="admin-capacity">
            <Bar chart="Bar" label={["up to 5 kg", "5 kg - 15 kg", "15 kg - 30 kg", "30 kg and up"]} data={ordersWeight}/>
        </div>
      </div>
      </>
      
    );
  }
  
  export default Statistics;