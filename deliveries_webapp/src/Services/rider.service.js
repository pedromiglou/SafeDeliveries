import {urlAPI} from './../data/data';

class RiderService {

        async getRiderById(riderId) {
            var url = urlAPI + 'api/private/rider?id=' + riderId;
            var res = await fetch(url, {
                method: 'GET',
                headers:{'Content-type':'application/json',
                         'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]}
            });
            return res.json();
        }


        async changeStatus(id, newStatus) {
            var url =  'http://localhost:8080/api/private/rider/' +id;
    
            let rider = {
                status: newStatus
            }

            await fetch(url, {
                method:'PUT',
                headers:{'Content-type':'application/json',
                         'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]},
                body: JSON.stringify(rider)
            });
    
            return;       
        }
        
        async changeRider(id, fname, lname, email) {
            let rider= {}

            var url = urlAPI + 'api/private/rider/'+id

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
                headers:{'Content-type':'application/json',
                         'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]},
                body: JSON.stringify(rider)
            });
    
            return;       
        }

        async getRiderStatistics() {
            var url = 'http://localhost:8080/api/private/riders/statistics';
            var res = await fetch(url, {
                method:'GET',
                headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]}});
            if (res.status !== 200) {
                return {error: true};
            }
            return res.json();
        }
    }
    
export default new RiderService();
