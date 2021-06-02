class SellerManagerment{
  constructor(container, grade_container,seller_container){
    this.container = container;
    //this.add_container = document.getElementById('main_seller_add_seller').parentElement;
    this.items_container = this.container.getElementsByClassName('main_seller_items')[0];
    this.grade_container = grade_container;
    this.seller_container = seller_container;
    this.initGlobalEvent();
    this.flushItems();
  }

  fetchNewSellers(){
    var url = getUrlByType('shop','seller');
    getJSON(url).then((json)=>{
      if(json['code'] === 200){
        var sellers = json['data'].map((seller)=>{
          return{
            "uid":seller["uid"],
            "name":seller['name'],
            "img":""
          }
        })

        if(sellers){
          for(let seller of sellers){
            this.addItem(seller);
          }
        }
      }
      else{

      }
    })
  }

  flushItems(){
    this.clearAllItem();
    this.fetchNewSellers();
  }

  clearAllItem(){
    var items = this.items_container.getElementsByClassName('main_seller_item');
    var idx = 0, len = items.length;
    while(idx < len){
      if(items[idx].getAttribute('id') === "man_seller_add_enter"){
        idx++;
        continue;
      }
      this.items_container.removeChild(items[idx]);
      len--;
    }
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

  setGradeContainer(container){
    this.grade_container = container;
  }

  setSellerContainer(container){
    this.seller_container = container;
  }

  initGlobalEvent(){
    document.getElementById('main_seller_add_seller').addEventListener('click',()=>{
      this.seller_container.isForPassword(false);
      this.seller_container.setValue({
        "title":"创建销售员",
        "name":""
      })
      this.seller_container.bindItem(this,null);
      this.seller_container.displayContainer();
    })

    this.items_container.addEventListener('click',(e)=>{
      var ele_target = e.target;
      var classname = ele_target.getAttribute('class');
      if(classname === "main_seller_reset"){
        var ele_item = e.path[2];
        this.seller_container.bindItem(this,ele_item);
        this.seller_container.isForPassword(true);
        this.seller_container.setValue({
          "title":"重设密码",
          "name":ele_item.getElementsByClassName('main_seller_name')[0].innerText
        })
        this.seller_container.displayContainer();
      }
      else if(classname === "main_seller_grade"){
        var ele_item = e.path[2];
        this.grade_container.bindItem(this,ele_item);
        this.grade_container.displayContainer();

      }
      else if(classname === "main_seller_remove"){
        var ele_item = e.path[2];
        var uid = ele_item.getAttribute('uid');
        var url = getUrlByType('shop','deleteSeller',{"uid":uid});
        getJSON(url).then((json)=>{
          if(json['code'] === 200){
            this.clearAllItem();
            console.log("删除成功");
          }
          else{
            console.log("删除失败");
          }
        })
      }
    })
  }
  addItem(data){
    this.items_container.append(this.createItem(data));
  }

  createItem(data){
    var container = this.createItemContaner();
    this.initItemValue(container,data);
    this.initItemEvent(container);
    return container;
  }

  initItemEvent(ele){

  }

  initItemValue(ele,data){
    if(!data) return;
    var uid = data['uid'];
    var seller_name = data['name'];
    var seller_photo = data['img'];
    ele.getElementsByClassName('main_seller_name')[0].innerText = seller_name;
    ele.setAttribute('uid',uid);
    if(seller_photo){
      var img = document.createElement('img');
      img.setAttribute('src',seller_photo);
      ele.getElementsByClassName('main_seller_photo')[0].append(img);
    }
    ele.getElementsByClassName('main_seller_grade')[0].innerText = "业绩";
    ele.getElementsByClassName('main_seller_reset')[0].innerText = "重置";
    ele.getElementsByClassName('main_seller_remove')[0].innerText = "删除";
  }

  createItemContaner(){
    var container = createClassEle('div','main_seller_item');
    var photo = createClassEle('div','main_seller_photo');
    var name = createClassEle('div','main_seller_name');
    var func = createClassEle('div','main_seller_func');
    var grade = createClassEle('div','main_seller_grade');
    var reset = createClassEle('div','main_seller_reset');
    var remove = createClassEle('div','main_seller_remove');
    func.append(grade);
    func.append(reset);
    func.append(remove);
    container.append(photo);
    container.append(name);
    container.append(func);
    return container;
  }
}
