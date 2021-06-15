class AuthService {

    async login(email, password) {
        let userInfo= {
            email: email,
            password: password,
        }

        try {
            var res = await fetch('http://localhost:8080/api/login', {
                method:'POST',
                headers:{'Content-type':'application/json'},
                body: JSON.stringify(userInfo)
            })
        } catch {
            return {error: true};
        }

        if (res.status !== 200) {
            return {error: true};
        }

        var json = await res.json()
        
        if(json.token) {
            json.status = "Online"
            sessionStorage.setItem("user", JSON.stringify(json));
        } 
        return json

    }

    async register(firstname, lastname, email, password, lat, lng) {
        let userInfo= {
            firstname: firstname,
            lastname: lastname,
            email: email,
            password: password,
            rating: 0.0,
            status: "Offline",
            lat: lat,
            lng: lng
        }
        try {
            var res = await fetch('http://localhost:8080/api/register', {
                method:'POST',
                headers:{'Content-type':'application/json'},
                body: JSON.stringify(userInfo)
            })
        } catch {
            return {error: true};
        }

        if (res.status !== 201) {
            return {error: true};
        }
        var json = await res.json()
    
        return json

    }

    getCurrentUser() {
        return JSON.parse(sessionStorage.getItem("user"))
    }


}

export default new AuthService()