window.BayShopAPI = window.BayShopAPI || {};
window.BayShopAPI = {
    _base_url : "http://localhost:8080/",

    post : function(endpoint, data, onSuccess, onError){
        return this._request('POST', endpoint, data, onSuccess, onError);
    },
    get : function(endpoint, data, onSuccess, onError){
        return this._request('GET', endpoint, data, onSuccess, onError);
    },
    update : function(endpoint, data, onSuccess, onError){
        return this._request('PUT', endpoint, data, onSuccess, onError);
    },
    delete : function(endpoint, data, onSuccess, onError){
        return this._request('DELETE', endpoint, data, onSuccess, onError);
    },

    _request : function(method, endpoint, data, onSuccess, onError){
        var url = BayShopAPI._base_url + endpoint;

        let ajaxCfg = {
            "url"           : url,
            "async"         : data.async || true,
            "type"          : method,
            "data"          : JSON.stringify(data),
            "dataType"      : "json",
            "headers"       : {
                'Content-Type': 'application/json; charset=utf-8'
            },
            "mimeType"      : "application/json",
        };

        if(onSuccess && typeof onSuccess === 'function'){
            ajaxCfg.success = onSuccess;
        }

        if(onError && typeof onError === 'function'){
            ajaxCfg.error = onError;
        }

        return $.ajax(ajaxCfg);
    }
};