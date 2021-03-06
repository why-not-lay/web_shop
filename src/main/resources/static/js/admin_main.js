(function(){
  window.onload = function() {

    var grade_container = new GradeContainer(document.getElementById('main_seller_grade_contaner'));
    var seller_container = new SellerContainer(document.getElementById('main_seller_create'));
    var seller_managerment_container = new SellerManagerment(document.getElementById('main_seller_managerment'), grade_container, seller_container);
    var commodity_statistic_container = new CommodityStatistic(document.getElementById('main_commodity_statistic'));

    var initSidebar = ()=>{
      var items = document.getElementById('body_container').getElementsByClassName('body_item');
      var old_container = seller_managerment_container;
      items[0].addEventListener('click',()=>{
        var selected_item = document.getElementById('item_selected');
        selected_item.removeAttribute('id');
        items[0].setAttribute('id','item_selected');
        old_container.closeContainer();
        old_container = seller_managerment_container;
        old_container.flushItems();
        old_container.displayContainer();
      })
      items[1].addEventListener('click',()=>{
        var selected_item = document.getElementById('item_selected');
        selected_item.removeAttribute('id');
        items[1].setAttribute('id','item_selected');
        old_container.closeContainer();
        old_container = commodity_statistic_container;
        old_container.flushItems();
        old_container.displayContainer();
      })
      items[2].addEventListener('click',()=>{
        window.location.assign("/shop/logout");
      })
    }

    initSidebar();
  }

})()
