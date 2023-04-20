function getCookie(name) {
    var cookieString = document.cookie;
    var cookies = cookieString.split('; ');
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var separatorIndex = cookie.indexOf('=');
        var cookieName = cookie.substring(0, separatorIndex);
        var cookieValue = cookie.substring(separatorIndex + 1);
        if (cookieName === name) {
            return cookieValue;
        }
    }
    return null;
}
