/**
 * Loading Div
 * -------------
 * Description: If the class "loading" is added to body, a modal waiting window is shown.
 *              To hide the window, just remove class "loading" from body.
 * Notes:
 * - Object instance is global on purpose as to be used if needed in other js files.
 * - It's even possible to import css and append message div directly to the document
 *   in an init event. In this case, was chosen not to do so, to optimize the loading of the page.
 * - This object does not conflict with eventual other body's classes
 */
const loadingModal = new LoadingModal(document.getElementById("loading_msg"));

function LoadingModal(loading_msg){
    this.loading_msg = loading_msg;
    this.show = function(message){
        this.update(message);
        if (!document.body.className.includes("loading"))
            document.body.className += " loading";
    };
    this.update = function(message){
        if (message) //If a message is supplied to method
            this.loading_msg.textContent = message;
        else
            this.loading_msg.textContent = "Communicating with Server ...";
        
    };
    this.hide = function(){
        document.body.className = document.body.className.replace(" loading", "");
    };
}

/**
 * Implicit submission for forms
 * -------------
 * Description: Whenever the user press ENTER on an input of a form, the first button 
 *              found in the form is clicked.
 * Notes:
 * - Hidden inputs are obviously excluded.
 * - When input is single-line text, default action is performing a submit. To override this behaviour,
 *   we prevent default action.
 *   (see https://www.w3.org/MarkUp/html-spec/html-spec_8.html#SEC8.2)
 */
(function (){
    const forms = document.getElementsByTagName("form");
    Array.from(forms).forEach(form => {
        const input_fields = form.querySelectorAll('input:not([type="button"]):not([type="hidden"])');
        const button = form.querySelector('input[type="button"]');
        Array.from(input_fields).forEach(input => {
            input.addEventListener("keydown", (e) => {
                if(e.keyCode === 13){
                    e.preventDefault();
                    let click = new Event("click");
                    button.dispatchEvent(click);
                }
            });
        });
    });
})();
/**
 * Check if an AJAX call has been redirected.
 * This means that auth is no longer valid.
 * @param {*} requestURL Request relative url of the call
 * @param {*} responseURL Response url after eventual redirects
 * 
 * Notes:
 * - It's not possible to detect a redirect using response status code, as
 *   if a request is made to the same origin, or the server has CORS enabled,
 *   the 3XX response is followed transparently by XMLHttpRequest.
 *   This default behaviour is not overridable.
 *   (see https://xhr.spec.whatwg.org/#dom-xmlhttprequest-readystate)
 * 
 * - The value of req.responseURL will be the final URL obtained after any redirects.
 *   (see https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest/responseURL)
 * 
 * - As pointed out here https://stackoverflow.com/questions/8056277/how-to-get-response-url-in-xmlhttprequest,
 *   req.responseURL could be empty when CORS request is blocked, or redirection loop is detected.
 */
function checkRedirect(requestURL, responseURL){
    if (!responseURL) {
        console.error("Invalid AJAX call");
        return false;
    } else {
        let actualRequestURL = relPathToAbs(requestURL);
        if (actualRequestURL !== responseURL) { //Url changed
            window.location.assign(responseURL); //Navigate to the url
            return false;
        }
        return true; //Pass the request to callback
    }
    //Else is CORS blocked or redirection loop
}

/**
 * Relative/Absolute path
 * -------------
 * Description: Returns absolute path from relative
 *              (see https://stackoverflow.com/questions/14780350/convert-relative-path-to-absolute-using-javascript)
 * 
 * @param {*} relative Relative path for the request
 */
function relPathToAbs(relative) {
    const stack = window.location.href.split("/"),
        parts = relative.split("/");
    stack.pop(); // remove current file name (or empty string)
    for (let i=0; i<parts.length; i++) {
        if (parts[i] === ".")
            continue;
        if (parts[i] === "..")
            stack.pop(); //One directory back
        else
            stack.push(parts[i]); //Add to path
    }
    return stack.join("/"); //Join everything
}

/**
 * Utils for arrays
 * -------------
 * Description: Adding a modified version of standard include,
 *              with automatic cast during comparison.
 */
Array.prototype.contains = function(element){ 
    for(let i = 0;i<this.length;i++)
        if (this[i] === element)
            return true;
    
    return false;
}

function myMakeCall(method, url, formElement, cback, reset = true) {
    const req = new XMLHttpRequest(); // visible by closure
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