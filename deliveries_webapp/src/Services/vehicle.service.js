import {urlAPI} from "./../data/data";

class VehicleService {

    async getVehiclesByRiderId(riderId) {
        var url = urlAPI + 'api/vehiclesbyrider?id=' + riderId;
        var res = await fetch(url);
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

        var url = urlAPI + 'api/vehicle'

        await fetch(url, {
            method:'POST',
            headers:{'Content-type':'application/json'},
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
        console.log(vehicle)

        var url = urlAPI + 'api/vehicle/'+id

        await fetch(url, {
            method:'PUT',
            headers:{'Content-type':'application/json'},
            body: JSON.stringify(vehicle)
        });

        return;       

    }

    async removeVehicle(id) {

        var url = urlAPI + 'api/vehicle/'+id

        await fetch(url, {
            method:'DELETE',
            headers:{'Content-type':'application/json'},
        });

        return;       

    }


 
}

export default new VehicleService();