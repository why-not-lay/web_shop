class CommodityStatistic{
  constructor(container){
    this.container = container;
    this.items_container = this.container.getElementsByClassName('main_items')[0];
  }

  flushItems(){
    this.clearAllItems();
    this.fetchNewGrade();
  }

  clearAllItems(){
    var items = this.items_container.getElementsByClassName('main_seller_grade_item');
    var idx = 0, len = items.length;
    while(idx < len){
      this.items_container.removeChild(items[idx]);
      len--;
    }
  }

  fetchNewGrade(){
    var url = getUrlByType('shop','grade',{"all":"1"});
    getJSON(url).then((json)=>{
      if(json['code'] == 200){
        var grades = json['data'].map((grade)=>{
          return {
            "name":grade['name'],
            "seller":grade['seller_name'],
            "sold_number":grade['sold_number'],
            "cur_price":grade['cur_price'],
            "cur_number":grade['cur_number'],
            "status":grade['status'],
            "income":grade['income']
          }
        });
        if(grades){
          for(let grade of grades){
            this.addItem(grade);
          }
        }
      }
      else{
        console.log("获取失败");
      }
    })
  }

  closeContainer(){
    this.setCloseContainer(true);
  }

  displayContainer(){
    this.setCloseContainer(false);
  }

  setCloseContainer(is_close){
    if(is_close){
      this.container.style.display = "none";
    }
    else{
      this.container.style.display = "block";
    }
  }

  addItem(data){
    this.items_container.append(this.createItem(data));
  }

  createItem(data){
    var container = this.createItemContaner();
    this.initItemValue(container,data);
    this.initItemEvent();
    return container;
  }

  initItemEvent(ele){

  }

  initItemValue(ele,data){
    if(!data) return;
    var name = data['name'];
    var seller = data['seller'];
    var sold_number = data['sold_number'];
    var cur_price = data['cur_price'];
    var cur_number = data['cur_number'];
    var cur_status = data['status'];
    var income = data['income'];

    ele.getElementsByClassName('main_seller_grade_name')[0].innerText = name;
    ele.getElementsByClassName('main_seller_grade_seller')[0].innerText = seller;
    ele.getElementsByClassName('main_seller_grade_sold_number')[0].innerText = sold_number;
    ele.getElementsByClassName('main_seller_grade_cur_price')[0].innerText = cur_price;
    ele.getElementsByClassName('main_seller_grade_cur_number')[0].innerText = cur_number;
    ele.getElementsByClassName('main_seller_grade_status')[0].innerText = cur_status;
    ele.getElementsByClassName('main_seller_grade_income')[0].innerText = income;

  }

  createItemContaner(){
    var container = createClassEle('div','main_seller_grade_item');
    var name = createClassEle('div','main_seller_grade_name');
    var seller = createClassEle('div','main_seller_grade_seller');
    var sold_number = createClassEle('div','main_seller_grade_sold_number');
    var cur_price = createClassEle('div','main_seller_grade_cur_price');
    var cur_number = createClassEle('div','main_seller_grade_cur_number');
    var cur_status = createClassEle('div','main_seller_grade_status');
    var income = createClassEle('div','main_seller_grade_income');

    container.append(name);
    container.append(seller);
    container.append(sold_number);
    container.append(cur_price);
    container.append(cur_number);
    container.append(cur_status);
    container.append(income);
    return container;
  }
}
