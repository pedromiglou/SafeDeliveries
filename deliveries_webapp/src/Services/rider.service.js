import {urlAPI} from './../data/data';

class RiderService {

        async getRiderById(riderId) {
            var url = urlAPI + 'api/rider?id=' + riderId;
            var res = await fetch(url);
            return res.json();
        }


        async changeStatus(rider, newStatus) {
            var url = urlAPI + 'api/rider?id=' +rider + '&status=' + newStatus;
    
            await fetch(url, {
                method:'PUT',
                headers:{'Content-type':'application/json'}
            });
    
            return;       
        }
        
        async changeRider(id, fname, lname, email) {
            let rider= {}

            var url = urlAPI + 'api/rider/'+id

            if (fname !== undefined && fname !== null && fname !== "") {
                rider.firstname = fname
            }

            if (lname !== undefined && lname !== null && lname !== "") {
                rider.lastname = lname
            }

            if (email !== undefined && email !== null && email !== "") {
                rider.email = email
            }
    
            await fetch(url, {
                method:'PUT',
                headers:{'Content-type':'application/json'},
                body: JSON.stringify(rider)
            });
    
            return;       

        }
    
    
     
    }
    
export default new RiderService();
