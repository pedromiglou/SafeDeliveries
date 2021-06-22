import {urlAPI} from "./../data/data";

class OrdersService {

    async create(pick_up_lat, pick_up_lng, deliver_lat, deliver_lng, items, user_id) {
        let orderInfo= {
            pick_up_lat: pick_up_lat,
            pick_up_lng: pick_up_lng,
            deliver_lat: deliver_lat,
            deliver_lng: deliver_lng,
            items: items,
            user_id: user_id
        }

        try {
            var res = await fetch(urlAPI + 'api/private/orders', {
                method:'POST',
                headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user_orders"))["token"]},
                body: JSON.stringify(orderInfo)
            })
        } catch {
            return {error: true, message: "An error occured during the request."};
        }
        var json = await res.json();
        if (res.status !== 201) {
            return {error: true, message: json.message};
        }
        return json

    }

    async getOrderInfo(order_id){
        try {
            var res = await fetch(urlAPI + 'api/orders/' + order_id, {
                method:'GET',
                headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user_orders"))["token"]}
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

    async confirmDelivery(order_id, rating){
        let orderInfo= {
            order_id: order_id,
            rating: rating
        }
        console.log(orderInfo);
        try {
            var res = await fetch(urlAPI + 'api/private/order/confirm', {
                method:'POST',
                headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user_orders"))["token"]},
                body: JSON.stringify(orderInfo)
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

    async getOrdersByUser(user_id) {
        try {
            var res = await fetch(urlAPI + 'api/private/user/' + user_id + '/orders/', {
                method:'GET',
                headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user_orders"))["token"]}
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

export default new OrdersService()