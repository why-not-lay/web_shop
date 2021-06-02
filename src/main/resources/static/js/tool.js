function getUrlByType(type,operation,params){
  var url = "";
  if(type === "commodity"){
    url += "/commodity";
  }
  else if(type === "trade"){
    url+= "/record/trade";
  }
  else if(type === "view"){
    url+= "/record/view";
  }
  else if(type === "shop"){
    url += "/shop";
  }
  else if(type === "search"){
    url += "/search";
  }

  if(operation === "get"){
    url+= "/get";
  }
  else if(operation === "seller"){
    url+= "/seller";
  }
  else if(operation === "delete"){
    url += "/delete";
  }
  else if(operation === "update"){
    url += "/update";
  }
  else if(operation === "add"){
    url += "/add";
  }
  else if(operation === "register"){
    url += "/registerSeller";
  }
  else if(operation === "reset"){
    url += "/reset";
  }
  else if(operation === "grade"){
    url += "/grade";
  }
  else if(operation === "deleteSeller"){
    url += "/deleteSeller";
  }
  var suffix = ""
  if(params){
    suffix = Object.keys(params).map((key)=>key+"="+params[key]).join("&");
  }
  if(suffix){
    url += "?" + suffix;
  }
  return url;
}
async function getJSON(url) {
  try {
    let response = await fetch(url);
    return await response.json();
  } catch (error) {
    console.log('Request Failed', error);
  }
}

async function PostJSON(url,data) {
  var form_data = new FormData();
  for(let key of Object.keys(data)){
    form_data.append(key,data[key]);
  }
  try {
    let response = await fetch(url,{
      method:"POST",
      //headers:{
      //  'Content-Type':'application/x-www-form-urlencoded'
      //},
      body:form_data
    });
    return await response.json();
  } catch (error) {
    console.log('Request Failed', error);
  }
}

var createClassEle = function(ele_type, classname){
  var ele = document.createElement(ele_type);
  ele.setAttribute('class',classname);
  return ele;
}

var deboune = function(fn,delay) {
  var timer;
  return function() {
    var that = this;
    var argus = arguments;
    if(timer){
      return;
    }
    timer = setTimeout(function(){
      fn.apply(that,argus);
      clearTimeout(timer);
      timer = null;
    },delay);
  }
}

var getScrollTop = function(){
  var scroll_top = 0;
  if(document.documentElement && document.documentElement.scrollTop){
    scroll_top = document.documentElement.scrollTop;
  }
  else if(document.body){
    scroll_top = document.body.scrollTop;
  }
  return scroll_top;
}


var docCookies = {
  getItem: function (sKey) {
    return decodeURIComponent(document.cookie.replace(new RegExp("(?:(?:^|.*;)\\s*" + encodeURIComponent(sKey).replace(/[-.+*]/g, "\\$&") + "\\s*\\=\\s*([^;]*).*$)|^.*$"), "$1")) || null;
  },
  setItem: function (sKey, sValue, vEnd, sPath, sDomain, bSecure) {
    if (!sKey || /^(?:expires|max\-age|path|domain|secure)$/i.test(sKey)) { return false; }
    var sExpires = "";
    if (vEnd) {
      switch (vEnd.constructor) {
        case Number:
          sExpires = vEnd === Infinity ? "; expires=Fri, 31 Dec 9999 23:59:59 GMT" : "; max-age=" + vEnd;
          break;
        case String:
          sExpires = "; expires=" + vEnd;
          break;
        case Date:
          sExpires = "; expires=" + vEnd.toUTCString();
          break;
      }
    }
    document.cookie = encodeURIComponent(sKey) + "=" + encodeURIComponent(sValue) + sExpires + (sDomain ? "; domain=" + sDomain : "") + (sPath ? "; path=" + sPath : "") + (bSecure ? "; secure" : "");
    return true;
  },
  removeItem: function (sKey, sPath, sDomain) {
    if (!sKey || !this.hasItem(sKey)) { return false; }
    document.cookie = encodeURIComponent(sKey) + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT" + ( sDomain ? "; domain=" + sDomain : "") + ( sPath ? "; path=" + sPath : "");
    return true;
  },
  hasItem: function (sKey) {
    return (new RegExp("(?:^|;\\s*)" + encodeURIComponent(sKey).replace(/[-.+*]/g, "\\$&") + "\\s*\\=")).test(document.cookie);
  },
  keys: /* optional method: you can safely remove it! */ function () {
    var aKeys = document.cookie.replace(/((?:^|\s*;)[^\=]+)(?=;|$)|^\s*|\s*(?:\=[^;]*)?(?:\1|$)/g, "").split(/\s*(?:\=[^;]*)?;\s*/);
    for (var nIdx = 0; nIdx < aKeys.length; nIdx++) { aKeys[nIdx] = decodeURIComponent(aKeys[nIdx]); }
    return aKeys;
  }
};
