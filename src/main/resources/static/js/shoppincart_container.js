class ShoppingcartContainer{
  constructor(container){
    this.ele_container = container;
    this.ele_cid = {};
    this.globalInit();
  }

  updateTotalMoney(){
    var ele = document.getElementById('shoppingcart_total').children[1];
    var values = document.getElementById('shoppingcart_items').getElementsByClassName('shoppingcart_sum');
    var sum = 0;
    for(let value of values){
      sum += Number.parseInt(value.innerText);
    }
    ele.innerText = sum;
  }

  globalInit(){
    document.getElementById('shoppingcart_bottom_buy').addEventListener('click',()=>{
      try {
        var cids = Object.keys(this.ele_cid);
        var data = [];
        for(let cid of cids){
          let ele = this.ele_cid[cid];
          if(ele.getElementsByTagName('input')[0].checked){
            let number = Number.parseInt(ele.getElementsByTagName('input')[1].value);
            data.push({
              "c":Number.parseInt(cid),
              "n":number
            });
            ele.remove();
            delete this.ele_cid[cid];
          }
        }
        if(Object.keys(data).length === 0){
          console.log("请选择商品");
          return;
        }
        docCookies.setItem('order',JSON.stringify(data));
        window.location.assign("/user/order");
      } catch (error) {
        console.log(error);
      }

    })
  }

  initShoppingcartValue(ele,shoppingcart_data){
    var shoppingcart_name, shoppingcart_price,shoppingcart_number,
      shoppingcart_sum,shoppingcart_cid, shoppingcart_pic_url;
    if(!shoppingcart_data){
      return;
    }
    shoppingcart_name = shoppingcart_data['name'];
    shoppingcart_price = shoppingcart_data['price'];
    shoppingcart_number = shoppingcart_data['number'];
    shoppingcart_sum = Number.parseInt(shoppingcart_price) * Number.parseInt(shoppingcart_number);
    shoppingcart_cid = shoppingcart_data['cid'];
    shoppingcart_pic_url = shoppingcart_data['pic'];

    ele.setAttribute('cid',shoppingcart_cid);
    ele.getElementsByTagName('img')[0].setAttribute('src',shoppingcart_pic_url);
    ele.getElementsByClassName('shoppingcart_goods_name')[0].innerText = shoppingcart_name;
    ele.getElementsByClassName('shoppingcart_price')[0].innerText = shoppingcart_price;
    ele.getElementsByTagName('input')[0].checked = true;
    ele.getElementsByTagName('input')[1].value = shoppingcart_number;
    ele.getElementsByClassName('shoppingcart_sum')[0].innerText = shoppingcart_sum;
    ele.getElementsByClassName('shoppingcart_func')[0].firstElementChild.innerText = "删除";
  }

  initShoppingcartEvent(ele){
    var btn_minus = ele.getElementsByClassName('btn_num_minus')[0];
    var btn_plus = ele.getElementsByClassName('btn_num_plus')[0];
    var ele_value = btn_minus.nextElementSibling
    var ele_sum = ele.getElementsByClassName('shoppingcart_sum')[0];
    var price = Number.parseInt(ele.getElementsByClassName('shoppingcart_price')[0].innerText);
    ele_value.addEventListener('change',()=>{
      var value = Number.parseInt(ele_value.value);
      if(value < 0) ele_value.value = 0;
      ele_sum.innerText = value < 0 ? 0 : value * price;
      this.updateTotalMoney();
    })
    btn_minus.addEventListener('click',()=>{
      var value = Number.parseInt(ele_value.value) - 1;
      btn_minus.nextElementSibling.value = value < 0 ? 0 : value;
      ele_sum.innerText = value < 0 ? 0 : value * price;
      this.updateTotalMoney();
    })
    btn_plus.addEventListener('click',()=>{
      var value = Number.parseInt(ele_value.value)+ 1;
      btn_minus.nextElementSibling.value = value;
      ele_sum.innerText =  value * price;
      this.updateTotalMoney();
    })

    var ele_func = ele.getElementsByClassName('shoppingcart_func')[0].firstElementChild;
    ele_func.addEventListener('click',()=>{
      var cid = ele.getAttribute('cid');
      delete this.ele_cid[cid];
      ele.remove();
      this.updateTotalMoney();
    })

  }

  createShoppingcart(shoppingcart_data){
    var container = this.createShoppingcartContainer();
    this.initShoppingcartValue(container,shoppingcart_data);
    this.initShoppingcartEvent(container);
    return container;
  }

  addShoppingcart(shoppingcart_data){
    var ele = this.ele_cid[shoppingcart_data['cid']];
    if(ele){
      var ele_number = ele.getElementsByTagName('input')[1];
      var ele_sum = ele.getElementsByClassName('shoppingcart_sum')[0]
      var price = Number.parseInt(ele.getElementsByClassName('shoppingcart_price')[0].innerText);
      var num = Number.parseInt(shoppingcart_data['number']);
      var cur = Number.parseInt(ele_number.value);
      ele_number.value = num + cur;
      ele_sum.innerText = (num+cur) * price;
      this.updateTotalMoney();
      return;
    }
    var shopingcart = this.createShoppingcart(shoppingcart_data);
    this.ele_container.append(shopingcart);
    this.updateTotalMoney();
    this.ele_cid[shoppingcart_data['cid']] = shopingcart;
  }

  createShoppingcartContainer(){
    var total_container = createClassEle('div','shoppingcart_item');

    var checkbox = createClassEle('div','shoppingcart_checkbox');
    var tick = document.createElement('input');
    tick.setAttribute('type','checkbox');
    checkbox.append(tick);

    var commodity = createClassEle('div','shoppingcart_commodity');
    var goods = createClassEle('div','shoppingcart_goods');
    var pic = createClassEle('div','shoppingcart_goods_pic');
    pic.append(document.createElement('img'));
    var name = createClassEle('div','shoppingcart_goods_name');
    goods.append(pic);
    goods.append(name);
    commodity.append(goods);

    var price = createClassEle('div','shoppingcart_price');

    var number = createClassEle('div','shoppingcart_number');
    var number_container = createClassEle('div','shoppingcart_number_container');
    var btn = createClassEle('button','btn_num_minus');
    btn.append(document.createElement('i'));
    number_container.append(btn);
    var input = document.createElement('input');
    input.setAttribute('type','number');
    number_container.append(input);
    btn = createClassEle('button','btn_num_plus');
    btn.append(document.createElement('i'));
    number_container.append(btn);
    number.append(number_container);

    var sum = createClassEle('div','shoppingcart_sum');
    var func = createClassEle('div','shoppingcart_func');
    func.append(document.createElement('div'));

    total_container.append(checkbox);
    total_container.append(commodity);
    total_container.append(price);
    total_container.append(number);
    total_container.append(sum);
    total_container.append(func);
    return total_container;
  }

}
