class CommodityContainer {
  constructor(container,shoppincart_container, commodity_detail_container){
    this.ele_container = container;
    this.shoppincart_container = shoppincart_container;
    this.commodity_detail_container = commodity_detail_container;
    this.type = 10;
    this.page = 0;
    this.page_num = 10;
    this.fetchNewCommodities();
    this.initGlobalEvent();
  }


  flushCommodities(){
    this.clearAllCommodity();
    this.fetchNewCommodities();
  }

  increasePage(){
    this.page++;
  }

  decreasePage(){
    this.page = this.page > 0 ? this.page -1 : 0;
  }

  initGlobalEvent(){
    this.ele_container.addEventListener('click',(e)=>{
      var ele_target = e.target;
      if(ele_target.getAttribute('class') === "commodity_trade_item commodity_trade_item_buy"){
        var ele_top = e.path[5];
        var cid = Number.parseInt(ele_top.getAttribute('cid'));
        var number = Number.parseInt(ele_top.getElementsByTagName('input')[0].value);
        var data = [{
          "c":cid,
          "n":number
        }]
        docCookies.setItem("order",JSON.stringify(data));
        window.location.assign("/user/order");
      }
    });


    var scrollToEnd = ()=>{
      var scroll_top = getScrollTop();
      var scroll_height = document.body.scrollHeight;
      var inner_height = window.innerHeight;
      var epsion = 10;
      console.log(scroll_top);
      console.log(scroll_height);
      console.log(inner_height);
      if(scroll_height - (scroll_top + inner_height) <= epsion){
        console.log("end");
        this.increasePage();
        this.fetchNewCommodities();
      }
    }
    var deScrollToEnd = deboune(scrollToEnd,1000);
    window.addEventListener('scroll',deScrollToEnd);
  }

  setType(type){
    this.type = type;
    this.flushCommodities();
  }

  fetchNewCommodities(){
    var url = getUrlByType('commodity','get',{"page":this.page,"number":this.page_num,"type":this.type});
    console.log(this.page,this.page_num,this.type);
    var promise = getJSON(url);
    promise.then((json)=>{
      if(json['code'] !== 200) return;
      var commodities = json['data'].map((commodity)=>{
        return {
          "cid":commodity['cid'],
          "name":commodity['name'],
          "desc":commodity['description'],
          "price":commodity['price'],
          "pic":"pic/get?cid="+commodity['cid']

        }
      })
      if(commodities && commodities.length !== 0){
        for(let commodity of commodities){
          this.addCommodity(commodity);
        }
      }
      else{
        this.decreasePage();
      }
    })
  }

  clearAllCommodity(){
    var items = this.ele_container.children;
    var idx = 0, len = items.length;
    while(idx < len){
      this.ele_container.removeChild(items[idx]);
      len--;
    }
  }

  addCommodity(commodity_data){
    var commodity = this.createCommodity(commodity_data);
    this.ele_container.append(commodity);
  }

  setElementStyles(styles,ele) {
    var tranStyleName = (style_name) =>{
      return style_name.split('-').map((word,index)=>{
        if(index === 0) return word;
        else return word[0].toLocaleUpperCase() + word.slice(1);
      }).join('');
    }
    var keys = Object.keys(styles);
    for(var key of keys){
      ele.style[tranStyleName(key)] = styles[key];
    }
  }

  initCommodityValue(ele,commodity_data){
    if(!commodity_data){
      return;
    }
    var commodity_name = commodity_data['name'];
    var commodity_desc = commodity_data['desc'];
    var commodity_price = commodity_data['price'];
    var commodity_pic_url = commodity_data['pic'];
    var commodity_cid = commodity_data["cid"];

    ele.setAttribute('cid',commodity_cid);
    ele.getElementsByTagName('img')[0].setAttribute("src",commodity_pic_url);
    ele.getElementsByClassName('commodity_name')[0].firstElementChild.innerText = commodity_name;
    ele.getElementsByClassName('commodity_desc')[0].firstElementChild.innerText = commodity_desc;
    var price_children = ele.getElementsByClassName('commodity_price')[0].children;
    price_children[0].innerText = "￥";
    price_children[1].innerText = commodity_price;
    price_children[2].innerText = ".00";
    ele.getElementsByTagName('input')[0].value = 1;
  }

