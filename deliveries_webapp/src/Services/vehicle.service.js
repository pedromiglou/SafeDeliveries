class VehicleService {

    async createVehicle(registration, brand,  model, category, capacity, idRider) {

        let vehicle= {
            registration: registration,
            brand: brand,
            model: model,
            category: category,
            capacity: capacity,
            rider: idRider
        }

        var url =  'http://localhost:8080/api/vehicle'

        await fetch(url, {
            method:'POST',
            headers:{'Content-type':'application/json'},
            body: JSON.stringify(vehicle)
        });

        return;       

    }


 
}

export default new VehicleService();