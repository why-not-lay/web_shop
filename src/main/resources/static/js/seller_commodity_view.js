class CommodityView{
  constructor(container){
    this.container = container;
    this.items_container = this.container.getElementsByClassName('main_items')[0];
    this.page = 0;
    this.page_num = 10;
    this.initGlobalEvent();
  }

  flushItems(){
    this.clearAllItem();
    this.fetchNewView();
  }

  initPageEvent(){
    var left = this.container.getElementsByClassName('main_page_left')[0];
    var right = this.container.getElementsByClassName('main_page_right')[0];
    left.addEventListener('click',()=>{
      this.decreasePage();
      this.flushItems();
    })
    right.addEventListener('click',()=>{
      this.increasePage();
      this.flushItems();
    })
  }


  initGlobalEvent(){
    this.initPageEvent();
  }

  increasePage(){
    this.page++;
  }
  decreasePage(){
    this.page = this.page - 1 >= 0 ? this.page -1 : 0;
  }

  fetchNewView(){
    var url = getUrlByType('view','get',{"page":this.page,"number":this.page_num});
    getJSON(url).then((json)=>{
      if(json['code'] !== 200){
        console.log(url+"获取记录失败");
        return;
      }
      var views = json['data'].map((view)=>{
        return{
          "commodity_name":view['name'],
          "user_name":view['user'],
          "start_time":view['start_time'],
          "leave_time":view['leave_time'],
          "duration":view['duration']
        }
      })
      if(views && views.length != 0){
        this.clearAllItem();
        for(let view of views){
          this.addItem(view);
        }
      }
      else{
        if(this.page > 0){
          this.decreasePage();
          this.flushItems();
        }
      }

    })
  }

  clearAllItem(){
    var items  = this.items_container.getElementsByClassName('main_item');
    var idx = 0, len = items.length;
    while(idx < len){
      this.items_container.removeChild(items[idx]);
      len--;
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
    if(!data) return;
    var data_commodity = data['commodity_name'];
    var data_user = data['user_name'];
    var data_start_time = data['start_time'];
    var data_leave_time = data['leave_time'];
    var data_duration = data['duration'];
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
