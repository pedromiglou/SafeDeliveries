/* css */
import './History.css';

/* react */
import { useHistory } from "react-router-dom";

function History() {

    const history = useHistory();

    const routeChange = () =>{ 
        let path = '/delivery'; 
        history.push(path, {is_History:true});
    }

    return (
      <>
        <div className="HistorySection">
            <div className="historyTable">
                <h1> Last Deliveries</h1>
                <ul className="list-group">
                    <li className="list-item">
                        <div>
                            Date
                        </div>

                        <div>
                            Item
                        </div>
                        
                        <div>
                            Your address
                        </div>

                        <div>
                            Destiny Address
                        </div>

                        <div>
                            Time
                        </div>
                    </li>

                    <li className="list-item" onClick={() => routeChange()}>
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                        
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                    </li>

                    <li className="list-item" onClick={() => routeChange()}>
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                        
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                    </li>

                    <li className="list-item" onClick={() => routeChange()}>
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                        
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                    </li>

                    <li className="list-item" onClick={() => routeChange()}>
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                        
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                    </li>

                    <li className="list-item" onClick={() => routeChange()}>
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                        
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                    </li>

                    <li className="list-item" onClick={() => routeChange()}>
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                        
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                    </li>


                    <li className="list-item" onClick={() => routeChange()}>
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                        
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                    </li>

                    <li className="list-item" onClick={() => routeChange()}>
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                        
                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>

                        <div>
                            xxx
                        </div>
                    </li>

                </ul>
            </div>            
        </div>
      </>
      
    );
  }
  
export default History;