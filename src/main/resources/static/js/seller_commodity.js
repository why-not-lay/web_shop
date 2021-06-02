class SellerCommodity{  constructor(container, commodity_detail_container){
  this.container = container;
  this.commodity_detail_container = commodity_detail_container;
  this.page = 0;
  this.page_num = 10;
  this.items_container = this.container.getElementsByClassName('main_items')[0];
  this.fetchNewCommodities();
  this.initGlobalEvent();
}


flushItems(){
  this.clearAllItems()
  this.fetchNewCommodities();
}

fetchNewCommodities(){
  var get_url = getUrlByType('commodity','get',{"page":this.page,"number":this.page_num})
  var promise = getJSON(get_url);
  promise.then((json)=>{
    if(json['code'] !== 200){
      console.log(get_url+"获取商品失败");
      return;
    }
    var commodities = json['data'].map((commodity)=>{
      return{
        "cid":commodity['cid'],
        "name":commodity['name'],
        'desc':commodity['description'],
        'price':commodity['price'],
        'number':commodity['cur_number'],
        'status':commodity['com_status'],
        'type':commodity['type']
      }
    })
    if(commodities && commodities.length != 0){
      this.clearAllItems();
      for(let commodity of commodities){
        this.addItem(commodity);
      }
    }
    else{
      this.decreasePage();
      this.flushItems();
    }
  })
}

increasePage(){
  this.page++;
}

decreasePage(){
  this.page = this.page - 1 > 0 ? this.page-1 : 0;
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
  this.container.getElementsByClassName('main_page_center')[0].addEventListener('click',()=>{
    this.commodity_detail_container.setEditable(true);
    this.commodity_detail_container.setValue({
      "title":"新建商品",
      "name":"",
      "price":"",
      "number":"",
      "type":"",
      "desc":""
    })
    this.commodity_detail_container.displayContainer();
  })
  this.items_container.addEventListener('click',(e)=>{
    var ele_target = e.target;
    var ele_item = e.path[2];
    var classname = ele_target.getAttribute('class');
    if(classname === "commodity_func_item commodity_setting"){
      this.commodity_detail_container.bindItem(this,ele_item);
      this.commodity_detail_container.setEditable(true);
      this.commodity_detail_container.setValue({
        "title":"商品详情",
        "cid":ele_item.getAttribute("cid"),
        "name":ele_item.getElementsByTagName('input')[0].value,
        "price":ele_item.getElementsByTagName('input')[1].value,
        "number":ele_item.getElementsByTagName('input')[2].value,
        "type":ele_item.getElementsByTagName('select')[0].value,
        "desc":ele_item.getElementsByTagName('input')[3].value,
        "status":ele_item.getElementsByTagName('select')[1].value
      })
      this.commodity_detail_container.displayContainer();
    }
    else if(classname === "commodity_func_item commodity_save"){
      var cid = ele_item.getAttribute("cid");
      var save_url = getUrlByType('commodity','update');
      PostJSON(save_url,{
        "cid":cid,
        "shopname":ele_item.getElementsByTagName('input')[0].value,
        "price":ele_item.getElementsByTagName('input')[1].value,
        "number":ele_item.getElementsByTagName('input')[2].value,
        "type":ele_item.getElementsByTagName('select')[0].value,
        "description":ele_item.getElementsByTagName('input')[3].value,
        "comStatus":ele_item.getElementsByTagName('select')[1].value
      }).then((json)=>{
        if(json['code'] === 200){
          console.log(save_url+"商品保存成功");
          this.flushItems();
        }
        else{
          console.log(save_url+"商品保存失败");
        }
      })

    }
    else if(classname === "commodity_func_item commodity_delete"){
      var cid = ele_item.getAttribute("cid");
      var delete_url = getUrlByType('commodity',"delete",{"cid":cid})
      getJSON(delete_url).then((json)=>{
        if(json['code'] === 200){
          console.log(delete_url+"删除商品成功");
          this.flushItems();
        }
        else{
          console.log(delete_url+"删除商品失败");
        }
      })
    }
  });

  this.initPageEvent();
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

clearAllItems(){
  var items = this.items_container.getElementsByClassName('commodity_item');
  var idx = 0, len = items.length;
  while(idx < len){
    this.items_container.removeChild(items[idx]);
    len--;
  }
  //for(let item of items){
  //  this.items_container.removeChild(item);
  //}
}

addItem(commodity_data){
  var item = this.createItem(commodity_data);
  this.items_container.append(item);
}

createItem(commodity_data){
  var container = this.createItemContainer();
  this.initItemValue(container,commodity_data);
  this.initItemEvent(container);
  return container;
}

setItemValue(ele,commodity_data){
  if(!commodity_data)return;
  var commodity_name = commodity_data['name'];
  var commodity_price = commodity_data['price'];
  var commodity_number = commodity_data['number'];
  var commodity_type = commodity_data['type'];
  var commodity_description = commodity_data['desc'];

  var ele_input = ele.getElementsByTagName('input');
  if(commodity_name){
    ele_input[0].value = commodity_name;
  }
  if(commodity_price){
    ele_input[1].value = commodity_price;
  }
  if(commodity_number){
    ele_input[2].value = commodity_number;
  }
  if(commodity_description){
    ele_input[3].value = commodity_description;
  }
  var ele_select = ele.getElementsByTagName('select');
  if(commodity_type){
    ele_select[0].value = commodity_type;
  }
}

initItemValue(ele,commodity_data){
  if(!commodity_data) return;
  var commodity_cid = commodity_data['cid'];
  var commodity_name = commodity_data['name'];
  var commodity_price = commodity_data['price'] ? commodity_data['price'] : 0;
  var commodity_number = commodity_data['number'] ? commodity_data['number'] : 0;
  var commodity_type = commodity_data['type'] ? commodity_data['type'] : "0";
  var commodity_status = commodity_data['status'] ? commodity_data['status'] : "3";
  var commodity_description = commodity_data['desc'];

  ele.setAttribute('cid',commodity_cid);
  var ele_input = ele.getElementsByTagName('input');
  //console.log(ele_input);
  ele_input[0].value = commodity_name;
  ele_input[1].value = commodity_price;
  ele_input[2].value = commodity_number;
  ele_input[3].value = commodity_description;
  var ele_select = ele.getElementsByTagName('select');
  ele_select[0].value = commodity_type;
  ele_select[1].value = commodity_status;
}

initItemEvent(ele){
  var btn_minus = ele.getElementsByClassName('btn_num_minus')[0];
  var btn_plus = ele.getElementsByClassName('btn_num_plus')[0];
  var ele_value = btn_minus.nextElementSibling
  ele_value.addEventListener('change',()=>{
    var value = Number.parseInt(ele_value.value);
    if(value < 0) ele_value.value = 0;
  })
  btn_minus.addEventListener('click',()=>{
    var value = Number.parseInt(ele_value.value);
    btn_minus.nextElementSibling.value = value - 1 <= 0 ? 0 : value - 1;
  })
  btn_plus.addEventListener('click',()=>{
    var value = Number.parseInt(ele_value.value);
    btn_minus.nextElementSibling.value = value + 1;
  })

}

createItemContainer(){
  var item_container = createClassEle('div','commodity_item');

  var commodity_name = createClassEle('div','commodity_name');
  var input = document.createElement('input');
  input.setAttribute('type','text');
  commodity_name.append(input);

  var commodity_price = createClassEle('div','commodity_price');
  var input = document.createElement('input');
  input.setAttribute('type','number');
  commodity_price.append(input);

  var commodity_number = createClassEle('div','commodity_number');
  var commodity_number_container = createClassEle('div','commodity_number_container');
  var button = createClassEle('button','btn_num_minus');
  button.append(document.createElement('i'));
  commodity_number_container.append(button);
  var input = document.createElement('input');
  input.setAttribute('type','number');
  commodity_number_container.append(input);
  button = createClassEle('button','btn_num_plus');
  button.append(document.createElement('i'));
  commodity_number_container.append(button);
  commodity_number.append(commodity_number_container);

  var commodity_type = createClassEle('div','commodity_type');
  var select = document.createElement('select');
  select.setAttribute('id','commodity_type_select');
  var option = document.createElement('option');
  option.setAttribute('value','0');
  option.innerText = "未分类";
  select.append(option);
  option = document.createElement('option');
  option.setAttribute('value','1');
  option.innerText = "衣";
  select.append(option);
  option = document.createElement('option');
  option.setAttribute('value','2');
  option.innerText = "食";
  select.append(option);
  option = document.createElement('option');
  option.setAttribute('value','3');
  option.innerText = "住";
  select.append(option);
  option = document.createElement('option');
  option.setAttribute('value','4');
  option.innerText = "行";
  select.append(option);
  option = document.createElement('option');
  option.setAttribute('value','5');
  option.innerText = "用";
  select.append(option);
  commodity_type.append(select);

  var commodity_status = createClassEle('div','commodity_status');
  select = document.createElement('select');
  select.setAttribute('id','commodity_status_select');
  option = document.createElement('option');
  option.setAttribute('value','1');
  option.innerText = "上架";
  select.append(option);
  option = document.createElement('option');
  option.setAttribute('value','3');
  option.innerText = "下架";
  select.append(option);
  commodity_status.append(select);

  var commodity_description = createClassEle('div','commodity_description');
  var input = document.createElement('input');
  input.setAttribute('type','hidden');
  commodity_description.append(input);

  var commodity_func = createClassEle('div','commodity_func');
  var func_item = createClassEle('div','commodity_func_item commodity_setting');
  func_item.innerText = "设置";
  commodity_func.append(func_item);
  func_item = createClassEle('div','commodity_func_item commodity_save');
  func_item.innerText = "保存";
  commodity_func.append(func_item);
  func_item = createClassEle('div','commodity_func_item commodity_delete');
  func_item.innerText = "删除";
  commodity_func.append(func_item);

  item_container.append(commodity_name);
  item_container.append(commodity_price);
  item_container.append(commodity_number);
  item_container.append(commodity_type);
  item_container.append(commodity_status);
  item_container.append(commodity_description);
  item_container.append(commodity_func);
  return item_container;
}
}
