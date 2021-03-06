import {urlAPI} from './../data/data';

class OrderService {

    async acceptOrder(order_id, rider_id) {
        var url = urlAPI + 'api/private/acceptorder?order_id=' + order_id + '&rider_id=' + rider_id;
        var res = await fetch(url, {
            method:'POST',
            headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]}
        });
        return res.json();
    }

    async declineOrder(order_id, rider_id) {
        var url = urlAPI +  'api/private/declineorder?order_id=' + order_id + '&rider_id=' + rider_id;
        await fetch(url, {
            method:'POST',
            headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]}
        });
        return;
    }

    async getOrderStatistics() {
        var url = urlAPI + 'api/private/orders/statistics';
        var res = await fetch(url, {
            method:'GET',
            headers:{'Content-type':'application/json',
                'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]}});
        if (res.status !== 200) {
            return {error: true};
        }
        return res.json();
    }

    async getOrdersByUser(user_id) {
        try {
            var res = await fetch(urlAPI + 'api/private/rider/' + user_id + '/orders/', {
                method:'GET',
                headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]}
            })
        } catch {
            return {error: true, message: "An error occured during the request."};
        }
        var json = await res.json();
        if (res.status !== 200) {
            return {error: true, message: json.message};
        }
        return json
    }

    async getOrderById(order_id) {
        try {
            var res = await fetch(urlAPI + 'api/private/orders/' + order_id, {
                method:'GET',
                headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]}
            })
        } catch {
            return {error: true, message: "An error occured during the request."};
        }
        var json = await res.json();
        if (res.status !== 200) {
            return {error: true, message: json.message};
        }
        return json
    }


}

export default new OrderService();
