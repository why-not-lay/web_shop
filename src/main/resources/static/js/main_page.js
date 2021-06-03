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

  function initSearchContainer(commodity_container){
    var container = document.getElementById('search');
    var input = container.getElementsByTagName('input')[0];
    var btn = container.getElementsByTagName('button')[0];

    btn.addEventListener('click',()=>{
      var keyword = input.value;
      input.value = "";
      var type = document.getElementById('type_seleted').getAttribute('type');
      var url = getUrlByType('search',"",{"key":keyword,"type":type});
      getJSON(url).then((json)=>{
        if(json['code'] === 200){
          var commodities = json['data'].map((commodity)=>{
            return {
              "cid":commodity['cid'],
              "name":commodity['name'],
              "desc":commodity['description'],
              "price":commodity['price'],
              "pic":""

            }
          })
          commodity_container.clearAllCommodity();
          for(let commodity of commodities){
            commodity_container.addCommodity(commodity);
          }

        }
        else{
          console.log("获取失败",error);
        }
      });
    })
  }



  window.onload = function() {
    initShoppingcart();
    //initTypeContainer();
    var commodity_detail_container = new CommodityDetail(document.getElementById('create_container'),null);
    var shoppingcart_container = new ShoppingcartContainer(document.getElementById('shoppingcart_items'));
    var commodity_container = new CommodityContainer(document.getElementById('main'),shoppingcart_container, commodity_detail_container);
    //initNumberMinusPlus();
    var eles_type = document.getElementsByClassName('type');
    var ele_type_cur = eles_type[0];
    for(let ele of eles_type){
      ele.addEventListener('click',function(){
        ele_type_cur.removeAttribute("id");
        ele.setAttribute("id","type_seleted");
        ele_type_cur = ele;
        var type = ele.getAttribute('type');
        commodity_container.setType(Number.parseInt(type));
      })
    }
    initSearchContainer(commodity_container);
  }
})();

