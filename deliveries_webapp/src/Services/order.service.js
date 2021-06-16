class OrderService {

    async acceptOrder(order_id, rider_id) {
        var url = 'http://localhost:8080/api/private/acceptorder?order_id=' + order_id + '&rider_id=' + rider_id;
        var res = await fetch(url, {
            method:'POST',
            headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]}
        });
        return res.json();
    }

    async declineOrder(order_id, rider_id) {
        var url = 'http://localhost:8080/api/private/declineorder?order_id=' + order_id + '&rider_id=' + rider_id;
        await fetch(url, {
            method:'POST',
            headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]}
        });
        return;
    }

}

export default new OrderService();
