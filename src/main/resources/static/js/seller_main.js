(function(){

  window.onload = function(){
    var ele_managerment = document.getElementById('main_commodity_management');
    var ele_trade = document.getElementById('main_commodity_trade');
    var ele_view = document.getElementById('main_commodity_view');
    var ele_commodity_detail = document.getElementById('create_container');

    var commodity_detail_container = new CommodityDetail(ele_commodity_detail);
    var commodity_container = new SellerCommodity(ele_managerment, commodity_detail_container);
    var view_container = new CommodityView(ele_view);
    var trade_container = new CommodityTrade(ele_trade)

    var initSidebar = function() {
      var contaner_old = commodity_container;

      var eles_body_items = document.getElementsByClassName('body_item');
      eles_body_items[0].addEventListener('click',()=>{
        var ele_selected_item = document.getElementById('item_selected');
        ele_selected_item.removeAttribute('id');
        eles_body_items[0].setAttribute('id','item_selected');
        contaner_old.closeContainer()
        contaner_old = commodity_container;
        contaner_old.flushItems();
        contaner_old.displayContainer();
      })
      eles_body_items[1].addEventListener('click',()=>{
        var ele_selected_item = document.getElementById('item_selected');
        ele_selected_item.removeAttribute('id');
        eles_body_items[1].setAttribute('id','item_selected');
        contaner_old.closeContainer();
        contaner_old = trade_container;
        contaner_old.flushItems();
        contaner_old.displayContainer();
      })
      eles_body_items[2].addEventListener('click',()=>{
        var ele_selected_item = document.getElementById('item_selected');
        ele_selected_item.removeAttribute('id');
        eles_body_items[2].setAttribute('id','item_selected');
        contaner_old.closeContainer();
        contaner_old = view_container;
        contaner_old.flushItems();
        contaner_old.displayContainer();
      })
      eles_body_items[3].addEventListener('click',()=>{
        window.location.assign("/shop/logout");
      })
    }

    //commodity_container.setCommodityDetailContainer(commodity_detail_container);
    view_container.setCommodityDetailContainer(commodity_detail_container);
    trade_container.setCommodityDetailContainer(commodity_detail_container);


    initSidebar();

  }
})()