  initCommodityEvent(ele){
    var btn_minus = ele.getElementsByClassName('btn_num_minus')[0];
    var btn_plus = ele.getElementsByClassName('btn_num_plus')[0];
    var ele_value = btn_minus.nextElementSibling
    ele_value.addEventListener('change',(e)=>{
      var value = Number.parseInt(ele_value.value);
      if(value < 0) ele_value.value = 0;
      e.stopPropagation();
    })
    btn_minus.addEventListener('click',(e)=>{
      var value = Number.parseInt(ele_value.value);
      btn_minus.nextElementSibling.value = value - 1 <= 0 ? 0 : value - 1;
      e.stopPropagation();
    })
    btn_plus.addEventListener('click',(e)=>{
      var value = Number.parseInt(ele_value.value);
      btn_minus.nextElementSibling.value = value + 1;
      e.stopPropagation();
    })

    var btn_buy = ele.getElementsByClassName('commodity_trade_item')[0];
    var btn_shoppingcart = ele.getElementsByClassName('commodity_trade_item')[1];
    btn_buy.addEventListener('click',(e)=>{
      e.stopPropagation();
      var cid = ele.getAttribute('cid');
      var number = ele_value.value;
      docCookies.setItem('order',JSON.stringify([{"c":cid,"n":number}]));
      window.location.assign("/user/order");
    })
    btn_shoppingcart.addEventListener('click',(e)=>{
      e.stopPropagation();
      var number = ele_value.value;
      var cid = ele.getAttribute('cid');
      var name = ele.getElementsByClassName('commodity_name')[0].innerText;
      var pic_url = ele.getElementsByTagName('img')[0].getAttribute('src');
      var price = ele.getElementsByClassName('commodity_price')[0].children[1].innerText;
      this.shoppincart_container.addShoppingcart({
        "cid":cid,
        "price":price,
        "number":number,
        "name":name,
        "pic":pic_url
      });
    })
    ele.addEventListener('click',()=>{
      var cid =ele.getAttribute('cid');
      this.commodity_detail_container.bindItem(this,ele);
      this.commodity_detail_container.setEditable(false);
      this.commodity_detail_container.setValue({
        "title":"商品详情",
        "cid":cid,
        "name":ele.getElementsByTagName('h2')[0].innerText,
        "price":ele.getElementsByTagName('span')[2].innerText,
        "number":ele.getElementsByTagName('input')[0].value,
        "type":this.type,
        "desc":ele.getElementsByTagName('span')[0].innerText,
        "status":"1"
      })
      this.commodity_detail_container.displayContainer();
    })


  }

  createCommodity(commodity_data) {
    var container = this.createCommodityContainer();
    this.initCommodityValue(container,commodity_data);
    this.initCommodityEvent(container);
    return container;
  }


  createCommodityContainer(){
    var total_container = createClassEle('div','commodity');
    var pic_container = createClassEle('div','commodity_pic');
    pic_container.append(document.createElement('img'));

    var desc_container = createClassEle('div','commodity_description');
    var items_container = createClassEle('div','commodity_items');
    var name = createClassEle('div','commodity_item commodity_name');
    name.append(document.createElement('h2'))
    items_container.append(name);

    var desc = createClassEle('div','commodity_item commodity_desc');
    var span = document.createElement('span');
    desc.append(span);
    items_container.append(desc);

    var price = createClassEle('div','commodity_item commodity_price');
    span = document.createElement('span');
    price.append(span);
    span = document.createElement('span');
    price.append(span);
    span = document.createElement('span');
    price.append(span);
    items_container.append(price);

    var number = createClassEle('div','commodity_item commodity_number');
    var div = createClassEle('div','commodity_number');
    var button = createClassEle('button','btn_num_minus');
    button.append(document.createElement('i'));
    div.append(button);
    var input = document.createElement('input');
    input.setAttribute('type','number');
    div.append(input);
    button = createClassEle('button','btn_num_plus');
    button.append(document.createElement('i'));
    div.append(button);
    number.append(div);
    items_container.append(number);

    var func = createClassEle('div','commodity_func');
    div = createClassEle('div','commodity_trade');
    button = createClassEle('button','commodity_trade_item commodity_trade_item_buy');
    button.innerText = "立即购买";
    div.append(button);
    button = createClassEle('button','commodity_trade_item');
    button.innerText = "放入购物车";
    div.append(button);
    func.append(div);
    items_container.append(func);

    desc_container.append(items_container);
    total_container.append(pic_container);
    total_container.append(desc_container);
    return total_container;
  }
}
