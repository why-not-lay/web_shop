class CommodityTrade{
  constructor(container){
    this.container = container;
    this.items_container = this.container.getElementsByClassName('main_items')[0];
    for(var i = 0; i < 10; i++){
      this.addItem({
        "commodity_name":"shop1",
        "user_name":"user1",
        "price":"123",
        "number":"123",
      })
    }
  }

  setCommodityDetailContainer(container){
    this.commodity_detail_container = container;
  }

  setCloseContainer(is_close){
    if(is_close){
      this.container.style.display = "none";
    }
    else{
      this.container.style.display = "block";
    }
  }

  closeContainer(){
    this.setCloseContainer(true);
  }

  displayContainer(){
    this.setCloseContainer(false);
  }

  addItem(data){
    var ele = this.createItem(data);
    this.items_container.append(ele);
  }

  createItem(data){
    var container = this.createContainer();
    this.initItemValue(container,data);
    this.initItemEvent(container);
    return container;
  }

  initItemEvent(ele){

  }

  initStyle(ele){
    ele.style.fontSize = "23px";
  }

  initItemValue(ele,data){
    var data_commodity, data_user, data_price,
      data_number;
    if(!data) return;
    data_commodity = data['commodity_name'];
    data_user = data['user_name'];
    data_price = data['price'];
    data_number = data['number'];
    ele.getElementsByClassName('main_commodity')[0].innerText = data_commodity;
    ele.getElementsByClassName('main_user')[0].innerText = data_user;
    ele.getElementsByClassName('main_price')[0].innerText = data_price;
    ele.getElementsByClassName('main_number')[0].innerText = data_number;
    ele.getElementsByClassName('main_total')[0].innerText = Number.parseInt(data_price) * Number.parseInt(data_number);

  }

  createContainer(){
    var container = createClassEle('div','main_item');
    var commodity = createClassEle('div','main_commodity');
    var user = createClassEle('div','main_user');
    var start_time = createClassEle('div','main_price');
    var leave_time = createClassEle('div','main_number');
    var duration = createClassEle('div','main_total');
    container.append(commodity);
    container.append(user);
    container.append(start_time);
    container.append(leave_time);
    container.append(duration);
    return container;
  }
}
