class AuthService {

    async login(email, password) {
        let userInfo= {
            email: email,
            password: password,
        }

        try {
            var res = await fetch('http://localhost:8081/api/login', {
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
            sessionStorage.setItem("user_orders", JSON.stringify(json));
        } 
        return json

    }

    async register(firstname, lastname, email, password) {
        let userInfo= {
            firstname: firstname,
            lastname: lastname,
            email: email,
            password: password,
            account_type: "U"
        }
        try {
            var res = await fetch('http://localhost:8081/api/register', {
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
        return JSON.parse(sessionStorage.getItem("user_orders"))
    }


}

export default new AuthService()