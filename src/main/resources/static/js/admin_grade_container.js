class GradeContainer{
  constructor(contaner){
    this.container = contaner;
    this.items_container = this.container.getElementsByClassName('main_seller_grade_items')[0];
    this.parent_container = null;
    this.binding_item = null;
    this.initEvent();
  }

  bindItem(parent_container,item){
    this.parent_container = parent_container;
    this.binding_item = item;
  }

  flushItems(){
    this.clearAllItem();
    this.fetchNewGrade();
  }

  clearAllItem(){
    this.setTotalIncome(0);
    var items = this.items_container.getElementsByClassName('main_seller_grade_item');
    var idx = 0, len = items.length;
    while(idx < len){
      this.items_container.removeChild(items[idx]);
      len--;
    }
  }

  fetchNewGrade(){
    var uid = this.binding_item.getAttribute('uid');
    var url = getUrlByType('shop','grade',{"uid":uid});
    getJSON(url).then((json)=>{
      if(json['code'] === 200){
        var grades = json['data'].map((grade)=>{
          return {
            "name":grade['name'],
            "status":grade['status'],
            "cur_price":grade['cur_price'],
            "sold_number":grade['sold_number'],
            "cur_number":grade['cur_number'],
            "income":grade['income']
          }
        })
        if(grades){
          for(let grade of grades){
            this.addItem(grade);
          }
        }
      }
      else{
        console.log("员工业绩获取失败");
      }
    })
  }

  initEvent(){
    this.container.getElementsByTagName('button')[0].addEventListener('click',()=>{
      this.closeContainer();
    })
  }

  setTotalIncome(value){
    var ele_total_ele = this.container.getElementsByClassName('main_seller_grade_total_income')[0];
    ele_total_ele.getElementsByTagName('span')[1].innerText = value;
  }

  getTotalIncome(){
    var ele_total_ele = this.container.getElementsByClassName('main_seller_grade_total_income')[0];
    var value = ele_total_ele.getElementsByTagName('span')[1].innerText;
    return value ? Number.parseInt(value) : 0;
  }

  closeContainer(){
    this.setCloseContainer(true);
  }

  displayContainer(){
    this.flushItems();
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
    var income = Number.parseInt(data['income']);
    this.setTotalIncome(this.getTotalIncome() + income);
  }

  createItem(data){
    var container = this.createItemContaner();
    this.initItemValue(container,data);
    return container;
  }

  initItemValue(ele,data){
    if(!data)return;
    console.log(data);
    var name = data['name'];
    var sold_number = data['sold_number'];
    var cur_price = data['cur_price'];
    var cur_number = data['cur_number'];
    var cur_status = data['status'];
    var income = data['income'];

    ele.getElementsByClassName('main_seller_grade_name')[0].innerText = name;
    ele.getElementsByClassName('main_seller_grade_sold_number')[0].innerText = sold_number;
    ele.getElementsByClassName('main_seller_grade_cur_price')[0].innerText = cur_price;
    ele.getElementsByClassName('main_seller_grade_cur_number')[0].innerText = cur_number;
    ele.getElementsByClassName('main_seller_grade_status')[0].innerText = cur_status;
    ele.getElementsByClassName('main_seller_grade_income')[0].innerText = income;
  }

  createItemContaner(){
    var container = createClassEle('div','main_seller_grade_item');
    var name = createClassEle('div','main_seller_grade_name');
    var sold_number = createClassEle('div','main_seller_grade_sold_number');
    var cur_price = createClassEle('div','main_seller_grade_cur_price');
    var cur_number = createClassEle('div','main_seller_grade_cur_number');
    var cur_status = createClassEle('div','main_seller_grade_status');
    var income = createClassEle('div','main_seller_grade_income');
    container.append(name);
    container.append(sold_number);
    container.append(cur_price);
    container.append(cur_number);
    container.append(cur_status);
    container.append(income);
    return container;
  }

}
