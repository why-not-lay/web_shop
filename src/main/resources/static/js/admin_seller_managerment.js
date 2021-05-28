class SellerManagerment{
  constructor(container){
    this.container = container;
    this.items_container = this.container.getElementsByClassName('main_seller_items')[0];
    this.initEvent();
    for(var i = 0; i < 10; i++){
      this.addItem({
        "name":"seller1"
      })
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

  initEvent(){
    document.getElementById('main_seller_add_seller').addEventListener('click',()=>{
      this.seller_container.isForPassword(false);
      this.seller_container.setValue({
        "title":"创建销售员",
        "name":""
      })
      this.seller_container.displayContainer();
    })

    this.items_container.addEventListener('click',(e)=>{
      var ele_target = e.target;
      var classname = ele_target.getAttribute('class');
      if(classname === "main_seller_reset"){
        var ele_item = e.path[2];
        this.seller_container.isForPassword(true);
        this.seller_container.setValue({
          "title":"重设密码",
          "name":ele_item.getElementsByClassName('main_seller_name')[0].innerText
        })
        this.seller_container.displayContainer();
      }
      else if(classname === "main_seller_grade"){
        var ele_item = e.path[2];
        this.grade_container.displayContainer();

      }
      else if(classname === "main_seller_remove"){
        var ele_item = e.path[2];

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
    var seller_name = data['name'];
    var seller_photo = data['img'];
    ele.getElementsByClassName('main_seller_name')[0].innerText = seller_name;
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
