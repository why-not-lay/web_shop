class SellerCommodity{
  constructor(container){
    this.ele_container = container;
    for(var i = 0; i < 10; i++){
      this.addItem({
        "cid":"123",
        "name":"shop1",
        "price":123,
        "number":12,
        "type":2,
        "status":2
      })

    }
  }

  clearAllItems(){
    var items = ele.getElementsByClassName('commodity_item');
    for(let item of items){
      item.remove();
    }
  }

  addItem(commodity_data){
    var item = this.createItem(commodity_data);
    this.ele_container.append(item);
  }

  createItem(commodity_data){
    var container = this.createItemContainer();
    this.initItemValue(container,commodity_data);
    this.initItemEvent(container);
    return container;
  }

  initItemValue(ele,commodity_data){
    var  commodity_cid,commodity_name, commodity_price, commodity_number,
      commodity_type,commodity_status;
    if(!commodity_data) return;
    commodity_cid = commodity_data['cid'];
    commodity_name = commodity_data['name'];
    commodity_price = commodity_data['price'] ? commodity_data['price'] : 0;
    commodity_number = commodity_data['number'] ? commodity_data['number'] : 0;
    commodity_type = commodity_data['type'] ? commodity_data['type'] : "0";
    commodity_status = commodity_data['status'] ? commodity_data['status'] : "3";

    ele.setAttribute('cid',commodity_cid);
    var ele_input = ele.getElementsByTagName('input');
    console.log(ele_input);
    ele_input[0].value = commodity_name;
    ele_input[1].value = commodity_price;
    ele_input[2].value = commodity_number;
    var ele_select = ele.getElementsByTagName('select');
    ele_select[0].value = commodity_type;
    ele_select[1].value = commodity_status;
  }

  initItemEvent(ele){
    ele.addEventListener('click',(e)=>{
      var old_selected = document.getElementById('commodity_selected');
      old_selected && old_selected.removeAttribute('id');
      ele.setAttribute('id','commodity_selected');
      var ele_target = e.target;
      var classname = ele_target.getAttribute('class');
      if(classname === "commodity_func_item commodity_setting"){

      }
      else if(classname === "commodity_func_item commodity_save"){

      }
      else if(classname === "commodity_func_item commodity_delete"){

      }
    })

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
    option.setAttribute('value','2');
    option.innerText = "上架";
    select.append(option);
    option = document.createElement('option');
    option.setAttribute('value','3');
    option.innerText = "下架";
    select.append(option);
    commodity_status.append(select);

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
    item_container.append(commodity_func);
    return item_container;
  }
}
