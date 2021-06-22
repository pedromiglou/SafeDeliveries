import {urlAPI} from './../data/data';

class NotificationService {

    async getNotificationByUserId(riderId) {
        var url = urlAPI + 'api/private/notifications?id=' + riderId;
        var res = await fetch(url, {
            method:'GET',
            headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]}
        });
        if (res.status === 404) {
            return []
        }
        if (res.status !== 200) {
            return {error: true};
        }
        return res.json();
    }

}

export default new NotificationService();
