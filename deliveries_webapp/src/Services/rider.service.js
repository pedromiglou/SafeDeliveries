class RiderService {

        async getRiderById(riderId) {
            var url = 'http://localhost:8080/api/rider?id=' + riderId;
            var res = await fetch(url);
            return res.json();
        }

        async changeStatus(rider, newStatus) {
            var url =  'http://localhost:8080/api/rider?id=' +rider + '&status=' + newStatus;
    
            await fetch(url, {
                method:'PUT',
                headers:{'Content-type':'application/json'}
            });
    
            return;       
        }
        
        async changeRider(id, fname, lname, email) {
            var url =  'http://localhost:8080/api/rider?id='+id

            if (fname !== undefined && fname !== null && fname !== "") {
                url = url + "&firstname=" + fname
            }

            if (lname !== undefined && lname !== null && lname !== "") {
                url = url + "&lastname=" + lname
            }

            if (email !== undefined && email !== null && email !== "") {
                url = url + "&email=" + email
            }
    
            await fetch(url, {
                method:'PUT',
                headers:{'Content-type':'application/json'}
            });
    
            return;       

        }
    
    
     
    }
    
    export default new RiderService();