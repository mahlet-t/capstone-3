
let userService;

class UserService {
    currentUser = {};

    constructor()
    {
        this.loadUser();
    }

    getHeader()
    {
        if(this.currentUser.token) {
            return {
                    'Authorization': `Bearer ${this.currentUser.token}`
            };
        }

        return {};
    }

    saveUser(user)
    {
        this.currentUser = {
            token: user.token,
            userId: user.user.id,
            username: user.user.username,
            role: user.user.authorities[0].name
        }
        localStorage.setItem('user', JSON.stringify(this.currentUser));
    }

    loadUser()
    {
        const user = localStorage.getItem('user');
        if(user)
        {
            this.currentUser = JSON.parse(user);
            axios.defaults.headers.common = {'Authorization': `Bearer ${this.currentUser.token}`}
        }
    }

    getHeaders()
    {
        const headers = {
            'Content-Type': 'application/json'
        }

        if(this.currentUser.token)
        {
            headers.Authorization = `Bearer ${this.currentUser.token}`;
        }

        return headers;
    }

    getUserName()
    {
        return this.isLoggedIn() ? this.currentUser.username : '';
    }

    isLoggedIn()
    {
        return this.currentUser.token !== undefined;
    }

    getCurrentUser()
    {
        return this.currentUser;
    }

    setHeaderLogin()
    {
        const user = {
                username: this.getUserName(),
                loggedin: this.isLoggedIn(),
                loggedout: !this.isLoggedIn()
            };

        templateBuilder.build('header', user, 'header-user');
    }

    register (username, password, confirm)
    {
        const url = `${config.baseUrl}/register`;
        const register = {
            username: username,
            password: password,
            confirmPassword: confirm,
            role: 'USER'
        };

        axios.post(url, register)
             .then(response => {
             const data={message:"registered"};
             templateBuilder.append("message",data,"errors")

             })
            .catch(error => {

                const data = {
                    error: "User registration failed."
                };

                templateBuilder.append("error", data, "errors")
            });
    }

    login (username, password)
    {
        const url = `${config.baseUrl}/login`;
        const login = {
            username: username,
            password: password
        };

        axios.post(url, login)
            .then(response => {
                this.saveUser(response.data)
                this.setHeaderLogin();

                axios.defaults.headers.common = {'Authorization': `Bearer ${this.currentUser.token}`}
                productService.enableButtons();
                cartService.loadCart();
            })
            .catch(error => {
                const data = {
                    error: "Login failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    logout()
    {
        localStorage.removeItem('user');
        axios.defaults.headers.common = {'Authorization': `bearer ${this.currentUser.token}`}
        this.currentUser = {};

        this.setHeaderLogin();

        productService.enableButtons();
    }

}
window.Register =function (){
const username=document.getElementById("username").value;
const password=document.getElementById("password").value;
userService.register(username,password,password);

}

document.addEventListener('DOMContentLoaded', () => {
    userService = new UserService();
    userService.setHeaderLogin();
});
