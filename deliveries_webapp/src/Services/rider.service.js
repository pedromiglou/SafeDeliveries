
class RiderService {
/*
    async getUserById(userId) {
        var url = urlAPI + 'api/users/' + userId;
        var res = await fetch(url);
        return res.json();
    }
*/

    changeStatus(rider, newStatus) {

        var url =  'http://localhost:8080/api/rider?id=' +rider + '&status=' + newStatus;
        console.log(url)
        fetch(url, {
            method:'PUT',
            headers:{'Content-type':'application/json'}
        });

        return;       
    }



 
}

export default new RiderService();