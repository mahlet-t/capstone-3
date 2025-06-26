let profileService;

class ProfileService
{
    loadProfile()
    {
        const url = `${config.baseUrl}/profile`;

        axios.get(url)
             .then(response => {
                 templateBuilder.build("profile", response.data, "main")
             })
             .catch(error => {
                 const data = {
                     error: "Load profile failed."
                 };

                 templateBuilder.append("error", data, "errors")
             })
    }

    updateProfile(profile)
    {

        const url = `${config.baseUrl}/profile`;

        axios.put(url, profile)
             .then(() => {
                 const data = {
                     message: "The profile has been updated."
                 };

                 templateBuilder.append("message", data, "errors")
             })
             .catch(error => {
                 const data = {
                     error: "Save profile failed."
                 };

                 templateBuilder.append("error", data, "errors")
             })
    }
}
window.Register= function() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const url = `${config.baseUrl}/register`;

    const user = {
        username: username,
        password: password,
        role: "USER"
    };

    axios.post(url, user)
        .then(() => {
            const data = {
                message: "✅ Registered successfully. You can now login."
            };
            templateBuilder.append("message", data, "errors");
        })
        .catch(() => {
            const data = {
                error: "Registration failed. Try a different username."
            };
            templateBuilder.append("error", data, "errors");
        });
}
document.addEventListener("DOMContentLoaded", () => {
   profileService = new ProfileService();
});
