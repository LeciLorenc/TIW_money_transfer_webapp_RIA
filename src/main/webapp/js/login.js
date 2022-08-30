(function() { // avoid variables ending up in the global scope

    document.getElementById("login_button").addEventListener('click', (e) => {
        var form = e.target.closest("form");
        e.preventDefault();
        if (form.checkValidity()) {
            myMakeCall("POST", 'checkLogin', e.target.closest("form"),
                function(x) {
                    if (x.readyState === XMLHttpRequest.DONE) {
                        var message = x.responseText;
                        switch (x.status) {
                            case 200:
                                var data = JSON.parse(x.responseText);
                                sessionStorage.setItem('id', data.id);
                                sessionStorage.setItem('name', data.name);
                                window.location.href = "home.html";
                                true;
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

function myMakeCall(method, url, formElement, cback, reset = true) {
    var req = new XMLHttpRequest(); // visible by closure
    req.onreadystatechange = function() {
        cback(req)
    }; // closure
    req.open(method, url);
    if (formElement == null) {
        req.send();
    } else {
        req.send(new FormData(formElement));
    }
    if (formElement !== null && reset === true) {
        formElement.reset();
    }
}

// (function() { // avoid variables ending up in the global scope
//
//     document.getElementById("login_button").addEventListener('click', (e) => {
//         var form = e.target.closest("form");
//         e.preventDefault();
//         if (form.checkValidity()) {
//             var formToSend = $('form').serialize();
//             let postRequest = $.post("checkLogin", formToSend);
//
//             postRequest.done(function (data, textStatus, jqXHR) {
//                 sessionStorage.setItem('isLoggedIn', 'true');
//
//                 //Here we save the data into the sessionStorage
//                 let user_data = jqXHR.responseJSON;
//                 // sessionStorage.setItem('username', user_data.username);
//                 // var user_data = JSON.parse(x.responseText);
//                 sessionStorage.setItem('id', user_data.id);
//                 sessionStorage.setItem('name', user_data.name);
//                 window.location.href = 'home.html';;
//
//
//
//             });
//             // myMakeCall("POST", 'checkLogin', e.target.closest("form"),
//             //     function(x) {
//             //         if (x.readyState === XMLHttpRequest.DONE) {
//             //             var message = x.responseText;
//             //             switch (x.status) {
//             //                 case 200:
//             //                     var data = JSON.parse(x.responseText);
//             //                     sessionStorage.setItem('id', data.id);
//             //                     sessionStorage.setItem('name', data.name);
//             //                     window.location.href = 'home.html';
//             //                     // return false;
//             //                     break;
//             //                 case 400: // bad request
//             //                     document.getElementById("errorMessage").textContent = message;
//             //                     break;
//             //                 case 401: // unauthorized
//             //                     document.getElementById("errorMessage").textContent = message;
//             //                     break;
//             //                 case 500: // server error
//             //                     document.getElementById("errorMessage").textContent = message;
//             //                     break;
//             //             }
//             //         }
//             //     }
//             // );
//         } else {
//             form.reportValidity();
//         }
//     });
//
// })();


