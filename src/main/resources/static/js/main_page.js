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

    var total_checkbox = ele_shoppingcart.getElementsByClassName('shoppingcart_head')[0].getElementsByTagName('input')[0];
    total_checkbox.addEventListener('click',(e)=>{
      var shoppingcart_items = ele_shoppingcart.getElementsByClassName('shoppingcart_item');
      for(let item of shoppingcart_items){
        item.getElementsByTagName('input')[0].checked = e.target.checked;
      }
    })
  }


  window.onload = function() {
    initShoppingcart();
    //initTypeContainer();
    var shoppingcart_container = new ShoppingcartContainer(document.getElementById('shoppingcart_items'));
    var commodity_container = new CommodityContainer(document.getElementById('main'),shoppingcart_container)
    //initNumberMinusPlus();
    var eles_type = document.getElementsByClassName('type');
    var ele_type_cur = eles_type[0];
    for(let ele of eles_type){
      ele.addEventListener('click',function(){
        ele_type_cur.removeAttribute("id");
        ele.setAttribute("id","type_seleted");
        ele_type_cur = ele;
        var type = ele.getAttribute('type');
        commodity_container.clearAllCommodity();
        commodity_container.fetchNewCommodities("/commodity/get?type=" + type)
      })
    }
  }
})();

