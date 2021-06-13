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
        console.log(orderInfo);
        try {
            var res = await fetch('http://localhost:8081/api/orders', {
                method:'POST',
                headers:{'Content-type':'application/json'},
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
}

export default new OrdersService()