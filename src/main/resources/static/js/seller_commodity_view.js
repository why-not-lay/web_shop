class CommodityView{
  constructor(container){
    this.container = container;
    this.items_container = this.container.getElementsByClassName('main_items')[0];
    for(var i = 0; i < 10; i++){
      this.addItem({
        "commodity_name":"shop1",
        "user_name":"user1",
        "start_time":"2001年02月21日 12时19分16秒",
        "leave_time":"2001年02月21日 12时19分16秒",
        "duration":"123"
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

  initItemValue(ele,data){
    var data_commodity, data_user, data_start_time,
      data_leave_time, data_duration;
    if(!data) return;
    data_commodity = data['commodity_name'];
    data_user = data['user_name'];
    data_start_time = data['start_time'];
    data_leave_time = data['leave_time'];
    data_duration = data['duration'];
    ele.getElementsByClassName('main_commodity')[0].innerText = data_commodity;
    ele.getElementsByClassName('main_user')[0].innerText = data_user;
    ele.getElementsByClassName('main_start_time')[0].innerText = data_start_time;
    ele.getElementsByClassName('main_leave_time')[0].innerText = data_leave_time;
    ele.getElementsByClassName('main_duration')[0].innerText = data_duration;
  }

  createContainer(){
    var container = createClassEle('div','main_item');
    var commodity = createClassEle('div','main_commodity');
    var user = createClassEle('div','main_user');
    var start_time = createClassEle('div','main_start_time');
    var leave_time = createClassEle('div','main_leave_time');
    var duration = createClassEle('div','main_duration');
    container.append(commodity);
    container.append(user);
    container.append(start_time);
    container.append(leave_time);
    container.append(duration);
    return container;
  }
}
