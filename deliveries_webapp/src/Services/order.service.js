class OrderService {

    async acceptOrder(order_id, rider_id) {
        var url = 'http://localhost:8080/api/acceptorder?order_id=' + order_id + '&rider_id=' + rider_id;
        var res = await fetch(url, {
            method:'POST',
            headers:{'Content-type':'application/json'}
        });
        return res.json();
    }

    async declineOrder(order_id, rider_id) {
        var url = 'http://localhost:8080/api/declineorder?order_id=' + order_id + '&rider_id=' + rider_id;
        await fetch(url, {
            method:'POST',
            headers:{'Content-type':'application/json'}
        });
        return;
    }

    async getOrderStatistics() {
        var url = 'http://localhost:8080/api/orders/statistics';
        var res = await fetch(url);
        if (res.status !== 200) {
            return {error: true};
        }
        return res.json();
    }

}

export default new OrderService();
