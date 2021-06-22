import {urlAPI} from "./../data/data";

class VehicleService {

    async getVehiclesByRiderId(riderId) {
        var url = urlAPI + 'api/private/rider/' + riderId + '/vehicles';
        var res = await fetch(url, {
            method: "GET",
            headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]}
        });
        return res.json();
    }

    async createVehicle(registration, brand,  model, category, capacity, idRider) {

        let vehicle= {
            registration: registration,
            brand: brand,
            model: model,
            category: category,
            capacity: capacity,
            rider: idRider
        }

        var url = urlAPI + 'api/private/vehicle'

        await fetch(url, {
            method:'POST',
            headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]},
            body: JSON.stringify(vehicle)
        });

        return;       

    }

    async editVehicle(id, registration, brand,  model, category, capacity) {

        let vehicle= {
            registration: registration,
            brand: brand,
            model: model,
            category: category,
            capacity: capacity,
        }

        var url = urlAPI + 'api/private/vehicle/'+id

        await fetch(url, {
            method:'PUT',
            headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]},
            body: JSON.stringify(vehicle)
        });

        return;       

    }

    async removeVehicle(id) {

        var url = urlAPI + 'api/private/vehicle/'+id

        await fetch(url, {
            method:'DELETE',
            headers:{'Content-type':'application/json',
                    'Authorization': 'Bearer ' + JSON.parse(sessionStorage.getItem("user"))["token"]},
        });

        return;       

    }

    async getVehiclesStatistics() {
        var url = urlAPI + 'api/private/vehicles/statistics';
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

export default new VehicleService();