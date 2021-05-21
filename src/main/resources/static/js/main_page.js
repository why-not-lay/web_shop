(function(){
  var initShoppingcart = function(){
    var ele_shoppingcart = document.getElementById('shoppingcart_container');
    var ele_shoppingcart_arrow = document.getElementById('shoppingcart_arrow');
    var shoppingcart_trasform = function(){
      if(ele_shoppingcart.style.transform === "translateX(-802px)" || ele_shoppingcart.style.transform === ""){
        ele_shoppingcart.style.transform = "translateX(0)";
      }
      else{
        ele_shoppingcart.style.transform = "translateX(-802px)";
      }
    }
    ele_shoppingcart_arrow.addEventListener('click', shoppingcart_trasform);
    document.getElementsByClassName('topbar_dropdown')[0].lastElementChild.addEventListener('click',shoppingcart_trasform);
  }

  var initTypeContainer = function() {
    var eles_type = document.getElementsByClassName('type');
    var ele_type_cur = eles_type[0];
    for(let ele of eles_type){
      ele.addEventListener('click',function(){
        ele_type_cur.removeAttribute("id");
        ele.setAttribute("id","type_seleted");
        ele_type_cur = ele;
      })
    }
  }


  var getJsonData = async function(url) {
    try {
      var response = await fetch(url);
      if(response.status === 200){
        return await response.json();
      }
      else{
        return null;
      }
    } catch (e) {
      console.log(`Request ${url} error`,error);
      return null;
    }
  }





  window.onload = function() {
    initShoppingcart();
    var commodity_container = new CommodityContainer(document.getElementById('main'))
    commodity_container.addCommodity({
      "cid":"123",
      "name":"commodity1",
      "desc":"this is the description for the commodity1",
      "price":888888,
      "pic":"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic13.nipic.com%2F20110420%2F2531170_133355088479_2.jpg&refer=http%3A%2F%2Fpic13.nipic.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1624176549&t=250eb18554f5360712d6b50e01acd95b"
    });
    //initTypeContainer();
    //initNumberMinusPlus();
  }
})();

