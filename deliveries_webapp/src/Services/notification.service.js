class NotificationService {

    async getNotificationByUserId(riderId) {
        var url = 'http://localhost:8080/api/notifications?id=' + riderId;
        var res = await fetch(url);
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
