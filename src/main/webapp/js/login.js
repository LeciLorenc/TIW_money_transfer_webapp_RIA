(function() { // avoid variables ending up in the global scope

    document.getElementById("login_button").addEventListener('click', (e) => {
        const form = e.target.closest("form");
        e.preventDefault();
        if (form.checkValidity()) {
            myMakeCall("POST", 'checkLogin', e.target.closest("form"),
                function(x) {
                    if (x.readyState === XMLHttpRequest.DONE) {
                        const message = x.responseText;
                        switch (x.status) {
                            case 200:
                                const data = JSON.parse(x.responseText);
                                sessionStorage.setItem('id', data.id);
                                sessionStorage.setItem('name', data.name);
                                window.location.href = "home.html";
                                break;
                            case 400: // bad request
                                document.getElementById("errorMessage").textContent = message;
                                break;
                            case 401: // unauthorized
                                document.getElementById("errorMessage").textContent = message;
                                break;
                            case 500: // server error
                                document.getElementById("errorMessage").textContent = message;
                                break;
                        }
                    }
                }
            );
        } else {
            form.reportValidity();
        }
    });

})();